package gui.frame;

import lombok.Getter;
import lombok.Setter;
import org.jaudiotagger.audio.mp3.MP3File;
import util.Other;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
public class PlayerBar extends JPanel {
    private int tracklengthSec;
    private int currentPosSec;
    private int x1, x2, y1, y2;
    private boolean drawCursor;
    private boolean hasMP3;

    private JProgressBar jProgressBar;
    private int length;
    private Thread cursorThread;

    public PlayerBar(){
        setLayout(null);

        drawCursor = false;

        jProgressBar = new JProgressBar();
        add(jProgressBar);
        jProgressBar.setEnabled(true);
        jProgressBar.setValue(0);
        jProgressBar.setVisible(true);
        hasMP3 = false;
        y1 = jProgressBar.getY();
        y2 = jProgressBar.getY() + 32;
    }

    public void displayMP3(int tracklengthSec){
        jProgressBar.setBackground(Color.YELLOW);
        hasMP3 = true;
        this.tracklengthSec = tracklengthSec;
    }

    public void displayNothing(){
        jProgressBar.setBackground(null);
        hasMP3 = false;
    }

    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        jProgressBar.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        // drawing cursor if playing
        if(drawCursor || hasMP3){
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(10));
            System.out.println("Painting: \nx1=" + x1 + "\ny1=" + y1 + "\nx2=" + x2 + "\ny2=" + y2);
            g.drawLine(x1, y1, x2, y2);
            g.drawString(Other.getMinutesForMillis(currentPosSec*1000) + ":"
                    + Other.getSecondsForMillis(currentPosSec*1000) + ":"
                    + "/" + Other.getMinutesForMillis(tracklengthSec*1000) + ":"
                    + Other.getSecondsForMillis(tracklengthSec*1000), getWidth() / 2, getHeight() / 2);
        }
    }

    public void startMovingCursor(long pauseLocation, long songTotalLength, MP3File mp3File){
        drawCursor = true;
        double percentage = ((double)pauseLocation) / ((double)(songTotalLength));

        tracklengthSec = mp3File.getAudioHeader().getTrackLength();
        currentPosSec = (int)(((double)tracklengthSec) * percentage);

        cursorThread = new Thread(() -> {
            try{
                while((currentPosSec <= tracklengthSec) && (drawCursor) && (!Thread.currentThread().isInterrupted())){
                    double newPercentage = ((double)currentPosSec) / ((double)tracklengthSec);
                    x1 = (int)(jProgressBar.getWidth() * newPercentage);
                    x2 = x1;
                    Thread.sleep(1000);
                    currentPosSec += 1;
                    repaint();
                }
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            drawCursor = false;
            System.out.println("FERTIG");
        });

        cursorThread.start();
    }

    public void stopDrawing(long pauseLocation, long songTotalLength){
        cursorThread.interrupt();

        double percentage = ((double)pauseLocation) / ((double)(songTotalLength));
        currentPosSec = (int)(((double)tracklengthSec) * percentage);
        x1 = currentPosSec;
        x2 = x1;
        drawCursor = false;
        repaint();
    }

}
