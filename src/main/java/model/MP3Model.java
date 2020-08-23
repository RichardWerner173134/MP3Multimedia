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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MP3Model {
    private MP3File mp3File;
    private HashMap<String, AbstractContentModel> abstractContentModelMap;

    public MP3Model(){
        abstractContentModelMap = new HashMap<>();
    }

    public void addImage(String filename, BufferedImage bufferedImage, int starttime){
        if(abstractContentModelMap.containsKey(filename)){
            if(abstractContentModelMap.get(filename) instanceof ImageModel) {
                boolean containsIdenticalTimestamp = abstractContentModelMap.get(filename).getTimestampMap().values()
                        .stream()
                        .map(ts -> ts.getStarttime())
                        .collect(Collectors.toList()).contains(starttime);
                if(!containsIdenticalTimestamp){
                    ((ImageModel) abstractContentModelMap.get(filename))
                            .getTimestampMap()
                            .put(String.valueOf(((ImageModel) abstractContentModelMap.get(filename)).getTimestampMap().size()),
                                    new ContentTimeStamp(starttime));
                    ((ImageModel)abstractContentModelMap.get(filename)).setBufferedImage(bufferedImage);
                }
            }
        } else{
            abstractContentModelMap.put(filename, new ImageModel());
            ((ImageModel)abstractContentModelMap.get(filename))
                    .getTimestampMap()
                    .put(String.valueOf(abstractContentModelMap.get(filename).getTimestampMap().size()),
                            new ContentTimeStamp(starttime));
            ((ImageModel)abstractContentModelMap.get(filename)).setBufferedImage(bufferedImage);
        }

    }

    public void addSubtitle(String subtitleContent, int starttime){
        if(abstractContentModelMap.containsKey(subtitleContent)){
            boolean containsIdenticalTimestamp = abstractContentModelMap.get(subtitleContent).getTimestampMap().values()
                    .stream()
                    .map(ts -> ts.getStarttime())
                    .collect(Collectors.toList()).contains(starttime);

            if(!containsIdenticalTimestamp) {
                abstractContentModelMap.get(subtitleContent)
                        .getTimestampMap()
                        .put(String.valueOf(abstractContentModelMap.get(subtitleContent).getTimestampMap().size()),
                                new ContentTimeStamp(starttime));
            }
        } else {
            abstractContentModelMap.put(subtitleContent, new SubtitleModel());
            abstractContentModelMap.get(subtitleContent)
                    .getTimestampMap()
                    .put(String.valueOf(abstractContentModelMap.get(subtitleContent).getTimestampMap().size()),
                            new ContentTimeStamp(starttime));
        }
    }

    public void setMp3File(MP3File mp3File){
        this.mp3File = mp3File;
        loadAbstractContentModel();
    }

    private void loadAbstractContentModel() {
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
                    parseSYLTFrame((ID3v24Frame) tag.frameMap.get("SYLT"), apicFrameList);
                } else if(tag.frameMap.get("SYLT") instanceof ArrayList){
                    parseSYLTFrameList((ArrayList)tag.frameMap.get("SYLT"), apicFrameList);
                }
            }
        }
    }

    private void parseSYLTFrame(ID3v24Frame syltFrame, ArrayList<ID3v24Frame> apicFrameList) {
        FrameBodySYLT body = (FrameBodySYLT) syltFrame.getBody();
        byte[] data = body.getLyrics();
        int contentType = body.getContentType();
        String language = body.getLanguage();
        int timeStampFormat = body.getTimeStampFormat();
        String description = body.getDescription();
        byte textEncoding = body.getTextEncoding();

        abstractContentModelMap = ByteExtractor.getAbstractContentModelMap(
                data,
                contentType,
                timeStampFormat,
                description,
                textEncoding,
                language,
                apicFrameList);


    }

    private void parseSYLTFrameList(ArrayList<ID3v24Frame> syltFrames, ArrayList<ID3v24Frame> apicFrameList) {
        for(ID3v24Frame frame : syltFrames){
            parseSYLTFrame(frame, apicFrameList);
        }
    }
}
