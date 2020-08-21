package model;

import lombok.Getter;
import lombok.Setter;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void addImage(String filename, BufferedImage bufferedImage, int starttime){
        if(imageModelMap.containsKey(filename)){
            imageModelMap.get(filename)
                    .getTimestampsMap()
                    .put(String.valueOf(imageModelMap.get(filename).getTimestampsMap().size() + 1),
                            new ImageTimestamp(starttime));
        } else{
            imageModelMap.put(filename, new ImageModel());
            imageModelMap.get(filename)
                    .getTimestampsMap()
                    .put(String.valueOf(imageModelMap.get(filename).getTimestampsMap().size() + 1),
                            new ImageTimestamp(starttime));
        }
        imageModelMap.get(filename).setBufferedImage(bufferedImage);

    }

    public void setMp3File(MP3File mp3File){
        this.mp3File = mp3File;
        fileIsLoaded = true;
        loadImageModel();
    }

    private void loadImageModel() {
        if(!mp3File.hasID3v2Tag()){
            ID3v24Tag tag = new ID3v24Tag();
            mp3File.setID3v2Tag(tag);
        } else {
            ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();

            if(tag.frameMap.containsKey("SYLT")){
                if(tag.frameMap.get("SYLT") instanceof AbstractID3v2Frame){
                    parseSYLTFrame((ID3v24Frame) tag.frameMap.get("SYLT"));
                } else if(tag.frameMap.get("SYLT") instanceof ArrayList){
                    parseSYLTFrameList((ArrayList)tag.frameMap.get("SYLT"));
                }
            }
        }
    }

    private void parseSYLTFrame(ID3v24Frame syltFrame) {
        FrameBodySYLT body = (FrameBodySYLT) syltFrame.getBody();
        byte[] data = body.getLyrics();
        int contentType = body.getContentType();
        String language = body.getLanguage();
        int timeStampFormat = body.getTimeStampFormat();
        String description = body.getDescription();
        byte textEncoding = body.getTextEncoding();

        String syltString = "";
        if (textEncoding == 0){
            syltString = StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(data)).toString();
        }
        if (textEncoding == 1){
            syltString = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(data)).toString();
        }

        ArrayList<String> syltLine = (ArrayList<String>) Arrays.asList(syltString.split("\n"));



    }

    private void parseSYLTFrameList(ArrayList<ID3v24Frame> syltFrames) {
        for(ID3v24Frame frame : syltFrames){
            parseSYLTFrame(frame);
        }
    }
}
