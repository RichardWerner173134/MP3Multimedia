package components;

import lombok.Getter;
import model.*;
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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MP3Enricher {

    private static void addSYLTFrame(MP3File mp3File, byte [] bytes) {
        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();
        ID3v24Frame frame = null;

        if(tag.frameMap.containsKey("SYLT")){
            tag.frameMap.remove("SYLT");
        }
        ByteBuffer buff = ByteBuffer.allocate(bytes.length);
        buff.put(bytes);

        frame = new ID3v24Frame("SYLT");
        byte[] data = buff.array();
        FrameBodySYLT framebody = new FrameBodySYLT(0, "eng", 2,
                8, "rwernerMultimediaApp", data);
        frame.setBody(framebody);
        tag.frameMap.put("SYLT", frame);
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

    public static void addAPICFrame(MP3File mp3File, AbstractID3v2Frame frame){
        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();

        if(!tag.frameMap.containsKey("APIC")){
            tag.frameMap.put("APIC", new ArrayList<>());
        }

        if(tag.frameMap.get("APIC") instanceof AbstractID3v2Frame){
            AbstractID3v2Frame oldFrame = (AbstractID3v2Frame)tag.frameMap.get("APIC");
            tag.frameMap.remove("APIC");
            ArrayList<AbstractID3v2Frame> list = new ArrayList<>(Collections.singletonList(oldFrame));
            tag.frameMap.put("APIC", list);
        }
        ((ArrayList)tag.frameMap.get("APIC")).add(frame);
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

        LinkedList<Entry> entryList = new LinkedList<>();

        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Iterator it2 = ((ImageModel) pair.getValue()).getTimestampMap().entrySet().iterator();
            while(it2.hasNext()){
                Map.Entry pair2 = (Map.Entry) it2.next();
                String imageName = String.valueOf(pair.getKey());
                int timestamp = ((ContentTimeStamp) pair2.getValue()).getStarttime();
                entryList.add(new Entry(imageName, timestamp));
            }
        }

        entryList.sort((o1, o2) -> {
            if (o1.timestamp < o2.timestamp) {
                //System.out.println(o1.timestamp + " < " + o2.timestamp);
                return -1;
            } else if(o1.timestamp == o2.timestamp) {
                return 0;
            } else {
                //System.out.println(o1.timestamp + " > " + o2.timestamp);
                return 1;
            }
        });

        Iterator<Entry> entryIt = entryList.iterator();

        while(entryIt.hasNext()){
            Entry e = entryIt.next();

            //APIC Frame hinzufügen
            if(!isImageAttached(mp3Model.getMp3File(), e.key)){
                attachImage(mp3Model.getImageModelMap().get(e.key).getBufferedImage(),
                        e.key,
                        mp3Model.getMp3File());
            }


            // SYLT Frame Bytes generieren bauen
            try {
                if(baos.toByteArray().length == 0){
                    baos.write(StandardCharsets.ISO_8859_1.encode(e.key).array());
                } else{
                    baos.write(StandardCharsets.ISO_8859_1.encode("\n" + e.key).array());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            byte[] timestampBytes = calcTimestampBytes(e.timestamp);

            try {
                baos.write(0x00);
                baos.write(timestampBytes);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        // add SYLT Frame
        addSYLTFrame(mp3Model.getMp3File(), baos.toByteArray());

        saveFile(mp3Model.getMp3File());


    }

    public static int saveFile(MP3File mp3File) {
        try {
            mp3File.save();
        } catch (IOException | TagException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static boolean isImageAttached(MP3File mp3File, String key) {
        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();
        if(tag.frameMap.containsKey("APIC")){
            if(tag.frameMap.get("APIC") instanceof ArrayList){
                for(ID3v24Frame f : (ArrayList<ID3v24Frame>)tag.frameMap.get("APIC")){
                    if (((FrameBodyAPIC)f.getBody()).getDescription().trim().equals(key.trim())){
                        return true;
                    }
                }
            } else if(tag.frameMap.get("APIC") instanceof ID3v24Frame){
                return ((FrameBodyAPIC)((ID3v24Frame)tag.frameMap.get("APIC")).getBody()).getDescription().trim().equals(key.trim());
            }
        }
        return false;
    }

    private static void attachImage(BufferedImage bi, String filename, MP3File mp3File) {
        ID3v24Frame apicFrame = createAPICFrame(getImageBytes(bi), filename);
        addAPICFrame(mp3File, apicFrame);
    }

    private static byte[] calcTimestampBytes(int starttime) {
        return ByteBuffer.allocate(4).putInt(starttime).array();
    }

    @Getter
    public static class Entry {
        private String key;
        private int timestamp;

        public Entry(String key, int timestamp){
            this.key = key;
            this.timestamp = timestamp;
        }
    }
}
