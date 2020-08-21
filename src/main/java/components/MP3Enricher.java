package components;

import gui.frame.DialogView;
import model.ImageModel;
import model.ImageTimestamp;
import model.MP3Model;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MP3Enricher {

    private static void editSYLTFrame(MP3File mp3File, byte [] bytes) {
        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();
        ID3v24Frame frame = null;

        if(tag.frameMap.containsKey("SYLT")){
            frame = (ID3v24Frame) tag.frameMap.get("SYLT");
            ByteBuffer buff = ByteBuffer.allocate(bytes.length + ((FrameBodySYLT)frame.getBody()).getLyrics().length);
            buff.put(((FrameBodySYLT)frame.getBody()).getLyrics());
            buff.put(bytes);

            ((FrameBodySYLT)frame.getBody()).setLyrics(buff.array());
        }
        else{
            ByteBuffer buff = ByteBuffer.allocate(bytes.length);
            buff.put(bytes);

            frame = new ID3v24Frame("SYLT");
            byte[] data = buff.array();
            FrameBodySYLT framebody = new FrameBodySYLT(0, "eng", 2,
                    0, "rwernerMultimediaApp", data);
            frame.setBody(framebody);
            tag.frameMap.put("SYLT", frame);
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

        if(!tag.frameMap.containsKey(key)){
            tag.frameMap.put("APIC", new ArrayList<>());
        }

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
    public static void attachAll(MP3Model mp3Model) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Iterator it = mp3Model.getImageModelMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            BufferedImage bi = ((ImageModel)pair.getValue()).getBufferedImage();
            String filename = (String) pair.getKey();
            ID3v24Frame apicFrame = createAPICFrame(getImageBytes(bi), filename);
            addFrameToMP3(mp3Model.getMp3File(), apicFrame);

            Iterator it2 = ((ImageModel) pair.getValue()).getTimestampsMap().entrySet().iterator();
            while(it2.hasNext()){

                Map.Entry pair2 = (Map.Entry) it2.next();
                ImageTimestamp ts = (ImageTimestamp) pair2.getValue();

                String newline = "\n<img src=\"" + filename + "\"> ";

                try {
                    baos.write(StandardCharsets.ISO_8859_1.encode(newline).array());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] timestampBytes = calcTimestampBytes(ts.getStarttime());
                try {
                    baos.write(0x00);
                    baos.write(timestampBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        editSYLTFrame(mp3Model.getMp3File(), baos.toByteArray());
        try {
            mp3Model.getMp3File().save();
        } catch (IOException | TagException e) {
            e.printStackTrace();
        }
    }

    private static byte[] calcTimestampBytes(int starttime) {
        /*byte syncByte = 0x00;
        byte b = (byte) 0x0F;
        byte c = (byte) 0xFF;
        byte d = (byte) 0xFF;
        byte e = (byte) 0xFF;
*/

        return ByteBuffer.allocate(4).putInt(starttime).array();
    }
}
