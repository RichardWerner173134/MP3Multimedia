package model;

import lombok.Getter;

@Getter
public class ContentTimeStamp {
    private int starttime;

    public ContentTimeStamp(int starttime){
        this.starttime = starttime;
    }

    public static ContentTimeStamp compareTo(ContentTimeStamp c1, ContentTimeStamp c2){
        if(c1.starttime < c2.starttime){
            return c1;
        } else if(c1.starttime == c2.starttime){
            return c1;
        }
        return c2;
    }
}
