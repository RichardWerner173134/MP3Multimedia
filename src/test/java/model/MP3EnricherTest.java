package model;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


class MP3EnricherTest {

    public static void main(String [] args){
        attachAll();
    }

    @Test
    static void attachAll() {
        MP3Model mp3Model = new MP3Model();
        try {
            mp3Model.setMp3File(new MP3File("C:/Users/Richard/Desktop/kopie.mp3"));
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        ImageModel im = new ImageModel();
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File("C:/Users/Richard/Documents/testbild1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        im.setBufferedImage(bi);
        im.getTimestampsMap().put("1", new ImageTimestamp(0));
        im.getTimestampsMap().put("2", new ImageTimestamp(21));
        im.getTimestampsMap().put("3", new ImageTimestamp(31));

        ImageModel im2 = new ImageModel();
        BufferedImage bi2 = null;
        try {
            bi2 = ImageIO.read(new File("C:/Users/Richard/Documents/testbild2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        im2.setBufferedImage(bi);
        im2.getTimestampsMap().put("1", new ImageTimestamp(41));
        im2.getTimestampsMap().put("2", new ImageTimestamp(51));
        im2.getTimestampsMap().put("3", new ImageTimestamp(61));

        mp3Model.getImageModelMap().put("testbild1.png", im);

        mp3Model.getImageModelMap().put("testbild2.png", im2);

        MP3Enricher.attachAll(mp3Model);
        MP3File mp3File = null;
        try {
            mp3File = new MP3File(new File("C:/Users/Richard/Desktop/kopie.mp3"));
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        String s = StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(
                ((FrameBodySYLT) ((ID3v24Frame)mp3File.getID3v2Tag().frameMap.get("SYLT")).getBody()).getLyrics()
        )).toString();
    }
}