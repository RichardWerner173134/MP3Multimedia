package components;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.Getter;
import lombok.Setter;

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
   private String fileLocation;

   public AudioPlayer(String path){
       fileLocation = path + "";
   }

   public void play(){
       new Thread(() -> {
           try {
               fis = new FileInputStream(fileLocation);
               bis = new BufferedInputStream(fis);
               player = new Player(bis);
               songTotalLength = fis.available();
               player.play();
           } catch (JavaLayerException | IOException e) {
               e.printStackTrace();
           }
       }).start();
   }

   public void pause(){
       if(player != null){
           try {
               pauseLocation = fis.available();
               player.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }

   public void stop(){
       pauseLocation = 0;
       player.close();
   }

   public void resume(){
        new Thread(() -> {
            try {
                fis = new FileInputStream(fileLocation);
                bis = new BufferedInputStream(fis);
                player = new Player(bis);
                songTotalLength = fis.available();

                if(pauseLocation != 0){
                    fis.skip(songTotalLength - pauseLocation);
                }

                player.play();
            } catch (JavaLayerException | IOException e){
                e.printStackTrace();
            }
        }).start();
   }

   public void setFile(String path){
       this.fileLocation = path;
   }

}