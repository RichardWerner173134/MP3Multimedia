package model;

import components.MP3Enricher;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


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

        mp3Model.addSubtitle("Der einzige Subtitle tritt dreimal auf", 24);
        mp3Model.addSubtitle("Der einzige Subtitle tritt dreimal auf", 33);
        mp3Model.addSubtitle("Der einzige Subtitle tritt dreimal auf", 28);

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