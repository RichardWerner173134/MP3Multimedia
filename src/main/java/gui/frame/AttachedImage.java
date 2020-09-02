package gui.frame;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Setter
@Getter
public class AttachedImage extends JButton {
    private String imageTitle;
    private int starttimeMillis;
    private boolean isNewImage;
    private int stoptime;

    public AttachedImage(String imageTitle, int starttimeMillis){
        this.imageTitle = imageTitle;
        this.starttimeMillis = starttimeMillis;
        this.isNewImage = true;
        setText(imageTitle);
    }

    public static int getStopTimeForAttachedImage(String id, HashMap<String, AttachedImage> attachedImages, int tracklength){
        // gét attachedImage from Collection
        AttachedImage attachedImage = attachedImages.get(id);

        int starttime1 = attachedImage.starttimeMillis;
        int stoptime1 = tracklength;

        Iterator it = attachedImages.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            int starttime2 = ((AttachedImage)entry.getValue()).starttimeMillis;
            if(starttime2 > starttime1){
                if(starttime2 < stoptime1){
                    stoptime1 = starttime2;
                }
            }
        }
        return stoptime1;
    }

}
