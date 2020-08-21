package gui.frame;

import components.AudioPlayer;
import javazoom.jl.player.Player;
import model.ImageList;
import components.MP3Enricher;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EclipseCopyFrame extends JFrame {

    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnNewMenu = new JMenu("New menu");
    private JMenuItem mntmNewMenuItem_1 = new JMenuItem("New menu item");
    private JMenuItem jMenuItemSave = new JMenuItem("Save");
    private JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
    private JMenu mnNewMenu_1 = new JMenu("New menu");
    private JPanel topLayerPanel = new JPanel();
    private JButton jButtonAddPicture = new JButton("Bild importieren");
    private JScrollPane scrollPane_1 = new JScrollPane();
    private JLabel jLabelLoadedPictures = new JLabel("Geladene Bilder");
    private JButton jButtonRemovePicture = new JButton("Bild entfernen");
    private JButton jButtonShowPicture = new JButton("Bild anzeigen");
    private JLabel jLabelImagePreview = new JLabel("");
    private JLabel jLabelPreviewText = new JLabel("Bildvorschau");
    private JPanel panel = new JPanel();
    private JLabel lblMeinempm3 = new JLabel("Keine MP3-Datei geladen");
    private JProgressBar progressBar = new JProgressBar();
    private JButton jButtonPlayStop = new JButton("Start");
    private JButton jButtonAddMP3 = new JButton("Neue MP3");
    private JLabel jLabelFrames = new JLabel("");
    private JList list_1 = new JList();
    private JButton jButtonAttachPicture = new JButton("Bild einbauen");
    private JButton jButtonMp3Cursor = new JButton("");

    private JPanel contentPane;

    private MP3Model mp3Model;
    private ImageList imageList = new ImageList();

    /**
     * Create the frame.
     */
    public EclipseCopyFrame() {
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);

        mp3Model = new MP3Model();
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
            }
        });

        // Importieren von Bilddatei
        jButtonAddPicture.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(contentPane);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                imageList.addImage(fileChooser.getSelectedFile());
                list_1.setModel(imageList);
            }
        });

        // Auswählen eines ListItems in list_1, zeigt Vorschau im jLabelImagePreview darunter
        list_1.addListSelectionListener(e -> {
            String selectedValue = (String)((JList) e.getSource()).getSelectedValue();
            if(selectedValue != null) {
                BufferedImage bi = imageList.getImageMap().get(selectedValue);
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
                BufferedImage bi = imageList.getImageMap().get(selectedValue);
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
            AudioPlayer player = new AudioPlayer(mp3Model.getMp3File().getFile().getAbsolutePath());
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(jButtonPlayStop.getText().equals("Start")) {
                        jButtonPlayStop.setText("Pause");
                        player.start();
                        System.out.println("running");
                    } else if(jButtonPlayStop.getText().equals("Pause")){
                        jButtonPlayStop.setText("Start");
                        player.stop();
                        System.out.println("stopped");
                    }

                }
            });


            t.start();
        });
    }
}