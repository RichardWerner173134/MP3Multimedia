package gui.frame;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AttachedImageTest {

    @Test
    void getStopTimeForAttachedImage() {
        HashMap<String, AttachedImage> attachedImages = new HashMap<>();
        AttachedImage a1 = new AttachedImage("img01", 0);
        AttachedImage a2 = new AttachedImage("img02", 1000);
        AttachedImage a3= new AttachedImage("img03", 800);
        attachedImages.put("0", a1);
        attachedImages.put("1", a2);
        attachedImages.put("2", a3);

        int stoptime1 = AttachedImage.getStopTimeForAttachedImage("0", attachedImages, 1500);
        int stoptime2 = AttachedImage.getStopTimeForAttachedImage("1", attachedImages, 1500);
        int stoptime3 = AttachedImage.getStopTimeForAttachedImage("2", attachedImages, 1500);

        Assert.assertEquals(800, stoptime1);
        Assert.assertEquals(1500, stoptime2);
        Assert.assertEquals(1000, stoptime3);

    }
}