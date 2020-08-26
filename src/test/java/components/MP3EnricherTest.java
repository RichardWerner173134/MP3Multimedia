package components;

import model.MP3Model;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.stubbing.Answer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static components.MP3Enricher.getMP3Info;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class MP3EnricherTest {

    @Test
    void attachAllToNewMP3() {
        MP3Model mp3Model = new MP3Model();
        MP3File mp3File = new MP3File();
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

        mp3Model.addImage("testbild1.png", bi, 0);
        mp3Model.addImage("testbild1.png", bi, 21);
        mp3Model.addImage("testbild1.png", bi, 31);

        mp3Model.addImage("testbild2.png", bi2, 41);
        mp3Model.addImage("testbild2.png", bi2, 51);
        mp3Model.addImage("testbild2.png", bi2, 61);

        mp3Model.addSubtitle("Das ist Subtitel 1", 24);
        mp3Model.addSubtitle("Das ist Subtitel 1", 33);
        mp3Model.addSubtitle("Das ist ein anderer Subtitel", 28);

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

    @Test
    public void testAttachAll(){
        MP3Model mp3Model = new MP3Model();
        MP3File mp3File = null;
        try {
            mp3File = new MP3File("C:/Users/Richard/Desktop/testfile.mp3");
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

        mp3Model.addImage("testbild1.png", bi, 0);
        mp3Model.addImage("testbild1.png", bi, 21);
        mp3Model.addImage("testbild1.png", bi, 31);

        mp3Model.addImage("testbild2.png", bi2, 41);
        mp3Model.addImage("testbild2.png", bi2, 51);
        mp3Model.addImage("testbild2.png", bi2, 61);

        mp3Model.addSubtitle("Das ist Subtitel 1", 24);
        mp3Model.addSubtitle("Das ist Subtitel 1", 33);
        mp3Model.addSubtitle("Das ist ein anderer Subtitel", 28);

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
}