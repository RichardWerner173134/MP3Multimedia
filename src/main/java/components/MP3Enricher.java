package components;

import lombok.Getter;
import model.TimeStampModel;
import model.ImageModel;
import model.MP3Model;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;


public class MP3Enricher {

    private static void addSYLTFrame(MP3File mp3File, byte [] bytes) {
        AbstractID3v2Tag tag = (AbstractID3v2Tag) mp3File.getID3v2Tag();
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
        AbstractID3v2Tag tag = (AbstractID3v2Tag) mp3File.getID3v2Tag();

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

    public static boolean attachAll(MP3Model mp3Model, File saveFile) {
        if(! (mp3Model.getMp3File().getID3v2Tag() instanceof ID3v24Tag)){
            ID3v24Tag newTag = mp3Model.getMp3File().getID3v2TagAsv24();
            mp3Model.getMp3File().setID3v2Tag(newTag);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Iterator it = mp3Model.getImageModelMap().entrySet().iterator();
        LinkedList<Entry> entryList = new LinkedList<>();

        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Iterator it2 = ((ImageModel) pair.getValue()).getTimeStampModelMap().entrySet().iterator();
            while(it2.hasNext()){
                Map.Entry pair2 = (Map.Entry) it2.next();
                String imageName = String.valueOf(pair.getKey());
                int timestamp = ((TimeStampModel) pair2.getValue()).getStarttime();
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
                baos.write(StandardCharsets.ISO_8859_1.encode(e.key).array());
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }

            byte[] timestampBytes = calcTimestampBytes(e.timestamp);

            try {
                baos.write(0x00);
                baos.write(timestampBytes);
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }

        }

        // add SYLT Frame
        if(!(baos.size() == 0)){
            addSYLTFrame(mp3Model.getMp3File(), baos.toByteArray());
        }

        saveToFile(mp3Model, saveFile);
        return true;
    }


    private static boolean saveToFile(MP3Model mp3Model, File saveFile) {
        if(!saveFile.getAbsolutePath().endsWith(".mp3")){
            saveFile = new File(saveFile.getAbsolutePath() + ".mp3");
        }

        // create saveFile
        if(!saveFile.exists()){
            try {
                if(saveFile.createNewFile()){
                    System.out.println("File " + saveFile.getAbsolutePath() + " created");
                } else {
                    System.out.println("File " + saveFile.getAbsolutePath() + " could not be created");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            Path src = mp3Model.getMp3File().getFile().toPath();
            Path dest = saveFile.toPath();
            try {
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            mp3Model.getMp3File().save(saveFile);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean isImageAttached(MP3File mp3File, String key) {
        AbstractID3v2Tag tag = mp3File.getID3v2Tag();
        if(tag.frameMap.containsKey("APIC")){
            if(tag.frameMap.get("APIC") instanceof ArrayList){
                for(AbstractID3v2Frame f : (ArrayList<AbstractID3v2Frame>)tag.frameMap.get("APIC")){
                    if (((FrameBodyAPIC)f.getBody()).getDescription().trim().equals(key.trim())){
                        return true;
                    }
                }
            } else if(tag.frameMap.get("APIC") instanceof AbstractID3v2Frame){
                return ((FrameBodyAPIC)((AbstractID3v2Frame)tag.frameMap.get("APIC")).getBody()).getDescription().trim().equals(key.trim());
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
