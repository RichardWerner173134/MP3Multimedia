package model;

import lombok.Getter;

@Getter
public class ImageTimestamp {
    private int starttime;
    private int stoptime;

    public ImageTimestamp(int starttime, int stoptime){
        this.starttime = starttime;
        this.stoptime = stoptime;
    }
}
