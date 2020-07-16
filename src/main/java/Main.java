import gui_forms.BasicWindow;
import org.farng.mp3.MP3File;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new BasicWindow().getJPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);    }
}
