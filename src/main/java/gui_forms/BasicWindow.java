package gui_forms;

import javax.swing.*;

public class BasicWindow {
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
        return this.basicWindowView;
    }
}
