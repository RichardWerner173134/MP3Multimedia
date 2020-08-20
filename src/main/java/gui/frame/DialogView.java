package gui.frame;

import lombok.Getter;
import model.MP3Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
public class DialogView extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField jTextFieldImage;
    private JTextField jTextFieldStart;
    private JTextField jTextFieldStop;
    private JTextField jTextFieldStartH = new JTextField();
    private JTextField jTextFieldStopH = new JTextField();
    private JTextField jTextFieldStartM = new JTextField();
    private JTextField jTextFieldStartS = new JTextField();
    private JTextField jTextFieldStopM = new JTextField();
    private JTextField jTextFieldStopS = new JTextField();


    /**
     * Create the dialog.
     */
    public DialogView(String selectedValue, MP3Model mp3Model, BufferedImage bufferedImage) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
        setBounds(100, 100, 450, 300);
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
        lblNewLabel_1.setBounds(12, 89, 81, 44);
        contentPanel.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Stoppzeit");
        lblNewLabel_2.setBounds(12, 155, 81, 60);
        contentPanel.add(lblNewLabel_2);

        jTextFieldStartH.setBounds(155, 89, 52, 44);
        contentPanel.add(jTextFieldStartH);
        jTextFieldStartH.setColumns(10);

        jTextFieldStopH.setBounds(155, 175, 52, 40);
        contentPanel.add(jTextFieldStopH);
        jTextFieldStopH.setColumns(10);

        jTextFieldStartM.setBounds(230, 89, 52, 44);
        contentPanel.add(jTextFieldStartM);
        jTextFieldStartM.setColumns(10);

        jTextFieldStartS.setBounds(302, 89, 52, 44);
        contentPanel.add(jTextFieldStartS);
        jTextFieldStartS.setColumns(10);

        jTextFieldStopM.setBounds(230, 175, 52, 40);
        contentPanel.add(jTextFieldStopM);
        jTextFieldStopM.setColumns(10);

        jTextFieldStopS.setBounds(302, 175, 52, 40);
        contentPanel.add(jTextFieldStopS);
        jTextFieldStopS.setColumns(10);

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(e -> {
                    try {
                        int starttimeH = Integer.parseInt(jTextFieldStartH.getText());
                        int starttimeM = Integer.parseInt(jTextFieldStartM.getText());
                        int starttimeS = Integer.parseInt(jTextFieldStartS.getText());
                        int stoptimeH = Integer.parseInt(jTextFieldStopH.getText());
                        int stoptimeM = Integer.parseInt(jTextFieldStopM.getText());
                        int stoptimeS = Integer.parseInt(jTextFieldStopS.getText());
                        int starttimeMillis = timeInMilliSeconds(starttimeH, starttimeM, starttimeS);
                        int stoptimeMillis = timeInMilliSeconds(stoptimeH, stoptimeM, stoptimeS);
                        mp3Model.addImage(selectedValue,
                                bufferedImage,
                                starttimeMillis,
                                stoptimeMillis);
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
    }

    private int timeInMilliSeconds(int hour, int minute, int seconds) {
        int milliSecondsFromZero = 0;
        milliSecondsFromZero += seconds * 1000;
        milliSecondsFromZero += minute * 60 * 1000;
        milliSecondsFromZero += hour * 60 * 60 * 1000;
        return milliSecondsFromZero;
    }
}
