import gui.frame.EditorFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // old: new MP3DemoFrame();
        SwingUtilities.invokeLater(() -> {
            try {
                EditorFrame frame = new EditorFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}