import gui.frame.EclipseCopyFrame;
import gui.frame.MP3DemoFrame;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // old: new MP3DemoFrame();
        EventQueue.invokeLater(() -> {
            try {
                EclipseCopyFrame frame = new EclipseCopyFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}