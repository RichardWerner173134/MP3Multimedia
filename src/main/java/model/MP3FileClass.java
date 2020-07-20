package model;

import org.farng.mp3.MP3File;

public class MP3FileClass {
    private MP3File mp3File;

    public MP3FileClass(MP3File mp3File){
        this.mp3File = mp3File;
        printTag();
    }

    private void printTag(){
        System.out.println(mp3File.getID3v2Tag());
    }
}
