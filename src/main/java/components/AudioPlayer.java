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
   private Thread timerThread;
   private int songLength;

   public AudioPlayer(String path){
       try {
           fis = new FileInputStream(path);
           bis = new BufferedInputStream(fis);
           player = new Player(bis);
           songLength = fis.available();
       } catch (JavaLayerException | IOException e) {
           e.printStackTrace();
       }
   }

   public void play(){
       new Thread(() -> {
           try {

               player.play();
           } catch (JavaLayerException e) {
               e.printStackTrace();
           }
       }).start();
   }

   public void stop(){
       player.close();
   }

   public void setFile(String path){
       try {
           fis = new FileInputStream(path);
           bis = new BufferedInputStream(fis);
           player = new Player(bis);
           songLength = fis.available();
       } catch (JavaLayerException | IOException e) {
           e.printStackTrace();
       }
   }

}