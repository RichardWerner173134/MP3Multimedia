package model;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@Getter
@Setter
public class ImageModel{
    private HashMap<String, TimeStampModel> timeStampModelMap;
    private BufferedImage bufferedImage;

    public ImageModel(){
        timeStampModelMap = new HashMap<>();
    }
}
