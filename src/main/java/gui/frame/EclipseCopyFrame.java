package gui.frame;

import components.AudioPlayer;
import components.MP3Enricher;
import model.ImageListModel;
import model.MP3Model;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EclipseCopyFrame extends JFrame {

    private void initComponents() {
        //Menu
        menuBar = new JMenuBar();

            // Menu File
            jMenuFile = new JMenu("File");
                jMenuFileItem1 = new JMenuItem("New menu item");
                jMenuFileItem2 = new JMenuItem("New menu item");
                jMenuFileItemSave = new JMenuItem("Save");
            jMenu2 = new JMenu("New menu");

        // Left Component of Frame
        jPanelWest = new JPanel();

            // ImageList
            scrollPaneImages = new JScrollPane();
            jLabelLoadedPictures = new JLabel("Geladene Bilder");
            imageList = new JList();

            // Buttons
            jButtonImportImage = new JButton("Bild importieren");
            jButtonShowPicture = new JButton("Bild anzeigen");
            jButtonRemovePicture = new JButton("Bild entfernen");

            // ImagePreview
            jLabelImagePreview = new JLabel("");
            jLabelPreviewText = new JLabel("Bildvorschau");

        // right Component of Frame
        jPanelEast = new JPanel();
            // Buttons
            jButtonAddMP3 = new JButton("Neue MP3");
            jButtonAttachPicture = new JButton("Bild einbauen");

            // Player
            jLabelMP3Name = new JLabel("Keine MP3-Datei geladen");
            jProgressBarMP3Bar = new JProgressBar();
            jButtonMp3Cursor = new JButton("");
            jButtonPlayStop = new JButton("Start");

        // displaying Frames, for testing purposes
        jLabelFrames = new JLabel("");
    }
    private JMenuBar        menuBar;
    private JMenu           jMenuFile;
    private JMenuItem       jMenuFileItem1;
    private JMenuItem       jMenuFileItemSave;
    private JMenuItem       jMenuFileItem2;
    private JMenu           jMenu2;
    private JPanel          jPanelWest;
    private JButton         jButtonImportImage;
    private JScrollPane     scrollPaneImages;
    private JLabel          jLabelLoadedPictures;
    private JButton         jButtonRemovePicture;
    private JButton         jButtonShowPicture;
    private JLabel          jLabelImagePreview;
    private JLabel          jLabelPreviewText;
    private JPanel          jPanelEast;
    private JLabel          jLabelMP3Name;
    private JProgressBar    jProgressBarMP3Bar;
    private JButton         jButtonPlayStop;
    private JButton         jButtonAddMP3;
    private JLabel          jLabelFrames;
    private JList           imageList;
    private JButton         jButtonAttachPicture;
    private JButton         jButtonMp3Cursor;
    private JPanel          contentPane;

    private MP3Model mp3Model;
    private ImageListModel imageListModel = new ImageListModel();

    private AudioPlayer player;


    /**
     * Create the frame.
     */
    public EclipseCopyFrame() {
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);

        mp3Model = new MP3Model();

        initComponents();
        initPanel();

        addActionListeners();
    }


    private void initPanel() {
        setJMenuBar(menuBar);

        menuBar.add(jMenuFile);

        jMenuFile.add(jMenuFileItem1);

        jMenuFile.add(jMenuFileItemSave);

        menuBar.add(jMenu2);

        jMenu2.add(jMenuFileItem2);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        jPanelWest.setBounds(10, 10, 414, 463);
        contentPane.add(jPanelWest);
        jPanelWest.setLayout(null);

        jButtonImportImage.setBounds(203, 7, 131, 21);
        jPanelWest.add(jButtonImportImage);

        scrollPaneImages.setBounds(10, 10, 168, 219);
        jPanelWest.add(scrollPaneImages);

        imageList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        scrollPaneImages.setViewportView(imageList);

        scrollPaneImages.setColumnHeaderView(jLabelLoadedPictures);
        scrollPaneImages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jButtonRemovePicture.setBounds(203, 96, 131, 21);
        jPanelWest.add(jButtonRemovePicture);

        jButtonShowPicture.setBounds(203, 50, 131, 21);
        jPanelWest.add(jButtonShowPicture);

        jLabelImagePreview.setBounds(10, 251, 168, 106);
        jPanelWest.add(jLabelImagePreview);

        jLabelPreviewText.setBounds(10, 369, 168, 82);
        jPanelWest.add(jLabelPreviewText);

        jPanelEast.setBounds(474, 10, 727, 463);
        contentPane.add(jPanelEast);
        jPanelEast.setLayout(null);

        jLabelMP3Name.setBounds(12, 91, 635, 19);
        jLabelMP3Name.setForeground(Color.GRAY);
        jLabelMP3Name.setBackground(Color.BLACK);
        jPanelEast.add(jLabelMP3Name);

        jProgressBarMP3Bar.setEnabled(true);
        jProgressBarMP3Bar.setValue(0);
        jProgressBarMP3Bar.setBounds(12, 122, 635, 32);
        jPanelEast.add(jProgressBarMP3Bar);

        jButtonPlayStop.setBounds(266, 166, 104, 21);
        jButtonPlayStop.setEnabled(false);
        jPanelEast.add(jButtonPlayStop);

        jButtonAddMP3.setBounds(10, 10, 104, 21);
        jPanelEast.add(jButtonAddMP3);

        jLabelFrames.setBounds(48, 202, 148, 194);
        jPanelEast.add(jLabelFrames);

        jButtonAttachPicture.setBounds(126, 10, 104, 21);
        jPanelEast.add(jButtonAttachPicture);

        jButtonMp3Cursor.setBounds(12, 121, 8, 32);
        jPanelEast.add(jButtonMp3Cursor);
        jButtonMp3Cursor.setEnabled(false);
        jButtonMp3Cursor.setForeground(Color.BLACK);
    }

    private void addActionListeners() {
        // Hinzufügen einer MP3 über FileChooser
        jButtonAddMP3.addActionListener(e -> {
            MP3File mp3File = null;
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(contentPane);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    mp3File = (MP3File) AudioFileIO.read(fileChooser.getSelectedFile());
                } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotReadException ex) {
                    ex.printStackTrace();
                }
                jLabelFrames.setText(MP3Enricher.getMP3Info(mp3File));
                mp3Model.setMp3File(mp3File);
                jLabelMP3Name.setText(mp3File.getFile().getAbsolutePath());
                jProgressBarMP3Bar.setBackground(Color.YELLOW);
                jButtonMp3Cursor.setEnabled(true);

                player = new AudioPlayer(mp3File.getFile().getAbsolutePath());
                jButtonPlayStop.setEnabled(true);
            }
        });

        // Importieren von Bilddatei
        jButtonImportImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(contentPane);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                imageListModel.addImage(fileChooser.getSelectedFile());
                imageList.setModel(imageListModel);
            }
        });

        // Auswählen eines ListItems in list_1, zeigt Vorschau im jLabelImagePreview darunter
        imageList.addListSelectionListener(e -> {
            String selectedValue = (String)((JList) e.getSource()).getSelectedValue();
            if(selectedValue != null) {
                BufferedImage bi = imageListModel.getImageMap().get(selectedValue);
                ImageIcon icon = new ImageIcon(bi.getScaledInstance(jLabelImagePreview.getWidth(), jLabelImagePreview.getHeight(),
                        Image.SCALE_SMOOTH));
                jLabelImagePreview.setIcon(icon);
                jLabelPreviewText.setText("<html><body>" + selectedValue + "<br/> size: " + bi.getHeight() + " x " + bi.getWidth() + "</body></html>");
            }
            else{
                jLabelImagePreview.setIcon(null);
            }
        });



        // Löst Dialog aus, wenn button betätigt wird und ein Bild ausgewählt ist
        jButtonAttachPicture.addActionListener(e -> {
            String selectedValue = (String) imageList.getSelectedValue();
            if(selectedValue != null) {
                BufferedImage bi = imageListModel.getImageMap().get(selectedValue);
                DialogView dialogView = new DialogView(selectedValue, mp3Model, bi);
                dialogView.setEnabled(true);
            }


        });

        // Save MP3
        jMenuFileItemSave.addActionListener(e -> {
            MP3Enricher.attachAll(mp3Model);
            jLabelFrames.setText(MP3Enricher.getMP3Info(mp3Model.getMp3File()));
        });

        // start/stop player
        jButtonPlayStop.addActionListener(e -> {
            if(player != null){
                if(jButtonPlayStop.getText().equals("Start")){
                    player.play(mp3Model.getMp3File().getFile().getAbsolutePath());
                    jButtonPlayStop.setText("Stop");
                } else if(jButtonPlayStop.getText().equals("Stop")){
                    player.stop();
                    jButtonPlayStop.setText("Start");
                }
            }
        });
    }


    private void resetUI(){
        this.imageListModel.setImageMap(null);

        this.player = null;
        this.jButtonPlayStop.setEnabled(false);
        this.jButtonMp3Cursor.setEnabled(false);

        this.mp3Model = null;
        this.jProgressBarMP3Bar.setBackground(Color.gray);
    }
}
