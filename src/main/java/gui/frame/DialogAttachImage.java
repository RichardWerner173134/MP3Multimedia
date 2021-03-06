package gui.frame;

import lombok.Getter;
import model.MP3Model;
import util.Other;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.stream.Collectors;

@Getter
public class DialogAttachImage extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField jTextFieldImage;
    private JTextField jTextFieldStartM = new JTextField();
    private JTextField jTextFieldStartS = new JTextField();
    private JTextField jTextFieldStartMS = new JTextField();
    private JLabel jLabelInfo = new JLabel("");
    private JButton okButton;

    /**
     * Create the dialog.
     */
    public DialogAttachImage(String selectedValue, MP3Model mp3Model, BufferedImage bufferedImage,
                             HashMap<String, AttachedImage> attachedImages, int[] currentTimeStamp, JPanel jPanelEdit,
                             JTextField jTextFieldImageName,
                             JTextField jTextFieldStartPassedM, JTextField jTextFieldStartPassedS, JTextField jTextFieldStartPassedMS) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
        setBounds(100, 100, 453, 236);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            JLabel lblNewLabel = new JLabel("Bilddatei");
            lblNewLabel.setBounds(12, 12, 101, 32);
            contentPanel.add(lblNewLabel);
        }

        jTextFieldImage = new JTextField(selectedValue);
        jTextFieldImage.setBounds(155, 12, 250, 37);
        jTextFieldImage.setEditable(false);
        contentPanel.add(jTextFieldImage);
        jTextFieldImage.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Startzeit");
        JLabel lblNewLabel_2 = new JLabel("   min                  sek                 ms");
        lblNewLabel_1.setBounds(12, 89, 130, 44);
        lblNewLabel_2.setBounds(155, 125, 400, 44);
        contentPanel.add(lblNewLabel_1);
        contentPanel.add(lblNewLabel_2);

        jTextFieldStartM.setBounds(155, 89, 52, 44);
        contentPanel.add(jTextFieldStartM);
        jTextFieldStartM.setColumns(10);

        jTextFieldStartS.setBounds(230, 89, 52, 44);
        contentPanel.add(jTextFieldStartS);
        jTextFieldStartS.setColumns(10);

        jTextFieldStartMS.setBounds(302, 89, 52, 44);
        contentPanel.add(jTextFieldStartMS);
        jTextFieldStartMS.setColumns(10);

        jLabelInfo.setBounds(12,35,200,44);
        contentPanel.add(jLabelInfo);

        if(currentTimeStamp != null){
            jTextFieldStartMS.setText(currentTimeStamp[0] + "");
            jTextFieldStartS.setText(currentTimeStamp[1] + "");
            jTextFieldStartM.setText(currentTimeStamp[2] + "");
        }

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(e -> {
                    try {
                        int starttimeM = Integer.parseInt(jTextFieldStartM.getText());
                        int starttimeS = Integer.parseInt(jTextFieldStartS.getText());
                        int starttimeMS = Integer.parseInt(jTextFieldStartMS.getText());
                        int starttimeMillis = Other.timeInMilliSeconds(starttimeM, starttimeS, starttimeMS);
                        String imgId = attachedImages.size() + "";
                        AttachedImage attachedImage = new AttachedImage(selectedValue, starttimeMillis);

                        attachedImage.addActionListener(e2 -> {
                            Border blackBorder = new LineBorder(Color.BLACK, 1);
                            Border redBorder = new LineBorder(Color.RED, 2);
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
                            for(AttachedImage a : attachedImages.values().stream().filter(ai -> ai != attachedImage).collect(Collectors.toList())){
                                a.setSelected(false);
                                a.setBorder(blackBorder);
                            }
                            jTextFieldStartPassedM.setText(Other.getMinutesForMillis(attachedImage.getStarttimeMillis()));
                            jTextFieldStartPassedS.setText(Other.getSecondsForMillis(attachedImage.getStarttimeMillis()));
                            jTextFieldStartPassedMS.setText(Other.getMilliSecondsForMillis(attachedImage.getStarttimeMillis()));
                        });


                        mp3Model.addImage(selectedValue,
                                bufferedImage,
                                starttimeMillis);

                        attachedImages.put(imgId, attachedImage);
                        dispose();
                    }catch(NumberFormatException ex){
                        ex.printStackTrace();
                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(e -> dispose());
            }
        }
        jTextFieldStartM.addKeyListener(Other.getNewAdapter(jTextFieldStartM, jLabelInfo, okButton, jTextFieldStartS, jTextFieldStartMS));
        jTextFieldStartS.addKeyListener(Other.getNewAdapter(jTextFieldStartS, jLabelInfo, okButton, jTextFieldStartM, jTextFieldStartMS));
        jTextFieldStartMS.addKeyListener(Other.getNewAdapter(jTextFieldStartMS, jLabelInfo, okButton, jTextFieldStartM, jTextFieldStartS));
    }
}