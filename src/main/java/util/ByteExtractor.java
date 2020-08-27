package util;

import model.ContentTimeStamp;
import model.ImageModel;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ByteExtractor {
    private static byte[][] splitByteArray(byte[] bytes, byte[] regex, Charset charset) {
        String str = new String(bytes, charset);
        String[] split = str.split(new String(regex, charset));
        byte[][] byteSplit = new byte[split.length][];
        for (int i = 0; i < split.length; i++) {
            byteSplit[i] = split[i].getBytes(charset);
        }
        return byteSplit;
    }

    public static HashMap getAbstractContentModelMap(byte[] data, int contentType, int timeStampFormat, String description,
                                                     byte textEncoding, String language, ArrayList<ID3v24Frame> apicFrameList) {
        Charset charset = StandardCharsets.ISO_8859_1;

        byte[] regex = {0x0A};
        byte[][] splitted = splitByteArray(data, regex, charset);

        HashMap<String, ImageModel> imageModeMap = generateImageModelMap(splitted);
        addApicImages(imageModeMap, apicFrameList);

        return imageModeMap;
    }

    private static void addApicImages(HashMap<String, ImageModel> imageModelMap, ArrayList<ID3v24Frame> apicFrameList) {
        Iterator it = imageModelMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();

            if(pair.getValue() instanceof ImageModel){
                ImageModel im = (ImageModel) pair.getValue();

                if(im.getBufferedImage() == null){
                    String imageName = String.valueOf(pair.getKey());
                    for(ID3v24Frame f : apicFrameList){
                        if(((FrameBodyAPIC)f.getBody()).getDescription().trim().equals(imageName.trim())){
                            byte[] imageData = ((FrameBodyAPIC)f.getBody()).getImageData();
                            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                            BufferedImage image = null;
                            try {
                                image = ImageIO.read(bais);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            im.setBufferedImage(image);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static HashMap generateImageModelMap(byte[][] splitted) {

        HashMap<String, ImageModel> imageModelMap = new HashMap<>();
        ImageModel model;
        for(int i = 0; i < splitted.length; i++){
            int timeStampNumber = getTimeStampNumber(splitted[i]);
            String contentWithoutTimeStamp = getContentWithoutTimeStamp(splitted[i]);

            if(imageModelMap.containsKey(contentWithoutTimeStamp)){
                model = imageModelMap.get(contentWithoutTimeStamp);
            } else{
                model = new ImageModel();
            }

            model.getTimestampMap().put(
                    String.valueOf(timeStampNumber),
                    new ContentTimeStamp(timeStampNumber));

            imageModelMap.put(contentWithoutTimeStamp, model);

            // System.out.println((char)b1 + "," + (char)b2 + "," + (char)b3 + "," + (char)b4);
            // System.out.println((b1 & 0xff) + " " + "," + (b2 & 0xff) + " " + "," + (b3 & 0xff) + " " + "," + (b4 & 0xff) + " ");
        }
        return imageModelMap;
    }

    private static String getContentWithoutTimeStamp(byte[] splittedByteArray) {
        return StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(
                Arrays.copyOfRange(splittedByteArray, 0, splittedByteArray.length - 5)
        )).toString();

    }

    private static int getTimeStampNumber(byte[] splittedByteArraay) {
        byte[] timestampBytes = new byte[4];
        timestampBytes[0] = (byte) ((splittedByteArraay[splittedByteArraay.length - 4]) & 0xff);
        timestampBytes[1] = (byte) ((splittedByteArraay[splittedByteArraay.length - 3]) & 0xff);
        timestampBytes[2] = (byte) ((splittedByteArraay[splittedByteArraay.length - 2]) & 0xff);
        timestampBytes[3] = (byte) ((splittedByteArraay[splittedByteArraay.length - 1]) & 0xff);
        return ByteBuffer.wrap(timestampBytes).getInt();
    }

}