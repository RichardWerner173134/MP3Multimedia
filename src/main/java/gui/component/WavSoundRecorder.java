package gui.component;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class WavSoundRecorder {
    // record duration, in milliseconds


    // path of the wav file
    private File wavFile;

    // format of audio file
    AudioFileFormat.Type fileType;

    // the line from which audio data is captured
    TargetDataLine line;

    public WavSoundRecorder() {
        fileType = AudioFileFormat.Type.WAVE;
    }

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    /**
     * Captures the sound and record into a WAV file
     */
    public void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing

            System.out.println("capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void pause(){
        line.stop();
        line.close();
        System.out.println("Recording paused");
    }

    public void continueRec(){
        start();
    }
    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    public void setWavFile(File wavFile) {
        this.wavFile = wavFile;
    }

    public static AudioInputStream concatenateFiles(List<File> files){
        AudioInputStream target = null;

        for(File file : files){
            try {
                target = joinWavFiles(target, AudioSystem.getAudioInputStream((file)));
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            target.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }

    public void saveStreamToFile(AudioInputStream ais, Path path){
        try {
            AudioSystem.write(ais,
                    AudioFileFormat.Type.WAVE,
                    new File(path.toAbsolutePath().toString()));
            System.out.println("File saved successfully at: " + path.toAbsolutePath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static AudioInputStream joinWavFiles(AudioInputStream stream1, AudioInputStream stream2){
        if(stream1 == null){
            return stream2;
        }
        return new AudioInputStream(
                new SequenceInputStream(stream1, stream2),
                        stream1.getFormat(),
                        stream1.getFrameLength() + stream2.getFrameLength());
    }
}
