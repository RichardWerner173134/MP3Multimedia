package components;

import lombok.Getter;
import model.ContentTimeStamp;
import model.ImageModel;
import model.MP3Model;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.mockito.Matchers.any;

class MP3EnricherTest {
    private String src = "C:/Users/Richard/Desktop/src.mp3";
    private String dest = "C:/Users/Richard/Desktop/dest.mp3";

    public void createTestFile(){
        try {
            File f = new File(dest);
            if(f.createNewFile()){
                System.out.println("File create successfully");
            } else{
                System.out.println("File already exists");
            }
            Files.copy(new File(src).toPath(), new File(dest).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void attachAllToNewMP3() {
        createTestFile();
        MP3Model mp3Model = new MP3Model();
        MP3File mp3File = null;
        try {
            mp3File = new MP3File(new File(dest));
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        mp3Model.setMp3File(mp3File);

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File("C:/Users/Richard/Documents/testbild1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage bi2 = null;
        try {
            bi2 = ImageIO.read(new File("C:/Users/Richard/Documents/testbild2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        TimeStampLine timeStampLine1 = new TimeStampLine("testbild1.png", 900000, bi);
        TimeStampLine timeStampLine2 = new TimeStampLine("testbild1.png", 43423, bi);
        TimeStampLine timeStampLine3 = new TimeStampLine("testbild1.png", 2712, bi);
        TimeStampLine timeStampLine4 = new TimeStampLine("testbild2.png", 4856, bi2);
        TimeStampLine timeStampLine5 = new TimeStampLine("testbild2.png", 11000, bi2);
        TimeStampLine timeStampLine6 = new TimeStampLine("testbild2.png", 4123, bi2);
        Set<TimeStampLine> timeStampLineHashMap = new HashSet<>();

        timeStampLineHashMap.add(timeStampLine1);
        timeStampLineHashMap.add(timeStampLine2);
        timeStampLineHashMap.add(timeStampLine3);
        timeStampLineHashMap.add(timeStampLine4);
        timeStampLineHashMap.add(timeStampLine5);
        timeStampLineHashMap.add(timeStampLine6);

        mp3Model.addImage(timeStampLine1.getImageName(), timeStampLine1.getBi(), timeStampLine1.getTimestamp());
        mp3Model.addImage(timeStampLine2.getImageName(), timeStampLine2.getBi(), timeStampLine2.getTimestamp());
        mp3Model.addImage(timeStampLine3.getImageName(), timeStampLine3.getBi(), timeStampLine3.getTimestamp());
        mp3Model.addImage(timeStampLine4.getImageName(), timeStampLine4.getBi(), timeStampLine4.getTimestamp());
        mp3Model.addImage(timeStampLine5.getImageName(), timeStampLine5.getBi(), timeStampLine5.getTimestamp());
        mp3Model.addImage(timeStampLine6.getImageName(), timeStampLine6.getBi(), timeStampLine6.getTimestamp());

        MP3Enricher.attachAll(mp3Model);

        MP3File testCompareFile = null;
        try {
            testCompareFile = new MP3File(new File(dest));
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        mp3Model.setMp3File(testCompareFile);

        Iterator it = mp3Model.getImageModelMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry e = (Map.Entry) it.next();
            String keyString = String.valueOf(e.getKey());
            Iterator it2 = ((ImageModel)e.getValue()).getTimestampMap().entrySet().iterator();

            while(it2.hasNext()){
                Map.Entry e2 = (Map.Entry) it2.next();
                keyString += ((ContentTimeStamp)e2.getValue()).getStarttime();
                if(timeStampLineHashMap.contains())
            }

        }
    }

    @Test
    public void testAttachAll(){
        createTestFile();
        MP3Model mp3Model = new MP3Model();
        MP3File mp3File = null;
        try {
            mp3File = new MP3File(dest);
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        mp3Model.setMp3File(mp3File);

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File("C:/Users/Richard/Documents/testbild1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage bi2 = null;
        try {
            bi2 = ImageIO.read(new File("C:/Users/Richard/Documents/testbild2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }



        TimeStampLine timeStampLine1 = new TimeStampLine("testbild1.png", 900000, bi);
        TimeStampLine timeStampLine2 = new TimeStampLine("testbild1.png", 43423, bi);
        TimeStampLine timeStampLine3 = new TimeStampLine("testbild1.png", 2712, bi);
        TimeStampLine timeStampLine4 = new TimeStampLine("testbild2.png", 4856, bi2);
        TimeStampLine timeStampLine5 = new TimeStampLine("testbild2.png", 11000, bi2);
        TimeStampLine timeStampLine6 = new TimeStampLine("testbild2.png", 4123, bi2);

        mp3Model.addImage(timeStampLine1.getImageName(), timeStampLine1.getBi(), timeStampLine1.getTimestamp());
        mp3Model.addImage(timeStampLine2.getImageName(), timeStampLine2.getBi(), timeStampLine2.getTimestamp());
        mp3Model.addImage(timeStampLine3.getImageName(), timeStampLine3.getBi(), timeStampLine3.getTimestamp());
        mp3Model.addImage(timeStampLine4.getImageName(), timeStampLine4.getBi(), timeStampLine4.getTimestamp());
        mp3Model.addImage(timeStampLine5.getImageName(), timeStampLine5.getBi(), timeStampLine5.getTimestamp());
        mp3Model.addImage(timeStampLine6.getImageName(), timeStampLine6.getBi(), timeStampLine6.getTimestamp());

        MP3Enricher.attachAll(mp3Model);

        String s = StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(
                ((FrameBodySYLT) ((ID3v24Frame) mp3Model.getMp3File().getID3v2Tag().frameMap.get("SYLT")).getBody()).getLyrics()
        )).toString();

        Assert.assertEquals("<img src=\"testbild1.png\"\n" +
                "<img src=\"testbild1.png\"\n" +
                "Das ist Subtitel 1\n" +
                "Das ist ein anderer Subtitel\n" +
                "<img src=\"testbild1.png\"\n" +
                "Das ist Subtitel 1\n" +
                "<img src=\"testbild2.png\"\n" +
                "<img src=\"testbild2.png\"\n" +
                "<img src=\"testbild2.png\"", s);
    }

    @Getter
    class TimeStampLine{
        private String imageName;
        private int timestamp;
        private BufferedImage bi;
        private boolean checked;

        TimeStampLine(String imageName, int timestamp, BufferedImage bi){
            this.imageName = imageName;
            this.timestamp = timestamp;
            this.bi = bi;
            checked = false;
        }
    }
}