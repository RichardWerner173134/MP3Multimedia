package components;

import gui.frame.EditorFrame;
import gui.frame.PlayerBar;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.Getter;
import lombok.Setter;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Setter
@Getter
public class AudioPlayer{
   private FileInputStream fis;
   private BufferedInputStream bis;
   private Player player;
   private long pauseLocation;
   private long songTotalLength;
   private String path;

   public AudioPlayer(String path){
       this.path = path + "";
   }

    private void loadMP3Player() throws FileNotFoundException, JavaLayerException {
        fis = new FileInputStream(path);
        bis = new BufferedInputStream(fis);
        player = new Player(bis);
    }

    public void pause(PlayerBar playerBar){
       if(player != null){
           try {
               pauseLocation = songTotalLength - fis.available();
               player.close();
               playerBar.stopDrawing();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }

   public void stop(PlayerBar playerBar){
       player.close();
       pauseLocation = 0;
       playerBar.stopDrawing();
   }

   public void resume(PlayerBar playerBar, MP3File mp3file){
        new Thread(() -> {
            try {
                loadMP3Player();
                songTotalLength = fis.available();
                fis.skip(pauseLocation);
                playerBar.startMovingCursor(pauseLocation, songTotalLength, mp3file);
                player.play();
            } catch (JavaLayerException | IOException e){
                e.printStackTrace();
            }
        }).start();
   }

   public void setPath(String path){
       this.path = path;
   }

   public int[] getTimeStampPosition(int tracklength){
       // pauselocation, songtotallength, tracklength

       double pauseLocationPercentage = ((double)pauseLocation) / ((double)songTotalLength);
       int sStartAll = (int) (pauseLocationPercentage * tracklength);


       int [] hms = new int[3];
       int sStart, hStart, mStart;

       hStart = sStartAll / (60*60);
       mStart = (sStartAll - hStart * 60 * 60) / 60;
       sStart = (sStartAll - hStart * 60 * 60 - mStart * 60) % 60;

       hms[0] = sStart;
       hms[1] = mStart;
       hms[2] = hStart;
       return hms;
   }
}