import gui.frame.EditorFrame;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // old: new MP3DemoFrame();
        EventQueue.invokeLater(() -> {
            try {
                EditorFrame frame = new EditorFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}