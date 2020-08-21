package util;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class test {
    public static byte[][] splitByteArray(byte[] bytes, byte[] regex, Charset charset) {
        String str = new String(bytes, charset);
        String[] split = str.split(new String(regex, charset));
        byte[][] byteSplit = new byte[split.length][];
        for (int i = 0; i < split.length; i++) {
            byteSplit[i] = split[i].getBytes(charset);
        }
        return byteSplit;
    }

    public static void main(String[] args) {
        Charset charset = StandardCharsets.UTF_8;
        MP3File f = null;
        try {
            f = new MP3File("C:/Users/Richard/Desktop/kopie.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }

        byte[] data = ((FrameBodySYLT)((ID3v24Frame)f.getID3v2Tag().frameMap.get("SYLT")).getBody()).getLyrics();

        byte[] regex = {'0', 'A'};
        byte[][] splitted = splitByteArray(data, regex, charset);
        for (byte[] arr : splitted) {
            System.out.print("[");
            for (byte b : arr) {
                System.out.print((char) b);
            }
            System.out.println("]");
        }
    }
}