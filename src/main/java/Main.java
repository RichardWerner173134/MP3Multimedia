import gui.frame.MyJFrame;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame myFrame = new MyJFrame();

        /*
        MP3File mp3file = null;
        try {
            mp3file = new MP3File(new File("C:/Users/Richard/Desktop/test.mp3"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        }
         * Tag
         *                     mp3file.getID3v2Tag()
         *
         * Frame
         *                     mp3file.getID3v2Tag().getFrame("APIC ")
         *
         * picture data
         *                     mp3file.getID3v2Tag().getFrame("APIC ").getBody().objectList.get(3)
         */

    }
}