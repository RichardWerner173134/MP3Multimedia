package model;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MP3Enricher {
    public static void addFrameToMP3(MP3File mp3File, AbstractID3v2Frame frame){
        String key = "";
        if(frame.getBody() instanceof FrameBodyAPIC){
            key = "APIC";
        }
        else if(frame.getBody() instanceof FrameBodySYLT){
            key = "SYLT";
        }

        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();

        if(tag.frameMap.get(key) instanceof AbstractID3v2Frame){
            AbstractID3v2Frame oldFrame = (AbstractID3v2Frame)tag.frameMap.get(key);
            tag.frameMap.remove(key);
            ArrayList<AbstractID3v2Frame> list = new ArrayList<>(Collections.singletonList(oldFrame));
            tag.frameMap.put(key, list);
        }
        mp3File.getID3v2Tag().frameMap.put(key, frame);
    }

    public static AbstractID3v2FrameBody createFrameBody(String id, byte[] data){
        AbstractID3v2FrameBody frameBody = null;
        if(id.equals("APIC")){
            frameBody = new FrameBodyAPIC((byte) 0,
                    "image/png",
                    (byte)0,
                    "",
                    data);

        }
        return frameBody;
    }

    public static String getMP3Info(MP3File mp3File) {
        String info = "";
        ArrayList<AbstractID3v2Frame> frames = new ArrayList<>();

        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();

        for (Object o1 : tag.frameMap.keySet())
        {
            String id = (String) o1;
            Object o = tag.frameMap.get(id);
            //SingleFrames
            if (o instanceof AbstractID3v2Frame)
            {
                frames.add((AbstractID3v2Frame) o);
            }
            //MultiFrames
            else if (o instanceof ArrayList)
            {
                frames.addAll((ArrayList<AbstractID3v2Frame>) o);
            }
        }

        info += "<html><body> Frames der Datei " + mp3File.getFile().getName() + "<br>";
        for(AbstractID3v2Frame frame : frames){
            info += frame.getIdentifier() + ", " + frame.getBody().getSize() + "<br>";
        }
        info += "</body></html>";

        return info;
    }
}
