package model;

import lombok.Getter;

@Getter
public class TimeStampModel {
    private int starttime;

    public TimeStampModel(int starttime){
        this.starttime = starttime;
    }

    public static TimeStampModel compareTo(TimeStampModel c1, TimeStampModel c2){
        if(c1.starttime < c2.starttime){
            return c1;
        } else if(c1.starttime == c2.starttime){
            return c1;
        }
        return c2;
    }
}
