package gui.frame;

import lombok.Getter;
import model.MP3Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

@Getter
public class DialogView extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField jTextFieldImage;
    private JTextField jTextFieldStartH = new JTextField();
    private JTextField jTextFieldStartM = new JTextField();
    private JTextField jTextFieldStartS = new JTextField();
    private JLabel jLabelInfo = new JLabel("");
    private JButton okButton;

    /**
     * Create the dialog.
     */
    public DialogView(String selectedValue, MP3Model mp3Model, BufferedImage bufferedImage,
                      HashMap<String, AttachedImage> attachedImages, int[] currentTimeStamp) {
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
        lblNewLabel_1.setBounds(12, 89, 81, 44);
        contentPanel.add(lblNewLabel_1);

        jTextFieldStartH.setBounds(155, 89, 52, 44);
        contentPanel.add(jTextFieldStartH);
        jTextFieldStartH.setColumns(10);

        jTextFieldStartM.setBounds(230, 89, 52, 44);
        contentPanel.add(jTextFieldStartM);
        jTextFieldStartM.setColumns(10);

        jTextFieldStartS.setBounds(302, 89, 52, 44);
        contentPanel.add(jTextFieldStartS);
        jTextFieldStartS.setColumns(10);

        jLabelInfo.setBounds(12,35,200,44);
        contentPanel.add(jLabelInfo);

        if(currentTimeStamp != null){
            jTextFieldStartS.setText(currentTimeStamp[0] + "");
            jTextFieldStartM.setText(currentTimeStamp[1] + "");
            jTextFieldStartH.setText(currentTimeStamp[2] + "");
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
                        int starttimeH = Integer.parseInt(jTextFieldStartH.getText());
                        int starttimeM = Integer.parseInt(jTextFieldStartM.getText());
                        int starttimeS = Integer.parseInt(jTextFieldStartS.getText());
                        int starttimeMillis = timeInMilliSeconds(starttimeH, starttimeM, starttimeS);
                        String imgId = attachedImages.size() + "";
                        AttachedImage attachedImage = new AttachedImage(selectedValue, starttimeMillis);
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
        jTextFieldStartH.addKeyListener(getNewAdapter(jTextFieldStartH));
        jTextFieldStartM.addKeyListener(getNewAdapter(jTextFieldStartM));
        jTextFieldStartS.addKeyListener(getNewAdapter(jTextFieldStartS));
    }

    private KeyAdapter getNewAdapter(JTextField jTextField){
        KeyAdapter keyAdapter = new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                // validate timestamp input
                String value = jTextField.getText();
                char[] valueChars = value.toCharArray();
                boolean isValid = true;
                for(char c : valueChars){
                    if(!(c >= '0' && c <= '9')){
                        isValid = false;
                        break;
                    }
                }

                if (!isValid){
                    jLabelInfo.setText("Bitte nur Ziffern eingeben [0-9]");
                    okButton.setEnabled(false);
                } else{
                    jLabelInfo.setText("");
                    if(isEmpty()){
                        okButton.setEnabled(false);
                    } else {
                        okButton.setEnabled(true);
                    }
                }
            }

        };
        return keyAdapter;
    }

    private int timeInMilliSeconds(int hour, int minute, int seconds) {
        int milliSecondsFromZero = 0;
        milliSecondsFromZero += seconds * 1000;
        milliSecondsFromZero += minute * 60 * 1000;
        milliSecondsFromZero += hour * 60 * 60 * 1000;
        return milliSecondsFromZero;
    }

    private boolean isEmpty() {
        return jTextFieldStartH.getText().isEmpty() || jTextFieldStartM.getText().isEmpty() || jTextFieldStartS.getText().isEmpty();
    }
}