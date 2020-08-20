package model;

import lombok.Getter;

@Getter
public class ImageTimestamp {
    private int starttime;

    public ImageTimestamp(int starttime){
        this.starttime = starttime;
    }
}
