package gui.frame;

import gui.forms.MyJPanel;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class MyJFrame extends JFrame {
    public MyJFrame (){
        super("My Editor");

        JPanel myJPanel = new MyJPanel().getJPanel();

        setContentPane(myJPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void closeJFrame(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}