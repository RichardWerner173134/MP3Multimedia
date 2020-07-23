package gui.component;

import javax.sound.sampled.*;
import java.io.*;
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

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void pause(){
        finish();
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

    public File getWavFile() {
        return wavFile;
    }

    public void setWavFile(File wavFile) {
        this.wavFile = wavFile;
    }

    public static void concatenateAllWAVFiles(List<File> files){
        AudioInputStream target = null;
        try {
            target = AudioSystem.getAudioInputStream(files.get(0));
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        files.remove(0);

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
            AudioSystem.write(target,
                    AudioFileFormat.Type.WAVE,
                    new File("./target/sound/final.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //löschen des restes

    }

    public static AudioInputStream joinWavFiles(AudioInputStream stream1, AudioInputStream stream2){
        return new AudioInputStream(
                new SequenceInputStream(stream1, stream2),
                        stream1.getFormat(),
                        stream1.getFrameLength() + stream2.getFrameLength());
    }
}
