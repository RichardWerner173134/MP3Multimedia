package components;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AudioPlayer{
   private String filepath;
   private AdvancedPlayer advancedPlayer;
   private FileInputStream fis;
   private Thread starter;

   public AudioPlayer(String filepath){
       this.filepath = filepath;
       try {
           fis = new FileInputStream(filepath);
           advancedPlayer = new AdvancedPlayer(fis);
       } catch (FileNotFoundException | JavaLayerException e) {
           e.printStackTrace();
       }

       starter = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   advancedPlayer.play();
               } catch (JavaLayerException e) {
                   e.printStackTrace();
               }
           }
       });
   }

   public void start(){
       starter.start();
   }

   public void stop(){
       Method m = null;
       try {
           m = Thread.class.getDeclaredMethod( "stop0" , new Class[]{Object.class} );
       } catch (NoSuchMethodException e) {
           e.printStackTrace();
       }

       m.setAccessible( true );

       try {
           m.invoke( starter , new ThreadDeath() );
       } catch (IllegalAccessException | InvocationTargetException e) {
           e.printStackTrace();
       }
       starter.stop();
   }


}