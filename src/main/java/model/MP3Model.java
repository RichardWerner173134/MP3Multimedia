package model;

import lombok.Getter;
import lombok.Setter;
import org.jaudiotagger.audio.mp3.MP3File;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@Getter
@Setter
public class MP3Model {
    private MP3File mp3File;
    private boolean fileIsLoaded = false;
    private HashMap<String, ImageModel> imageModelMap;

    public MP3Model(){
        imageModelMap = new HashMap<>();
    }

    public void addImage(String filename, BufferedImage bufferedImage, int starttime, int stoptime){
        if(imageModelMap.containsKey(filename)){
            imageModelMap.get(filename)
                    .getTimestampsMap()
                    .put(String.valueOf(imageModelMap.get(filename).getTimestampsMap().size() + 1),
                            new ImageTimestamp(starttime, stoptime));
        } else{
            imageModelMap.put(filename, new ImageModel());
            imageModelMap.get(filename)
                    .getTimestampsMap()
                    .put(String.valueOf(imageModelMap.get(filename).getTimestampsMap().size() + 1),
                            new ImageTimestamp(starttime, stoptime));
        }
        imageModelMap.get(filename).setBufferedImage(bufferedImage);

    }

    public void setMp3File(MP3File mp3File){
        this.mp3File = mp3File;
        fileIsLoaded = true;
    }
}
