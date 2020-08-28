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
        MP3Model modelWrite = new MP3Model();
        MP3File mp3File = null;
        try {
            mp3File = new MP3File(new File(dest));
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        modelWrite.setMp3File(mp3File);

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

        modelWrite.addImage("testbild1.png", bi, 15000);
        modelWrite.addImage("testbild1.png", bi, 25000);
        modelWrite.addImage("testbild1.png", bi, 5000);
        modelWrite.addImage("testbild2.png", bi2, 10000);
        modelWrite.addImage("testbild2.png", bi2, 20000);
        modelWrite.addImage("testbild2.png", bi2, 0);

        MP3Enricher.attachAll(modelWrite);

        MP3Model modelRead = new MP3Model();
        MP3File testCompareFile = null;
        try {
            testCompareFile = new MP3File(new File(dest));
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        modelRead.setMp3File(testCompareFile);

        HashMap<String, ImageModel> imageModelMap1 = modelWrite.getImageModelMap();
        modelWrite.getImageModelMap().entrySet().stream()
                .forEach(t -> t.getValue().setTimestampMap(sortByValues(t.getValue().getTimestampMap())));

        HashMap<String, ImageModel> imageModelMap2 = modelRead.getImageModelMap();
        modelRead.getImageModelMap().entrySet().stream()
                .forEach(t -> t.getValue().setTimestampMap(sortByValues(t.getValue().getTimestampMap())));

        Assert.assertTrue(isImageModelMapEualsTo(imageModelMap1, imageModelMap2));
    }


    private boolean isImageModelMapEualsTo(HashMap<String, ImageModel> modelWrite,HashMap<String, ImageModel> modelRead){
        int i = 0;
        /**
         * model1: alle timestamps für bild1 raussuchen
         * für jeden timestamp schauen, ob bei model2 der starttimewert übereinstimmt
         */

        Iterator itM1 = modelWrite.entrySet().iterator();
        while(itM1.hasNext()){
            Map.Entry entry = (Map.Entry) itM1.next();
            String imageName = (String) entry.getKey();
            ImageModel im1 = (ImageModel) entry.getValue();

            Iterator itTimeStamps = im1.getTimestampMap().entrySet().iterator();
            while(itTimeStamps.hasNext()){
                Map.Entry entry2 = (Map.Entry) itTimeStamps.next();

                if(!(((ContentTimeStamp)entry2.getValue()).getStarttime() == modelRead.get(imageName).getTimestampMap()
                        .get((String)entry2.getKey()).getStarttime())){
                    return false;
                }
            }
        }

        return true;
    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((ContentTimeStamp)((Map.Entry) (o1)).getValue()).getStarttime())
                        .compareTo(((ContentTimeStamp)((Map.Entry) (o2)).getValue()).getStarttime());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

}