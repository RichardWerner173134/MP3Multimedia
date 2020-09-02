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

    public void pause(){
       if(player != null){
           try {
               pauseLocation = songTotalLength - fis.available();
               player.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }

   public void stop(){
       player.close();
       pauseLocation = 0;
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
}