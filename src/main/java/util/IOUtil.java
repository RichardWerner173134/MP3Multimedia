package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class IOUtil {

    public static BufferedImage loadImgFromResources(String filePath) {
        BufferedImage img = null;
        ClassLoader cl = IOUtil.class.getClassLoader();
        if(cl != null) {
            try {
                img = ImageIO.read(IOUtil.class.getClassLoader().getResource(filePath));
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
            return img;
        }
        return null;
    }
}
