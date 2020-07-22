package gui.behaviour;

import gui.frame.MyJFrame;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ImageClickAction implements MouseListener {

    private MyJFrame myJFrame;

    public ImageClickAction(MyJFrame myJFrame){
        this.myJFrame = myJFrame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //TODO Timestamps setzen
        //image wechseln

        JLabel jLabel = ((JLabel)(e.getSource()));
        if(jLabel == myJFrame.getMyContainer().getJLabelNextImage()){
            myJFrame.setNextToCurrentImage();
            myJFrame.updateImages();
        }
        if(jLabel == myJFrame.getMyContainer().getJLabelPreviousImage()){
            myJFrame.setPreviousToCurrentImage();
            myJFrame.updateImages();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
