package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Util {
    public final static String PATH_IMG = "src/main/resources/img/";

    public static BufferedImage loadImgFromResources(String filePath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(PATH_IMG + filePath));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static BufferedImage loadImg(String filePath){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filePath));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
