package model;

import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Getter
public class ImageList extends DefaultListModel{

    private HashMap<String, BufferedImage> imageMap;

    public ImageList() {
        imageMap = new HashMap<>();
    }

    public void addImage(File file){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageMap.put(file.getName(), bufferedImage);
        addElement(file.getName());
    }
}
