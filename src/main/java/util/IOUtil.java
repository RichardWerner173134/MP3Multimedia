package util;

import org.jaudiotagger.audio.mp3.MP3File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOUtil {
    public final static String PATH_IMG = "src/main/resources/img/";

    public static BufferedImage loadImgFromResources(String filePath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(PATH_IMG + filePath));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static BufferedImage loadImg(String filePath){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filePath));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static File createCopy(File fileToCopy){
        File uneditedCopyFile = null;
        try {
            uneditedCopyFile = getBackupFile(fileToCopy);
            if(uneditedCopyFile.createNewFile()){
                System.out.println("File create successfully");
            } else{
                throw new FileAlreadyExistsException("File " + uneditedCopyFile.getAbsolutePath() + " already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uneditedCopyFile;
    }

    public static void renameFile(MP3File editedMP3, MP3File copiedMP3) {
        Path original = Paths.get(editedMP3.getFile().getAbsolutePath());
        Path copied = Paths.get(copiedMP3.getFile().getAbsolutePath());
        Path originalBackup = Paths.get(getBackupFile(editedMP3.getFile()).getAbsolutePath());

        try {
            Files.move(original, originalBackup);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.move(copied, original);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.delete(originalBackup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getBackupFile(File oldFile){
        int suffix = 0;
        String fileExtension = ".mp3";
        String filepath = "";
        if (oldFile.getAbsolutePath().endsWith(".mp3")) {
            filepath = oldFile.getAbsolutePath().replace(".mp3", "");
        } else {
            filepath = oldFile.getAbsolutePath();
        }
        File newFile = new File(filepath + fileExtension);

        while(newFile.exists()){
            suffix++;
            newFile = new File(filepath + suffix + fileExtension);
        }
        return newFile;
    }

}
