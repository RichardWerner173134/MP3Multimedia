package gui.behaviour;

import gui.component.MP3SoundRecorder;
import gui.component.WavSoundRecorder;
import gui.frame.MyJFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecordingButtonClickAction extends AbstractAction {
    private MyJFrame myJFrame;

    WavSoundRecorder recorder = new WavSoundRecorder();
    private String filename = "target/sound/RecordAudio";
    private String fileFormat = ".wav";

    private List<File> files;

    public RecordingButtonClickAction(MyJFrame myJFrame){
        this.myJFrame = myJFrame;
        files = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = ((JButton)e.getSource());
        toggleButtons(jButton);

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

            WavSoundRecorder.concatenateAllWAVFiles(files);

            List<Path> filePaths = files.stream()
                    .map(file -> Paths.get(file.getAbsolutePath()))
                    .collect(Collectors.toList());

            recorder = null;
            files = null;

            deleteFiles(filePaths);
        }
    }

    private void deleteFiles(List<Path> filePaths){
        for(Path p : filePaths){
            try {
                Files.delete(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleButtons(JButton buttonPressed){
        if(buttonPressed == myJFrame.getMyContainer().getJButtonRecord()){
            myJFrame.getMyContainer().getJButtonRecord().setEnabled(false);
            myJFrame.getMyContainer().getJButtonPause().setEnabled(true);
            myJFrame.getMyContainer().getJButtonStop().setEnabled(false);
        }
        if(buttonPressed == myJFrame.getMyContainer().getJButtonPause()){
            myJFrame.getMyContainer().getJButtonRecord().setEnabled(false);
            myJFrame.getMyContainer().getJButtonPause().setEnabled(true);
            if(buttonPressed.getText().equals("Pause")){
                myJFrame.getMyContainer().getJButtonStop().setEnabled(true);
            }
            else{
                myJFrame.getMyContainer().getJButtonStop().setEnabled(false);
            }
        }
        if(buttonPressed == myJFrame.getMyContainer().getJButtonStop()){
            myJFrame.getMyContainer().getJButtonRecord().setEnabled(true);
            myJFrame.getMyContainer().getJButtonPause().setEnabled(false);
            myJFrame.getMyContainer().getJButtonStop().setEnabled(false);
        }
    }
}
