package gui.frame;

import components.AudioPlayer;
import components.MP3Enricher;
import model.TimeStampModel;
import model.ImageListModel;
import model.ImageModel;
import model.MP3Model;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import util.IOUtil;
import util.Other;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class EditorFrame extends JFrame {

    private JMenuBar        jMenuBar;
    private JMenu           jMenuFile;
    private JMenuItem       jMenuResetWorkspace;
    private JMenuItem       jMenuFileItemSave;
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
    private PlayerBar       playerBar;
    private JButton         jButtonPlayPause;
    private JButton         jButtonReset;
    private JButton         jButtonAddMP3;
    private JLabel          jLabelFrames;
    private JList           imageList;
    private JButton         jButtonAttachPicture;
    private JPanel          contentPane;
    private JPanel          jPanelAttachedPictures;
    private MP3Model        mp3Model;
    private ImageListModel  imageListModel;
    private JPanel          jPanelEdit;
    private JTextField      jTextFieldImageName;
    private JTextField      jTextFieldStartTimeM;
    private JTextField      jTextFieldStartTimeS;
    private JTextField      jTextFieldStartTimeMS;
    private JButton         jButtonShowImg;
    private JLabel          jLabelUnit;
    private JLabel          jLabelInfo;
    private JButton         jButtonEdit;
    private JButton         jButtonRemove;
    private Border          blackBorder;
    private Border          redBorder;

    private AudioPlayer     player;
    private HashMap<String, AttachedImage> attachedPictures;
    private int []          currentTimeStamp;

    /**
     * Create the frame.
     */
    public EditorFrame() {
        setVisible(true);
        setResizable(false);
        setTitle("MP3-Multimedia Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);

        mp3Model = new MP3Model();
        imageListModel = new ImageListModel();
        attachedPictures = new HashMap<>();
        currentTimeStamp = new int[3];

        initComponents();
        initPanel();

        addActionListeners();
    }


    private void initPanel() {
        setJMenuBar(jMenuBar);

        jMenuBar.add(jMenuFile);

        jMenuFile.add(jMenuResetWorkspace);

        jMenuFile.add(jMenuFileItemSave);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        jPanelWest.setBounds(10, 10, 414, 463);
        contentPane.add(jPanelWest);
        jPanelWest.setLayout(null);

        jButtonImportImage.setBounds(220, 7, 131, 21);
        jPanelWest.add(jButtonImportImage);

        scrollPaneImages.setBounds(10, 10, 168, 219);
        jPanelWest.add(scrollPaneImages);

        imageList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        scrollPaneImages.setViewportView(imageList);

        scrollPaneImages.setColumnHeaderView(jLabelLoadedPictures);
        scrollPaneImages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jButtonRemovePicture.setBounds(220, 99, 131, 21);
        jButtonRemovePicture.setEnabled(false);
        jPanelWest.add(jButtonRemovePicture);

        jButtonShowPicture.setBounds(220, 52, 131, 21);
        jButtonShowPicture.setEnabled(false);
        jPanelWest.add(jButtonShowPicture);

        jLabelImagePreview.setBounds(10, 251, 168, 106);
        jPanelWest.add(jLabelImagePreview);
        ImageIcon imageIcon = new ImageIcon(IOUtil.loadImgFromResources("img/blank.png").getScaledInstance(
                jLabelImagePreview.getWidth(), jLabelImagePreview.getHeight(), Image.SCALE_SMOOTH));
        jLabelImagePreview.setIcon(imageIcon);

        jLabelPreviewText.setBounds(10, 369, 168, 82);
        jPanelWest.add(jLabelPreviewText);

        jPanelEast.setBounds(474, 10, 727, 463);
        contentPane.add(jPanelEast);
        jPanelEast.setLayout(null);

        jLabelMP3Name.setBounds(48, 90, 400, 19);
        jLabelMP3Name.setForeground(Color.GRAY);
        jLabelMP3Name.setBackground(Color.BLACK);
        jPanelEast.add(jLabelMP3Name);

        playerBar.setBounds(48, 160, 635, 32);
        jPanelEast.add(playerBar);

        jButtonPlayPause.setBounds(48, 129, 85, 21);
        jButtonPlayPause.setEnabled(false);
        jPanelEast.add(jButtonPlayPause);

        jButtonReset.setBounds(143, 129, 85, 21);
        jButtonReset.setEnabled(false);
        jPanelEast.add(jButtonReset);

        jButtonAddMP3.setBounds(48, 10, 104, 21);
        jPanelEast.add(jButtonAddMP3);

        jLabelFrames.setBounds(48, 202, 148, 194);
        jPanelEast.add(jLabelFrames);

        jButtonAttachPicture.setBounds(220, 169, 131, 61);
        jPanelWest.add(jButtonAttachPicture);
        jButtonAttachPicture.setEnabled(false);

        jPanelAttachedPictures.setBounds(playerBar.getX(), playerBar.getY() + 50, playerBar.getWidth(), 50);
        jPanelAttachedPictures.setLayout(null);
        jPanelEast.add(jPanelAttachedPictures);

        jPanelEdit = new JPanel();
        jPanelEdit.setBounds(48, 290, 485, 100);
        jPanelEast.add(jPanelEdit);
        jPanelEdit.setLayout(null);
        jPanelEdit.setVisible(false);

        JLabel jLabelImageName = new JLabel("Bilddatei");
        jLabelImageName.setBounds(10, 10, 90, 13);
        jPanelEdit.add(jLabelImageName);

        JLabel jLabelStarttime = new JLabel("Startzeit");
        jLabelStarttime.setBounds(10, 44, 90, 13);
        jPanelEdit.add(jLabelStarttime);

        jTextFieldImageName = new JTextField();
        jTextFieldImageName.setEditable(false);
        jTextFieldImageName.setBounds(132, 7, 155, 19);
        jPanelEdit.add(jTextFieldImageName);
        jTextFieldImageName.setColumns(10);

        jTextFieldStartTimeM = new JTextField();
        jTextFieldStartTimeM.setBounds(132, 41, 45, 19);
        jPanelEdit.add(jTextFieldStartTimeM);
        jTextFieldStartTimeM.setColumns(10);

        jTextFieldStartTimeS = new JTextField();
        jTextFieldStartTimeS.setColumns(10);
        jTextFieldStartTimeS.setBounds(187, 41, 45, 19);
        jPanelEdit.add(jTextFieldStartTimeS);

        jTextFieldStartTimeMS = new JTextField();
        jTextFieldStartTimeMS.setColumns(10);
        jTextFieldStartTimeMS.setBounds(242, 41, 45, 19);
        jPanelEdit.add(jTextFieldStartTimeMS);

        jButtonShowImg = new JButton("Bild anzeigen");
        jButtonShowImg.setBounds(309, 74, 113, 21);
        jPanelEdit.add(jButtonShowImg);

        jLabelInfo = new JLabel();
        jLabelInfo.setBounds(132, 60, 300, 20);
        jLabelInfo.setText("");
        jPanelEdit.add(jLabelInfo);

        jButtonEdit = new JButton("Aktualisieren");
        jButtonEdit.setBounds(309, 40, 113, 21);
        jPanelEdit.add(jButtonEdit);

        jButtonRemove = new JButton("Entfernen");
        jButtonRemove.setBounds(309, 6, 113, 21);
        jPanelEdit.add(jButtonRemove);

        jLabelUnit = new JLabel("min            sek            ms");
        jLabelUnit.setBounds(138,60,150,20);
        jPanelEdit.add(jLabelUnit);

    }

    private void addActionListeners() {
        // Hinzufügen einer MP3 über FileChooser
        jButtonAddMP3.addActionListener(e -> {
            MP3File mp3File = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileFilter mp3Filter = new FileNameExtensionFilter(
                    "MP3", "mp3");
            fileChooser.setFileFilter(mp3Filter);
            int returnVal = fileChooser.showOpenDialog(contentPane);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    mp3File = (MP3File) AudioFileIO.read(fileChooser.getSelectedFile());
                } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotReadException ex) {
                    ex.printStackTrace();
                }
                mp3Model.setMp3FileAndLoad(mp3File);
                jLabelMP3Name.setText(mp3File.getFile().getAbsolutePath());
                playerBar.displayMP3(mp3File.getMP3AudioHeader().getTrackLength());

                initExistingAttachedPictures();

                player = new AudioPlayer(mp3Model.getMp3File().getFile().getAbsolutePath());
                jButtonPlayPause.setEnabled(true);
                if(imageList.getSelectedValue() != null){
                    jButtonAttachPicture.setEnabled(true);
                }
            }
        });

        // Importieren von Bilddatei
        jButtonImportImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileFilter imageFilter = new FileNameExtensionFilter(
                    "Image files", "jpg", "png");
            fileChooser.setFileFilter(imageFilter);
            int returnVal = fileChooser.showOpenDialog(contentPane);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File[] selectedFiles = fileChooser.getSelectedFiles();
                for(File f : selectedFiles){
                    imageListModel.addImage(f);
                }
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
                if(mp3Model.getMp3File() != null){
                    jButtonAttachPicture.setEnabled(true);
                }
                jButtonShowPicture.setEnabled(true);
            }
            else{
                ImageIcon imageIcon = new ImageIcon(
                        IOUtil.loadImgFromResources("img/blank.png")
                                .getScaledInstance(jLabelImagePreview.getWidth(), jLabelImagePreview.getHeight(), Image.SCALE_SMOOTH));
                jLabelImagePreview.setIcon(imageIcon);
            }
        });



        // Löst Dialog aus, wenn button betätigt wird und ein Bild ausgewählt ist
        jButtonAttachPicture.addActionListener(e -> {
            String selectedValue = (String) imageList.getSelectedValue();
            if(player != null){
                if(jButtonPlayPause.getText().equals("Pause")) {
                    player.pause(playerBar);
                    currentTimeStamp = player.getTimeStampPosition(mp3Model.getMp3File().getAudioHeader().getTrackLength());
                    jButtonPlayPause.setText("Start");
                } else{
                    currentTimeStamp = player.getTimeStampPosition(0);
                }
            }

            if(selectedValue != null) {
                BufferedImage bi = imageListModel.getImageMap().get(selectedValue);
                DialogAttachImage dialogAttachImage = new DialogAttachImage(selectedValue, mp3Model, bi, attachedPictures,
                        currentTimeStamp, jPanelEdit, jTextFieldImageName, jTextFieldStartTimeM, jTextFieldStartTimeS, jTextFieldStartTimeMS);
                dialogAttachImage.setEnabled(true);
                dialogAttachImage.setAlwaysOnTop(true);
                dialogAttachImage.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        repaint();
                    }
                });
            }


        });

        // Save MP3
        jMenuFileItemSave.addActionListener(e -> {
            if(player != null){
                player.pause(playerBar);
            }
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(contentPane);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                if(saveFile != null){
                    if(MP3Enricher.attachAll(mp3Model, saveFile)){
                        JOptionPane.showMessageDialog(this, "Speichern erfolgreich");
                    } else {
                        JOptionPane.showMessageDialog(this, "Speichern fehlgeschlagen");
                    }
                }
            }
        });

        // start/pause player
        jButtonPlayPause.addActionListener(e -> {
            if (player == null) {
                player = new AudioPlayer(mp3Model.getMp3File().getFile().getAbsolutePath());
                player.setPath(mp3Model.getMp3File().getFile().getAbsolutePath());
            }
            jButtonReset.setEnabled(true);
            if(jButtonPlayPause.getText().equals("Start")){
                player.resume(playerBar, mp3Model.getMp3File());
                jPanelEast.revalidate();
                jButtonPlayPause.setText("Pause");
            } else if(jButtonPlayPause.getText().equals("Pause")){
                player.pause(playerBar);
                currentTimeStamp = player.getTimeStampPosition(mp3Model.getMp3File().getAudioHeader().getTrackLength());
                jButtonPlayPause.setText("Start");
            }
        });

        // input validation for editPanel
        jTextFieldStartTimeM.addKeyListener(Other.getNewAdapter(jTextFieldStartTimeM, jLabelInfo, jButtonEdit, jTextFieldStartTimeS, jTextFieldStartTimeMS));
        jTextFieldStartTimeS.addKeyListener(Other.getNewAdapter(jTextFieldStartTimeS, jLabelInfo, jButtonEdit, jTextFieldStartTimeM, jTextFieldStartTimeMS));
        jTextFieldStartTimeMS.addKeyListener(Other.getNewAdapter(jTextFieldStartTimeMS, jLabelInfo, jButtonEdit, jTextFieldStartTimeM, jTextFieldStartTimeS));


        // edit AttachedPicture
        jButtonEdit.addActionListener(e -> {
            int millis = Other.timeInMilliSeconds(
                    Integer.parseInt(jTextFieldStartTimeM.getText()),
                    Integer.parseInt(jTextFieldStartTimeS.getText()),
                    Integer.parseInt(jTextFieldStartTimeMS.getText()));

            String attachedPictureKey = this.attachedPictures.entrySet().stream()
                    .filter(x->x.getValue().isSelected())
                    .map(x->x.getValue().getImageTitle())
                    .findFirst().get();

            BufferedImage bufferedImage = mp3Model.getImageModelMap().get(attachedPictureKey).getBufferedImage();
            String oldKeyInAttachedPictures = this.attachedPictures.entrySet().stream().filter(f -> f.getValue().isSelected()).findFirst().get().getKey();

            // remove
            {
                // remove from imageModelMap
                ImageModel toBeRemovedTimeStamp = this.mp3Model.getImageModelMap().get(attachedPictureKey);
                if (toBeRemovedTimeStamp != null) {
                    int starttimeMillis = attachedPictures.entrySet().stream()
                            .map(Map.Entry::getValue)
                            .filter(AttachedImage::isSelected)
                            .findFirst()
                            .get()
                            .getStarttimeMillis();
                    toBeRemovedTimeStamp.getTimeStampModelMap().remove(String.valueOf(starttimeMillis));
                }

                // remove Button
                this.jPanelAttachedPictures.remove(
                        Arrays.asList(this.jPanelAttachedPictures.getComponents())
                                .stream()
                                .filter(s -> ((AttachedImage) s).isSelected())
                                .findFirst()
                                .get());

                // remove from List

                this.attachedPictures.remove(oldKeyInAttachedPictures);
            }

            // add new
            {
                AttachedImage attachedImage = new AttachedImage(attachedPictureKey, millis);

                attachedImage.addActionListener(e2 -> {
                    if(attachedImage.isSelected()){
                        attachedImage.setSelected(false);
                        attachedImage.setBorder(blackBorder);
                        jPanelEdit.setVisible(false);
                    } else{
                        attachedImage.setSelected(true);
                        attachedImage.setBorder(redBorder);
                        jTextFieldImageName.setText(attachedImage.getImageTitle());
                        jPanelEdit.setVisible(true);
                    }
                    for(AttachedImage a : attachedPictures.values().stream().filter(ai -> ai != attachedImage).collect(Collectors.toList())){
                        a.setSelected(false);
                        a.setBorder(blackBorder);
                    }
                });

                mp3Model.addImage(attachedPictureKey,
                        bufferedImage,
                        millis);

                attachedPictures.put(oldKeyInAttachedPictures, attachedImage);
                attachedImage.setBorder(redBorder);
                jTextFieldStartTimeM.setText(Other.getMinutesForMillis(attachedImage.getStarttimeMillis()));
                jTextFieldStartTimeS.setText(Other.getSecondsForMillis(attachedImage.getStarttimeMillis()));
                jTextFieldStartTimeMS.setText(Other.getMilliSecondsForMillis(attachedImage.getStarttimeMillis()));

                // stays selected
                attachedImage.setSelected(true);
            }

            repaint();
        });

        // remove AttachedPicture
        jButtonRemove.addActionListener(e -> {
            String attachedPictureKey = this.attachedPictures.entrySet().stream()
                    .filter(x->x.getValue().isSelected())
                    .map(x->x.getValue().getImageTitle())
                    .findFirst().get();

            // remove from imageModelMap
            ImageModel toBeRemovedTimeStamp = this.mp3Model.getImageModelMap().get(attachedPictureKey);
            if(toBeRemovedTimeStamp != null){
                int starttimeMillis = attachedPictures.entrySet().stream()
                        .map(Map.Entry::getValue)
                        .filter(AttachedImage::isSelected)
                        .findFirst()
                        .get()
                        .getStarttimeMillis();
                toBeRemovedTimeStamp.getTimeStampModelMap().remove(String.valueOf(starttimeMillis));
                if(toBeRemovedTimeStamp.getTimeStampModelMap().size() == 0){
                    this.mp3Model.getImageModelMap().remove(attachedPictureKey);
                }
            }

            // remove Button
            this.jPanelAttachedPictures.remove(
                    Arrays.asList(this.jPanelAttachedPictures.getComponents())
                            .stream()
                            .filter(s->((AttachedImage)s).isSelected())
                            .findFirst()
                            .get());

            // remove from List

            this.attachedPictures.remove(this.attachedPictures.entrySet().stream().filter(f->f.getValue().isSelected()).findFirst().get().getKey());

            this.jTextFieldImageName.setText("");
            this.jTextFieldStartTimeM.setText("");
            this.jTextFieldStartTimeS.setText("");
            this.jTextFieldStartTimeMS.setText("");
            this.jPanelEdit.setVisible(false);

            repaint();
        });

        // reset/ Stop player
        jButtonReset.addActionListener(e -> {
            if(player != null){
                player.stop(playerBar);
            }
            jButtonPlayPause.setText("Start");
            jButtonReset.setEnabled(false);
        });

        // Reset UI
        jMenuResetWorkspace.addActionListener(e -> {
            resetUI();
        });

        // Picture Preview
        jButtonShowPicture.addActionListener(e -> {
            String selectedValue = (String) ((JList) imageList).getSelectedValue();
            BufferedImage bi = imageListModel.getImageMap().get(selectedValue);
            Other.imagePreview(bi);
        });

        // Picture Preview Edit Panel
        jButtonShowImg.addActionListener(e -> {
            String imgTitle = attachedPictures.entrySet().stream()
                    .filter(x->x.getValue().isSelected())
                    .map(x->x.getValue().getImageTitle())
                    .findFirst()
                    .get();
            BufferedImage bi = mp3Model.getImageModelMap().get(imgTitle).getBufferedImage();
            Other.imagePreview(bi);
        });
    }

    private void initExistingAttachedPictures() {
        Iterator it = mp3Model.getImageModelMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry imageModelEntry = (Map.Entry) it.next();
            Iterator it2 = ((ImageModel)imageModelEntry.getValue()).getTimeStampModelMap().entrySet().iterator();
            while(it2.hasNext()){
                Map.Entry timeStampModelEntry = (Map.Entry) it2.next();
                AttachedImage attachedImage = new AttachedImage((String) imageModelEntry.getKey(),
                        ((TimeStampModel)timeStampModelEntry.getValue()).getStarttime());
                attachedImage.addActionListener(e -> {
                    if(attachedImage.isSelected()){
                        attachedImage.setSelected(false);
                        attachedImage.setBorder(blackBorder);
                        jPanelEdit.setVisible(false);
                    } else{
                        attachedImage.setSelected(true);
                        attachedImage.setBorder(redBorder);
                        jTextFieldImageName.setText(attachedImage.getImageTitle());
                        jPanelEdit.setVisible(true);

                        String formattedTimeForAttachedImage = Other.getFormattedTimeForAttachedImage(attachedImage);
                        jTextFieldStartTimeM.setText(Other.getMinutesForMillis(attachedImage.getStarttimeMillis()));
                        jTextFieldStartTimeS.setText(Other.getSecondsForMillis(attachedImage.getStarttimeMillis()));
                        jTextFieldStartTimeMS.setText(Other.getMilliSecondsForMillis(attachedImage.getStarttimeMillis()));
                    }
                    for(AttachedImage a : attachedPictures.values().stream().filter(ai -> ai != attachedImage).collect(Collectors.toList())){
                        a.setSelected(false);
                        a.setBorder(blackBorder);
                    }
                });
                attachedPictures.put(attachedPictures.size() + "", attachedImage);
            }

        }
        repaint();
    }

    @Override
    public void paint(Graphics g){
        if(attachedPictures.size() == 0){
            jPanelAttachedPictures.revalidate();
            super.paint(g);
            return;
        }

        int totalWidth = jPanelAttachedPictures.getWidth();

        double tracklengthMillis = mp3Model.getMp3File().getAudioHeader().getTrackLength() * 1000;

        // Iterate through Collection of all AttachedImages
        Iterator it = attachedPictures.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            AttachedImage attachedImage = (AttachedImage) entry.getValue();

            // recalculate Stoptime based on least bigger starttime
            attachedImage.setStoptime(
                    AttachedImage.getStopTimeForAttachedImage(String.valueOf(entry.getKey()),
                            attachedPictures,
                            (int) tracklengthMillis));

            // calculate Coordinates and add to Panel
            double percentageStart = ((double) attachedImage.getStarttimeMillis()) / tracklengthMillis;
            double percentageStop = ((double) attachedImage.getStoptime()) / tracklengthMillis;

            int x1 = (int) (totalWidth * (percentageStart));
            int y1 = 0;
            int x2 = (int) (totalWidth * (percentageStop));
            int y2 = 20;
            int imgWidth = x2 - x1;
            int imgHeight = y2 - y1;

            attachedImage.setBackground(Color.yellow);
            attachedImage.setBounds(x1, y1, imgWidth, imgHeight);
            attachedImage.setVisible(true);
            attachedImage.setToolTipText(Other.getFormattedTimeForAttachedImage(attachedImage));

            jPanelAttachedPictures.add(attachedImage);

        }


        jPanelAttachedPictures.revalidate();
        super.paint(g);
    }

    private void resetUI(){
        this.imageListModel.removeAllElements();

        this.player.stop(playerBar);
        this.player = null;
        this.jButtonPlayPause.setEnabled(false);
        this.jButtonPlayPause.setText("Start");
        this.jButtonReset.setEnabled(false);
        this.jLabelMP3Name.setText("Keine MP3 geladen");
        this.jLabelFrames.setText("");
        this.attachedPictures = new HashMap<>();

        this.jLabelPreviewText.setText("Bildvorschau");
        ImageIcon imageIcon = new ImageIcon(IOUtil.loadImgFromResources("img/blank.png").getScaledInstance(
                jLabelImagePreview.getWidth(), jLabelImagePreview.getHeight(), Image.SCALE_SMOOTH));
        this.jLabelImagePreview.setIcon(imageIcon);
        this.jButtonAttachPicture.setEnabled(false);
        this.jPanelAttachedPictures.removeAll();

        this.mp3Model = new MP3Model();
        this.playerBar.displayNothing();

        jPanelEdit.setVisible(false);

        repaint();
    }

    private void initComponents() {
        //Menu
        jMenuBar = new JMenuBar();

        // Menu File
        jMenuFile = new JMenu("Datei");
        jMenuResetWorkspace = new JMenuItem("Arbeitsplatz zurücksetzen");
        jMenuFileItemSave = new JMenuItem("Speichern");

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
        playerBar = new PlayerBar();
        jButtonPlayPause = new JButton("Start");
        jButtonReset = new JButton("Stop");
        jPanelAttachedPictures = new JPanel();
        // displaying Frames, for testing purposes
        jLabelFrames = new JLabel("");

        // Borders
        blackBorder = new LineBorder(Color.BLACK, 1);
        redBorder = new LineBorder(Color.RED, 2);
    }
}
