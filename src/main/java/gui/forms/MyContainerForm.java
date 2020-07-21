package gui.forms;

import gui.behaviour.MyMenuActionFile;
import gui.frame.MyJFrame;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MyContainerForm extends Container {
    private JPanel topLayerJPanel;
    private JMenuBar myMenuBar;
    private JMenu jMenuFile;
    private JMenu jMenuEdit;
    private JMenuItem jMenuItemOpen;
    private JMenuItem jMenuItemClose;
    private JMenuItem jMenuItemSave;
    private JMenuItem jMenuItemExit;
    private JMenuItem jMenuItemSettings;
    private JLabel jLabelImage;
}
