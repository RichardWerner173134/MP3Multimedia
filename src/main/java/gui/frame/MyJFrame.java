package gui.frame;

import gui.behaviour.MyMenuActionFile;
import gui.forms.MyContainerForm;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MyJFrame extends JFrame {
    private MyContainerForm myContainer;

    private BufferedImage showedImage;
    private List<BufferedImage> images;

    public MyJFrame (){
        super("My Editor");
        initFields();

        addActionListeners();

        setContentPane(myContainer.getTopLayerJPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }

    private void initFields() {
        myContainer = new MyContainerForm();
        images = new ArrayList<>();
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("C:/Users/Richard/Desktop/emptyImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myContainer.getJLabelPreviousImage().setIcon(new ImageIcon(img));
        myContainer.getJLabelImage().setIcon(new ImageIcon(img));
        myContainer.getJLabelNextImage().setIcon(new ImageIcon(img));
    }

    public void closeJFrame(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void addActionListeners(){
        myContainer.getJMenuItemOpen().addActionListener(new MyMenuActionFile(this));
        myContainer.getJMenuItemExit().addActionListener(new MyMenuActionFile(this));
        myContainer.getJMenuItemSave().addActionListener(new MyMenuActionFile(this));
        myContainer.getJMenuItemClose().addActionListener(new MyMenuActionFile(this));
    }

    public void addImage(BufferedImage bufferedImage){
        images.add(bufferedImage);
        showImage(bufferedImage);
    }

    private void showImage(BufferedImage bufferedImage){
        showedImage = bufferedImage;
        myContainer.getJLabelImage().setIcon(new ImageIcon(showedImage));
    }


}