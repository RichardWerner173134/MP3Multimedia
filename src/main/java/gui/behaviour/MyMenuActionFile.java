package gui.behaviour;

import gui.frame.MyJFrame;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.AccessibleObject;

public class MyMenuActionFile extends AbstractAction {

    private JPanel actionOwnerJPanel;

    public MyMenuActionFile(JPanel actionOwnerJPanel){
        this.actionOwnerJPanel = actionOwnerJPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "Open":
                //Open new File
                break;
            case "Save":
                //Save Opened File
                break;
            case "Close":
                //Close Opened File
                break;
            case "Exit":
                //Exits the Program
                Accessible o = this.actionOwnerJPanel.getAccessibleContext().getAccessibleParent()
                        .getAccessibleContext().getAccessibleParent()
                        .getAccessibleContext().getAccessibleParent();
                ((MyJFrame)o).closeJFrame();
                break;
        }
    }
}
