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
    private JButton cursorMP3;
    private JProgressBar jProgressBar;
    private int length;

    public PlayerBar(){
        setLayout(null);

        jProgressBar = new JProgressBar();
        add(jProgressBar);
        jProgressBar.setEnabled(true);
        jProgressBar.setValue(0);
        jProgressBar.setVisible(true);

        cursorMP3 = new JButton();
        add(cursorMP3);
        cursorMP3.setEnabled(false);
        cursorMP3.setVisible(true);
        cursorMP3.setLocation(0,0);
        cursorMP3.setSize(8, 32);
        cursorMP3.setBackground(Color.black);
    }

    public void displayMP3(){
        cursorMP3.setEnabled(true);
        jProgressBar.setBackground(Color.YELLOW);
    }

    public void displayNothing(){
        cursorMP3.setEnabled(false);
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
        System.out.println("Painting: \nx1=" + x1 + "\ny1=" + y1 + "\nx2=" + x2 + "\ny2=" + y2);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g.drawLine(x1, y1, x2, y2);
        g.drawString(currentPosSec + "/" + tracklengthSec, getWidth() / 2, getHeight() / 2);
    }

    public void startMovingCursor(long pauseLocation, long songTotalLength, MP3File mp3File){
        AtomicInteger pauseLocationAtomic = new AtomicInteger((int) pauseLocation);
        AtomicInteger currentTimeAtomic;
        y1 = cursorMP3.getY();
        y2 = cursorMP3.getY() + jProgressBar.getHeight();


        tracklengthSec = mp3File.getAudioHeader().getTrackLength();
        double percents;
        if(songTotalLength == 0){
            percents = 0.0;
            currentTimeAtomic = new AtomicInteger(0);
        }else {
            percents = ((int)pauseLocationAtomic.get()) / ((double) songTotalLength);

            currentTimeAtomic = new AtomicInteger((int) (percents * tracklengthSec));
        }


        int cursorX = pauseLocationAtomic.get();
        int cursorY = cursorMP3.getY();


        new Thread(() -> {
            while(pauseLocationAtomic.get() <= songTotalLength){
                System.out.println(pauseLocationAtomic.get() < songTotalLength);
                System.out.println(pauseLocationAtomic.get()  + ", " +  songTotalLength);
                //cursorMP3.setLocation(cursorX + pauseLocationAtomic.get(), cursorY);
                pauseLocationAtomic.addAndGet(20);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x1 = pauseLocationAtomic.get();
                x2 = x1;
                if(currentTimeAtomic != null){
                    int i = (int) (percents * tracklengthSec);
                    int j = i - currentPosSec;
                    currentPosSec = j;
                }
                System.out.println("currenttime " + currentPosSec);
                repaint();
            }
            System.out.println("FERTIG");
        }).start();


    }
}
