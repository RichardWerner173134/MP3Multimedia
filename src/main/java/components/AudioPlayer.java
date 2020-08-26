package components;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AudioPlayer{
   private FileInputStream fis;
   private BufferedInputStream bis;
   private Player player;

   public void play(String path){
       try {
           fis = new FileInputStream(path);
           bis = new BufferedInputStream(fis);
           player = new Player(bis);
       } catch (FileNotFoundException | JavaLayerException e) {
           e.printStackTrace();
       }
       new Thread(() -> {
           try {
               player.play();
           } catch (JavaLayerException e) {
               e.printStackTrace();
           }
       }).start();
   }

   public void stop(){
       if(player != null){
           player.close();
       }
   }


}