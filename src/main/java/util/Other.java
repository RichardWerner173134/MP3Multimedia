package util;

import gui.frame.AttachedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Other {

    public static String getMinutesForMillis(int msStartAll){
        int mStart = msStartAll / (60*1000);
        return String.valueOf(mStart);
    }

    public static String getSecondsForMillis(int msStartAll){
        int mStart = msStartAll / (60*1000);
        int sStart = (msStartAll - mStart * 60 * 1000) / 1000;
        return String.valueOf(sStart);
    }

    public static String getMilliSecondsForMillis(int msStartAll){
        int mStart = msStartAll / (60*1000);
        int sStart = (msStartAll - mStart * 60 * 1000) / 1000;
        int msStart = (msStartAll - mStart * 60 *1000) % 1000;
        return String.valueOf(msStart);
    }

    public static String getFormattedTimeForAttachedImage(AttachedImage attachedImage){
        int msStartAll, mStart, sStart, msStart;
        int msStopAll, mStop, sStop, msStop;

        msStartAll = attachedImage.getStarttimeMillis();
        mStart = msStartAll / (60*1000);
        sStart = (msStartAll - mStart * 60 * 1000) / 1000;
        msStart = (msStartAll - mStart * 60 *1000) % 1000;

        String start = addZeroPadding(mStart + ":" + sStart + ":" + msStart);

        msStopAll = attachedImage.getStoptime();
        mStop = msStopAll / (60*1000);
        sStop = (msStopAll - mStop * 60 * 1000) / 1000;
        msStop = (msStopAll - mStop * 60 * 1000) % 1000;
        String stop = addZeroPadding(mStop + ":" + sStop + ":" + msStop);

        return "\"" + attachedImage.getImageTitle()
                + "\", Startzeit: " + start
                + ", Stoppzeit: " + stop;
    }

    public static int timeInMilliSeconds(int minute, int seconds, int milliseconds) {
        int milliSecondsFromZero = 0;
        milliSecondsFromZero += milliseconds;
        milliSecondsFromZero += seconds * 1000;
        milliSecondsFromZero += minute * 60 * 1000;
        return milliSecondsFromZero;
    }

    private static String addZeroPadding(String s) {
        String [] parts = s.split(":");
        String paddedString = "";
        for(int i = 0; i < parts.length; i++){
            if(parts[i].length() < 2){
                paddedString += "0" + parts[i];
            } else{
                paddedString += parts[i];
            }
            if(i != 2){
                paddedString += ":";
            }
        }

        return paddedString;
    }

    public static KeyAdapter getNewAdapter(JTextField jTextField1, JLabel jLabelInfo, JButton okButton, JTextField jTextField2, JTextField jTextField3){
        KeyAdapter keyAdapter = new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                // validate timestamp input
                String value = jTextField1.getText();
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
                    if(isEmpty(jTextField1, jTextField2, jTextField3)){
                        okButton.setEnabled(false);
                    } else {
                        okButton.setEnabled(true);
                    }
                }
            }

        };
        return keyAdapter;
    }

    private static boolean isEmpty(JTextField jTextFieldStart1, JTextField jTextFieldStart2, JTextField jTextFieldStartM3) {
        return jTextFieldStart1.getText().isEmpty() || jTextFieldStart2.getText().isEmpty() || jTextFieldStartM3.getText().isEmpty();
    }

    public static void imagePreview(BufferedImage bi){
        File f = null;
        try {
            f = File.createTempFile("temp", ".png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            ImageIO.write(bi, "png", f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        f.deleteOnExit();
        System.out.println("Done.");
    }
}
