package gui.behaviour;

import exceptions.FileSortingException;
import gui.component.MP3SoundRecorder;
import gui.frame.MyJFrame;
import util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class MyMenuActionFile extends AbstractAction {

    private MyJFrame myJFrame;

    public MyMenuActionFile(MyJFrame myJFrame){
        this.myJFrame = myJFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "Open":
                //Open new File
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setMultiSelectionEnabled(true);

                int returnVal = jFileChooser.showOpenDialog(myJFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = jFileChooser.getSelectedFiles();
                    List<File> fileList = Arrays.asList(files);

                    try{
                        fileList.sort((o1, o2) -> {
                            String s1 = o1.getName().replace("Folie", "").replace(".PNG","")
                                    .replace(".JPG", "");
                            String s2 = o2.getName().replace("Folie", "").replace(".PNG","")
                                    .replace(".JPG","");
                            return (Integer.parseInt(s1) < Integer.parseInt(s2)) ? -1 : 1;
                        });
                    } catch (FileSortingException ex){
                        ex.printStackTrace();
                    }
                    //This is where a real application would open the file.
                    for(File file : fileList){
                        System.out.println("Opening: " + file.getName());
                        BufferedImage img = Util.loadImg(file.getPath());

                        myJFrame.addImage(img);
                    }
                } else {
                    System.out.println("Open command cancelled by user.");
                }
                break;
            case "Save":
                //Save Opened File
                break;
            case "Close":
                //Close Opened File
                break;
            case "Exit":
                //Exits the Program
                myJFrame.closeJFrame();
                break;
        }
    }
}
