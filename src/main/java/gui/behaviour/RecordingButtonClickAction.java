package gui.behaviour;

import gui.component.MP3SoundRecorder;
import gui.component.WavSoundRecorder;
import gui.frame.MyJFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecordingButtonClickAction extends AbstractAction {
    private MyJFrame myJFrame;

    final WavSoundRecorder recorder = new WavSoundRecorder();
    private String filename = "./target/sound/RecordAudio";
    private String fileFormat = ".wav";

    final String source = "./target/sound/RecordAudio.wav";
    final String target = "./target/sound/RecordAudio.mp3";

    private List<File> files;

    public RecordingButtonClickAction(MyJFrame myJFrame){
        this.myJFrame = myJFrame;
        files = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = ((JButton)e.getSource());

        //startRecording
        if (jButton == myJFrame.getMyContainer().getJButtonRecord()){
            File newWavFile = new File(filename + files.size() + fileFormat);
            files.add(newWavFile);
            recorder.setWavFile(newWavFile);

            Thread starterThread = new Thread(recorder::start);

            starterThread.start();
        }

        if (jButton == myJFrame.getMyContainer().getJButtonPause()){
            //pauseRecording
            if(jButton.getText().equals("Pause")){
                jButton.setText("Continue");
                Thread pauseThread = new Thread(recorder::pause);

                pauseThread.start();
            }
            //continueRec
            else{
                File newWavFile = new File(filename + files.size() + fileFormat);
                files.add(newWavFile);
                recorder.setWavFile(newWavFile);

                jButton.setText("Pause");
                Thread continueThread = new Thread(recorder::continueRec);

                continueThread.start();
            }

        }

        if (jButton == myJFrame.getMyContainer().getJButtonStop()){
            //stopRecording
            Thread finisherThread = new Thread(recorder::finish);

            finisherThread.start();
            //TODO wavs zusammenfügen
        }
    }
}
