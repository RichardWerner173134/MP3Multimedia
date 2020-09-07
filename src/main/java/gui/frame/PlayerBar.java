package gui.frame;

import lombok.Getter;
import lombok.Setter;
import org.jaudiotagger.audio.mp3.MP3File;

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
    }

    public void displayMP3(){
        jProgressBar.setBackground(Color.YELLOW);
    }

    public void displayNothing(){
        jProgressBar.setBackground(null);
    }

    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        jProgressBar.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(drawCursor){
            System.out.println("Painting: \nx1=" + x1 + "\ny1=" + y1 + "\nx2=" + x2 + "\ny2=" + y2);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(10));
            g.drawLine(x1, y1, x2, y2);
            g.drawString(currentPosSec + "/" + tracklengthSec, getWidth() / 2, getHeight() / 2);
        }
    }

    public void startMovingCursor(long pauseLocation, long songTotalLength, MP3File mp3File){
        AtomicInteger pauseLocationAtomic = new AtomicInteger((int) pauseLocation);

        drawCursor = true;
        y1 = jProgressBar.getY();
        y2 = jProgressBar.getY() + jProgressBar.getHeight();

        double percentage = ((double)pauseLocation) / ((double)(songTotalLength));

        tracklengthSec = mp3File.getAudioHeader().getTrackLength();
        currentPosSec = (int)(((double)tracklengthSec) * percentage);

        cursorThread = new Thread(() -> {
            while((currentPosSec <= tracklengthSec) && (drawCursor) && (!Thread.currentThread().isInterrupted())){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double newPercentage = ((double)currentPosSec) / ((double)tracklengthSec);
                x1 = (int)(jProgressBar.getWidth() * newPercentage);
                x2 = x1;
                repaint();
                currentPosSec += 1;
            }
            drawCursor = false;
            System.out.println("FERTIG");
        });

        cursorThread.start();
    }

    public void stopDrawing(){
        drawCursor = false;
        cursorThread.interrupt();
    }

}
