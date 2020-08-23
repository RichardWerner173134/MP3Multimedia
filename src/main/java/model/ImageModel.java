package model;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class ImageModel extends AbstractContentModel{
    private BufferedImage bufferedImage;

    public ImageModel(){
        super();
    }
}
