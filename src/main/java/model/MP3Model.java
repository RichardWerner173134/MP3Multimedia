package model;

import lombok.Getter;
import lombok.Setter;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;
import util.ByteExtractor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class MP3Model {
    private MP3File mp3File;
    private HashMap<String, ImageModel> imageModelMap;

    public MP3Model(){
        imageModelMap = new HashMap<>();
    }

    public void addImage(String filename, BufferedImage bufferedImage, int starttime){
        if(imageModelMap.containsKey(filename)){
            if(imageModelMap.get(filename) instanceof ImageModel) {
                boolean containsIdenticalTimestamp = imageModelMap.get(filename).getTimestampMap().values()
                        .stream()
                        .map(ts -> ts.getStarttime())
                        .collect(Collectors.toList()).contains(starttime);
                if(!containsIdenticalTimestamp){
                    imageModelMap.get(filename)
                            .getTimestampMap()
                            .put(String.valueOf(starttime),
                                    new ContentTimeStamp(starttime));
                    imageModelMap.get(filename).setBufferedImage(bufferedImage);
                }
            }
        } else{
            imageModelMap.put(filename, new ImageModel());
            imageModelMap.get(filename)
                    .getTimestampMap()
                    .put(String.valueOf(starttime),
                            new ContentTimeStamp(starttime));
            imageModelMap.get(filename).setBufferedImage(bufferedImage);
        }

    }

    public void setMp3File(MP3File mp3File){
        this.mp3File = mp3File;
        loadImageModelMap();
    }

    private void loadImageModelMap() {
        if(!mp3File.hasID3v2Tag()){
            ID3v24Tag tag = new ID3v24Tag();
            mp3File.setID3v2Tag(tag);
        } else {
            ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();
            ArrayList<ID3v24Frame> apicFrameList = new ArrayList<>();
            if(tag.frameMap.containsKey("APIC")){
                if(tag.frameMap.get("APIC") instanceof ArrayList){
                    apicFrameList.addAll((ArrayList)tag.frameMap.get("APIC"));
                } else if(tag.frameMap.get("APIC") instanceof ID3v24Frame){
                    apicFrameList.add((ID3v24Frame)tag.frameMap.get("APIC"));
                }
            }
            if(tag.frameMap.containsKey("SYLT")){
                if(tag.frameMap.get("SYLT") instanceof AbstractID3v2Frame){
                    // parsing Timestamps if Sylt contentType == 8
                    if(isCorrectContentType((ID3v24Frame) tag.frameMap.get("SYLT"))){
                        parseSYLTFrame((ID3v24Frame) tag.frameMap.get("SYLT"), apicFrameList);
                    }
                }
                tag.frameMap.remove("SYLT");
            }
        }
    }

    private boolean isCorrectContentType(ID3v24Frame frame) {
        return ((FrameBodySYLT)frame.getBody()).getContentType() == 8;
    }

    private void parseSYLTFrame(ID3v24Frame syltFrame, ArrayList<ID3v24Frame> apicFrameList) {
        FrameBodySYLT body = (FrameBodySYLT) syltFrame.getBody();
        byte[] data = body.getLyrics();
        int contentType = body.getContentType();
        String language = body.getLanguage();
        int timeStampFormat = body.getTimeStampFormat();
        String description = body.getDescription();
        byte textEncoding = body.getTextEncoding();

        imageModelMap = ByteExtractor.getAbstractContentModelMap(
                data,
                contentType,
                timeStampFormat,
                description,
                textEncoding,
                language,
                apicFrameList);


    }

}
