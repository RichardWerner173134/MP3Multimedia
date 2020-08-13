package gui.frame;

import model.MP3Enricher;
import net.miginfocom.swing.MigLayout;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MP3DemoFrame extends JFrame {
    private JPanel topLevelPanel = new JPanel();
    private JLabel mp3Info = new JLabel("no mp3 file loaded");
    private JButton loadMp3 = new JButton("Load MP3");
    private MP3File mp3File;


    private JButton addImage = new JButton("Add Image");


    public MP3DemoFrame(){
        setContentPane(topLevelPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setVisible(true);

        initActionListeners();
        setupPanel();
    }

    private void initActionListeners() {
        loadMp3.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(topLevelPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    mp3File = (MP3File) AudioFileIO.read(fileChooser.getSelectedFile());
                } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotReadException ex) {
                    ex.printStackTrace();
                }
                mp3Info.setText(MP3Enricher.getMP3Info(mp3File));

            }
        });

        addImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(topLevelPanel);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                BufferedImage image = null;
                try {
                    image = ImageIO.read(fileChooser.getSelectedFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                createFrame(image);
                try {
                    mp3File.save();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (TagException ex) {
                    ex.printStackTrace();
                }
                mp3Info.setText(MP3Enricher.getMP3Info(mp3File));
            }
        });
    }

    private void createFrame(BufferedImage image) {
        ID3v24Tag tag = (ID3v24Tag) mp3File.getID3v2Tag();

        if(tag.frameMap.containsKey("APIC")){
            if(tag.frameMap.get("APIC") instanceof AbstractID3v2Frame){
                AbstractID3v2Frame frameOld = (AbstractID3v2Frame) tag.frameMap.get("APIC");
                tag.frameMap.remove("APIC");

                ID3v24Frame newFrame = new ID3v24Frame("APIC");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", baos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] data = baos.toByteArray();

                FrameBodyAPIC frameBodyAPIC = new FrameBodyAPIC((byte) 0,
                        "image/png",
                        (byte)0,
                        "kopie.png",
                        data);
                newFrame.setBody(frameBodyAPIC);

                ArrayList<AbstractID3v2Frame> al = new ArrayList<AbstractID3v2Frame>();
                al.add(frameOld);
                al.add(newFrame);
                tag.frameMap.put("APIC", al);
            }
            else if(tag.frameMap.get("APIC") instanceof ArrayList){
                ID3v24Frame newFrame = new ID3v24Frame("APIC");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", baos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] data = baos.toByteArray();

                FrameBodyAPIC frameBodyAPIC = new FrameBodyAPIC((byte) 0,
                        "image/png",
                        (byte)0,
                        "kopie.png",
                        data);
                newFrame.setBody(frameBodyAPIC);

                ((ArrayList)tag.frameMap.get("APIC")).add(newFrame);
            }
        }
    }



    private void setupPanel() {
        topLevelPanel.setLayout(new MigLayout());

        topLevelPanel.add(loadMp3, "wrap");
        topLevelPanel.add(addImage, "wrap");
        topLevelPanel.add(mp3Info);

    }
}
