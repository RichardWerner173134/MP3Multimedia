package gui.frame;

import gui.behaviour.ImageClickAction;
import gui.behaviour.MyMenuActionFile;
import gui.behaviour.RecordingButtonClickAction;
import gui.forms.MyContainerForm;
import lombok.Getter;
import lombok.Setter;
import util.Util;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MyJFrame extends JFrame {
    private MyContainerForm myContainer;

    private BufferedImage currentImage;
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

        myContainer.getJLabelImage().setSize(250,250);
        BufferedImage img = getEmptyImage();

        setImageLabel(myContainer.getJLabelPreviousImage(), img);
        setImageLabel(myContainer.getJLabelImage(), img);
        setImageLabel(myContainer.getJLabelNextImage(), img);

        myContainer.getJButtonRecord().setEnabled(true);
        myContainer.getJButtonPause().setEnabled(false);
        myContainer.getJButtonStop().setEnabled(false);
    }

    private BufferedImage getEmptyImage() {
        return Util.loadImgFromResources("emptyImage.png");
    }

    public void closeJFrame(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void addActionListeners(){
        MyMenuActionFile myMenuActionFile = new MyMenuActionFile(this);
        myContainer.getJMenuItemOpen().addActionListener(myMenuActionFile);
        myContainer.getJMenuItemExit().addActionListener(myMenuActionFile);
        myContainer.getJMenuItemSave().addActionListener(myMenuActionFile);
        myContainer.getJMenuItemClose().addActionListener(myMenuActionFile);

        ImageClickAction imageClickAction = new ImageClickAction(this);
        myContainer.getJLabelPreviousImage().addMouseListener(imageClickAction);
        myContainer.getJLabelImage().addMouseListener(imageClickAction);
        myContainer.getJLabelNextImage().addMouseListener(imageClickAction);

        RecordingButtonClickAction recordingButtonClickAction = new RecordingButtonClickAction(this);
        myContainer.getJButtonRecord().addActionListener(recordingButtonClickAction);
        myContainer.getJButtonPause().addActionListener(recordingButtonClickAction);
        myContainer.getJButtonStop().addActionListener(recordingButtonClickAction);

    }

    //adding new Image to images
    public void addImage(BufferedImage newImage){
        images.add(newImage);
        myContainer.getJLabelImageCount().setText("Anzahl Slides: " + images.size());

        // adding first image
        if(currentImage == null){
            currentImage = newImage;
        }
        // adding more images
        else {
            setNextToCurrentImage();
        }
        updateImages();
    }

    public void setNextToCurrentImage(){
        if(images.indexOf(currentImage) < images.size() - 1){
            currentImage = images.get(images.indexOf(currentImage) + 1);
        }
    }

    public void setPreviousToCurrentImage(){
        if(images.indexOf(currentImage) > 0) {
            currentImage = images.get(images.indexOf(currentImage) - 1);
        }
    }

    private void setImageLabel(JLabel label, BufferedImage bufferedImage){
        label.setIcon(new ImageIcon(bufferedImage.getScaledInstance(
                myContainer.getJLabelImage().getWidth(),
                myContainer.getJLabelImage().getHeight(),
                0)));
    }

    public void updateImages(){
        // no images: 3 times emptyImage
        if(currentImage == null){
            setImageLabel(myContainer.getJLabelPreviousImage(), getEmptyImage());
            setImageLabel(myContainer.getJLabelImage(), getEmptyImage());
            setImageLabel(myContainer.getJLabelNextImage(), getEmptyImage());
        }
        // at least 1 image: show currentImage
        else{
            setImageLabel(myContainer.getJLabelImage(), currentImage);

            if(images.indexOf(currentImage) == images.size() - 1){
                setImageLabel(myContainer.getJLabelNextImage(), getEmptyImage());
            }
            else{
                setImageLabel(myContainer.getJLabelNextImage(), images.get(images.indexOf(currentImage) + 1));
            }
            if(images.indexOf(currentImage) > 0){
                setImageLabel(myContainer.getJLabelPreviousImage(), images.get(images.indexOf(currentImage) - 1));
            }
            else{
                setImageLabel(myContainer.getJLabelPreviousImage(), getEmptyImage());
            }

        }
    }
}