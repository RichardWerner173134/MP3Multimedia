package gui.frame;

import model.ImageList;
import model.MP3Enricher;
import net.miginfocom.swing.MigLayout;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static javax.swing.ScrollPaneConstants.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class MP3DemoFrame extends JFrame {
    private JPanel topLevelPanel = new JPanel();

    private JLabel jLabelFrames = new JLabel("Keine MP3 geladen");
    private JButton jButtonLoadMp3 = new JButton("Lade MP3");
    private MP3File mp3File;

    private JButton jButtonAttachPicture = new JButton("Ausgewähltes Bild einbauen");
    private JButton jButtonAddImage = new JButton("Add Image");

    private JLabel jLabelImageList = new JLabel("Geladene Bilder");

    private JScrollPane jScrollPaneList;
    private JList jList = new JList();

    private JPanel jPanelWest = new JPanel();
    private JPanel jPanelEast = new JPanel();
    private JPanel jPanelSouth = new JPanel();

    private ImageList imageList = new ImageList();

    public MP3DemoFrame(){
        setContentPane(topLevelPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setVisible(true);

        initActionListeners();
        setupPanels();
    }

    private void initActionListeners() {
        jButtonLoadMp3.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(topLevelPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    mp3File = (MP3File) AudioFileIO.read(fileChooser.getSelectedFile());
                } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotReadException ex) {
                    ex.printStackTrace();
                }
                jLabelFrames.setText(MP3Enricher.getMP3Info(mp3File));

            }
        });

        jButtonAddImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(topLevelPanel);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                imageList.addImage(fileChooser.getSelectedFile());
                jList.setModel(imageList);
            }
        });

        jButtonAttachPicture.addActionListener(e -> {

            //MP3Enricher.addPicure(mp3File, fileChooser.getSelectedFile(), 200);
            jLabelFrames.setText(MP3Enricher.getMP3Info(mp3File));

        });
    }


    private void setupPanels() {
        topLevelPanel.setLayout(new MigLayout());
        jPanelWest.setLayout(new MigLayout());
        jPanelEast.setLayout(new MigLayout());
        jPanelSouth.setLayout(new MigLayout());

        // setup jList/ Scrollpane
        jList.setModel(new ImageList());
        jScrollPaneList = new JScrollPane(jList,
                VERTICAL_SCROLLBAR_AS_NEEDED,
                HORIZONTAL_SCROLLBAR_NEVER);

        //West
        jPanelWest.add(jButtonAddImage);
        jPanelWest.add(jScrollPaneList);

        //East
        jPanelEast.add(jButtonLoadMp3);
        jPanelEast.add(jLabelFrames);

        //South

        //topLevelPanel.add(jButtonAttachPicture, "wrap");

        topLevelPanel.add(jPanelWest);
        topLevelPanel.add(jPanelEast);
        topLevelPanel.add(jPanelSouth);
    }
}
