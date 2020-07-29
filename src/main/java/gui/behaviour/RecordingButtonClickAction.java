package gui.behaviour;

import gui.component.MP3SoundRecorder;
import gui.component.WavSoundRecorder;
import gui.frame.MyJFrame;
import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecordingButtonClickAction extends AbstractAction {
    private MyJFrame myJFrame;

    final private String directory = "target/sound";
    final private String wavSplitFileName = "RecordAudio";
    final private String wavFormatSuffix = ".wav";
    final private String mp3FormatSuffix = ".mp3";

    final private String filenameFinal = "target/sound/final";

    private WavSoundRecorder wavRecorder = new WavSoundRecorder();
    private MP3SoundRecorder mp3SoundRecorder = new MP3SoundRecorder(
            filenameFinal + wavFormatSuffix,
            filenameFinal + mp3FormatSuffix);

    private List<File> files;

    public RecordingButtonClickAction(MyJFrame myJFrame){
        this.myJFrame = myJFrame;
        files = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = ((JButton)e.getSource());
        myJFrame.toggleButtons(jButton);

        //startRecording
        if (jButton == myJFrame.getMyContainer().getJButtonRecord()){
            File newWavFile = new File(directory + "/" + wavSplitFileName + files.size() + wavFormatSuffix);
            files.add(newWavFile);
            wavRecorder.setWavFile(newWavFile);

            File saveDirectory = new File(directory);
            if(! saveDirectory.exists()){
                saveDirectory.mkdir();
            }

            Thread starterThread = new Thread(wavRecorder::start);

            starterThread.start();
        }

        if (jButton == myJFrame.getMyContainer().getJButtonPause()){
            //pauseRecording
            if(jButton.getText().equals("Pause")){
                jButton.setText("Continue");
                Thread pauseThread = new Thread(wavRecorder::pause);

                pauseThread.start();
            }
            //continueRec
            else{
                File newWavFile = new File(directory + "/" + wavSplitFileName + files.size() + wavFormatSuffix);
                files.add(newWavFile);
                wavRecorder.setWavFile(newWavFile);

                jButton.setText("Pause");
                Thread continueThread = new Thread(wavRecorder::continueRec);

                continueThread.start();
            }

        }

        if (jButton == myJFrame.getMyContainer().getJButtonStop()){
            //stopRecording
            Thread finisherThread = new Thread(wavRecorder::finish);
            finisherThread.start();

            AudioInputStream ais = WavSoundRecorder.concatenateFiles(files);
            wavRecorder.saveStreamToFile(ais, Paths.get("target/sound/final.wav"));
            deleteWavSplitFiles();

            mp3SoundRecorder.convertWavToMp3();

            deleteFile(new File(filenameFinal + wavFormatSuffix));
        }
    }

    private void deleteWavSplitFiles() {
        File directory = new File("./target/sound");
        wavRecorder.setWavFile(null);
        files = null;
        for (File f : directory.listFiles()) {
            if (f.getName().startsWith("RecordAudio")) {
                deleteFile(f);
            }
        }
    }

    private void deleteFile(File f) {
        try {
            Files.delete(Paths.get(f.getCanonicalPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
