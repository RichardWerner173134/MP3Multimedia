package gui.forms;

import gui.behaviour.MyMenuActionFile;

import javax.swing.*;

public class MyJPanel {
    private JPanel basicWindowView;
    private JMenuBar myMenuBar;
    private JMenu jMenuFile;
    private JMenu jMenuEdit;
    private JMenuItem jMenuItemOpen;
    private JMenuItem jMenuItemClose;
    private JMenuItem jMenuItemSave;
    private JMenuItem jMenuItemExit;
    private JMenuItem jMenuItemSettings;

    public JPanel getJPanel(){
        return basicWindowView;
    }

    public MyJPanel() {
        jMenuItemOpen.addActionListener(new MyMenuActionFile(basicWindowView));
        jMenuItemExit.addActionListener(new MyMenuActionFile(basicWindowView));
        jMenuItemSave.addActionListener(new MyMenuActionFile(basicWindowView));
        jMenuItemClose.addActionListener(new MyMenuActionFile(basicWindowView));
    }
}
