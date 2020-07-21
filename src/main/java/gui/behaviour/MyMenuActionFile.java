package gui.behaviour;

import gui.frame.MyJFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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
                int returnVal = jFileChooser.showOpenDialog(myJFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName());

                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    myJFrame.addImage(img);
                } else {
                    System.out.println("Open command cancelled by user.");
                }
                //imagePicker.
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
