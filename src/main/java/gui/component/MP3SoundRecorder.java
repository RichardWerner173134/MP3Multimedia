package gui.component;

import ws.schild.jave.*;

import java.io.File;

public class MP3SoundRecorder {
    private File source;
    private File target;
    private Encoder instance;
    private AudioAttributes audio;
    private EncodingAttributes attrs;

    public MP3SoundRecorder(String source, String target){
        this.source = new File(source);
        this.target = new File(target);
        instance = new Encoder();
        audio = new AudioAttributes();
        attrs = new EncodingAttributes();
    }

    public void convertWavToMp3() {
        System.out.println("encode mp3");
        attrs.setAudioAttributes(audio);
        attrs.setFormat("mp3");

        audio.setCodec("libmp3lame");
        audio.setBitRate(64000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);
        audio.setBitRate(192000);
        try {
            instance.encode(new MultimediaObject(source), target, attrs, null);
            System.out.println("converting " + source.getAbsolutePath() + " to " + target.getAbsolutePath());
        } catch (EncoderException e) {
            System.out.println("converting failed");
            e.printStackTrace();
        }
    }
}
