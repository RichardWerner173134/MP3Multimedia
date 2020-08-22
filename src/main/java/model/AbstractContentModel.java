package model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public abstract class AbstractContentModel {
    private HashMap<String, ContentTimeStamp> timestampMap;

    public AbstractContentModel(){
        timestampMap = new HashMap<>();
    }
}
