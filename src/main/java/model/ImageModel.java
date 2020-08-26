package model;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@Getter
@Setter
public class ImageModel{
    private HashMap<String, ContentTimeStamp> timestampMap;
    private BufferedImage bufferedImage;

    public ImageModel(){
        timestampMap = new HashMap<>();
    }
}
