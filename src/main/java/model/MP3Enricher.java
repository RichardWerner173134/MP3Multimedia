package model;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class MP3Enricher {

    public static void addPicure(MP3File mp3File, File imageFile, int millis){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte [] imageBytes = getImageBytes(bufferedImage);

        addFrameToMP3(mp3File, createAPICFrame(imageBytes, imageFile.getName()));
        addSYLTTimestamp(mp3File, imageFile, millis);
        try {
            mp3File.save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        }
    }

    private static void addSYLTTimestamp(MP3File mp3File, File imageFile, int millis) {
        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();
        ID3v24Frame frame = null;
        String newline = "\n<img src=" + imageFile.getName() + "> (" + millis + "ms)";

        if(tag.frameMap.containsKey("SYLT")){
            frame = (ID3v24Frame) tag.frameMap.get("SYLT");

            String lyrics = StandardCharsets.UTF_8.decode(
                    ByteBuffer.wrap(
                            ((FrameBodySYLT)frame.getBody()).getLyrics())).toString();
            lyrics += newline;
            ((FrameBodySYLT)frame.getBody()).setLyrics(StandardCharsets.UTF_8.encode(lyrics).array());
        }
        else{
            frame = new ID3v24Frame("SYLT");
            byte[] data = StandardCharsets.UTF_8.encode(newline).array();
            FrameBodySYLT framebody = new FrameBodySYLT(0, "eng", 2,
                    1, "rwernerMultimediaApp", data);

            frame.setBody(framebody);
        }


    }

    private static byte[] getImageBytes(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static void addFrameToMP3(MP3File mp3File, AbstractID3v2Frame frame){
        String key = "";
        if(frame.getBody() instanceof FrameBodyAPIC){
            key = "APIC";
        }

        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();

        if(tag.frameMap.get(key) instanceof AbstractID3v2Frame){
            AbstractID3v2Frame oldFrame = (AbstractID3v2Frame)tag.frameMap.get(key);
            tag.frameMap.remove(key);
            ArrayList<AbstractID3v2Frame> list = new ArrayList<>(Collections.singletonList(oldFrame));
            tag.frameMap.put(key, list);
        }
        ((ArrayList)tag.frameMap.get(key)).add(frame);
    }

    public static ID3v24Frame createAPICFrame(byte[] data, String fileName){
        ID3v24Frame frame = new ID3v24Frame("APIC");
        String fileExtension = "image/" + Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1))
                .get();
        byte textEncoding = (byte) 0;
        byte pictureType = (byte) 0;
        FrameBodyAPIC frameBody = new FrameBodyAPIC(textEncoding, fileExtension, pictureType, fileName, data);
        frame.setBody(frameBody);
        return frame;
    }

    public static String getMP3Info(MP3File mp3File) {
        String info = "";
        ArrayList<AbstractID3v2Frame> frames = new ArrayList<>();

        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();

        for (Object o1 : tag.frameMap.keySet())
        {
            String id = (String) o1;
            Object o = tag.frameMap.get(id);
            //SingleFrames
            if (o instanceof AbstractID3v2Frame)
            {
                frames.add((AbstractID3v2Frame) o);
            }
            //MultiFrames
            else if (o instanceof ArrayList)
            {
                frames.addAll((ArrayList<AbstractID3v2Frame>) o);
            }
        }

        info += "<html><body> Frames der Datei " + mp3File.getFile().getName() + "<br>";
        for(AbstractID3v2Frame frame : frames){
            info += frame.getIdentifier() + ", " + frame.getBody().getSize() + "<br>";
        }
        info += "</body></html>";

        return info;
    }
}
