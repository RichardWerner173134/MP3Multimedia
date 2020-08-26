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
        menuBar = new JMenuBar();
        mnNewMenu = new JMenu("New menu");
        mntmNewMenuItem_1 = new JMenuItem("New menu item");
        jMenuItemSave = new JMenuItem("Save");
        mntmNewMenuItem = new JMenuItem("New menu item");
        mnNewMenu_1 = new JMenu("New menu");
        topLayerPanel = new JPanel();
        jButtonAddPicture = new JButton("Bild importieren");
        scrollPane_1 = new JScrollPane();
        jLabelLoadedPictures = new JLabel("Geladene Bilder");
        jButtonRemovePicture = new JButton("Bild entfernen");
        jButtonShowPicture = new JButton("Bild anzeigen");
        jLabelImagePreview = new JLabel("");
        jLabelPreviewText = new JLabel("Bildvorschau");
        panel = new JPanel();
        lblMeinempm3 = new JLabel("Keine MP3-Datei geladen");
        progressBar = new JProgressBar();
        jButtonPlayStop = new JButton("Start");
        jButtonAddMP3 = new JButton("Neue MP3");
        jLabelFrames = new JLabel("");
        list_1 = new JList();
        jButtonAttachPicture = new JButton("Bild einbauen");
        jButtonMp3Cursor = new JButton("");
    }
    private JMenuBar menuBar;
    private JMenu mnNewMenu;
    private JMenuItem mntmNewMenuItem_1;
    private JMenuItem jMenuItemSave;
    private JMenuItem mntmNewMenuItem;
    private JMenu mnNewMenu_1;
    private JPanel topLayerPanel;
    private JButton jButtonAddPicture;
    private JScrollPane scrollPane_1;
    private JLabel jLabelLoadedPictures;
    private JButton jButtonRemovePicture;
    private JButton jButtonShowPicture;
    private JLabel jLabelImagePreview;
    private JLabel jLabelPreviewText;
    private JPanel panel;
    private JLabel lblMeinempm3;
    private JProgressBar progressBar;
    private JButton jButtonPlayStop;
    private JButton jButtonAddMP3;
    private JLabel jLabelFrames;
    private JList list_1;
    private JButton jButtonAttachPicture;
    private JButton jButtonMp3Cursor;
    private JPanel contentPane;

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

        menuBar.add(mnNewMenu);

        mnNewMenu.add(mntmNewMenuItem_1);

        mnNewMenu.add(jMenuItemSave);

        menuBar.add(mnNewMenu_1);

        mnNewMenu_1.add(mntmNewMenuItem);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        topLayerPanel.setBounds(10, 10, 414, 463);
        contentPane.add(topLayerPanel);
        topLayerPanel.setLayout(null);

        jButtonAddPicture.setBounds(203, 7, 131, 21);
        topLayerPanel.add(jButtonAddPicture);

        scrollPane_1.setBounds(10, 10, 168, 219);
        topLayerPanel.add(scrollPane_1);

        list_1.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        scrollPane_1.setViewportView(list_1);

        scrollPane_1.setColumnHeaderView(jLabelLoadedPictures);
        scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jButtonRemovePicture.setBounds(203, 96, 131, 21);
        topLayerPanel.add(jButtonRemovePicture);

        jButtonShowPicture.setBounds(203, 50, 131, 21);
        topLayerPanel.add(jButtonShowPicture);

        jLabelImagePreview.setBounds(10, 251, 168, 106);
        topLayerPanel.add(jLabelImagePreview);

        jLabelPreviewText.setBounds(10, 369, 168, 82);
        topLayerPanel.add(jLabelPreviewText);

        panel.setBounds(474, 10, 727, 463);
        contentPane.add(panel);
        panel.setLayout(null);

        lblMeinempm3.setBounds(12, 91, 635, 19);
        lblMeinempm3.setForeground(Color.GRAY);
        lblMeinempm3.setBackground(Color.BLACK);
        panel.add(lblMeinempm3);

        progressBar.setEnabled(true);
        progressBar.setValue(0);
        progressBar.setBounds(12, 122, 635, 32);
        panel.add(progressBar);

        jButtonPlayStop.setBounds(266, 166, 104, 21);
        jButtonPlayStop.setEnabled(false);
        panel.add(jButtonPlayStop);

        jButtonAddMP3.setBounds(10, 10, 104, 21);
        panel.add(jButtonAddMP3);

        jLabelFrames.setBounds(48, 202, 148, 194);
        panel.add(jLabelFrames);

        jButtonAttachPicture.setBounds(126, 10, 104, 21);
        panel.add(jButtonAttachPicture);

        jButtonMp3Cursor.setBounds(12, 121, 8, 32);
        panel.add(jButtonMp3Cursor);
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
                lblMeinempm3.setText(mp3File.getFile().getAbsolutePath());
                progressBar.setBackground(Color.YELLOW);
                jButtonMp3Cursor.setEnabled(true);

                player = new AudioPlayer(mp3File.getFile().getAbsolutePath());
                jButtonPlayStop.setEnabled(true);
            }
        });

        // Importieren von Bilddatei
        jButtonAddPicture.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(contentPane);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                imageListModel.addImage(fileChooser.getSelectedFile());
                list_1.setModel(imageListModel);
            }
        });

        // Auswählen eines ListItems in list_1, zeigt Vorschau im jLabelImagePreview darunter
        list_1.addListSelectionListener(e -> {
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
            String selectedValue = (String)list_1.getSelectedValue();
            if(selectedValue != null) {
                BufferedImage bi = imageListModel.getImageMap().get(selectedValue);
                DialogView dialogView = new DialogView(selectedValue, mp3Model, bi);
                dialogView.setEnabled(true);
            }


        });

        // Save MP3
        jMenuItemSave.addActionListener(e -> {
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
        this.progressBar.setBackground(Color.gray);
    }
}
