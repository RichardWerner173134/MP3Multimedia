package util;

import gui.frame.AttachedImage;

public class Other {
    public static String getToolTipTextForJButton(AttachedImage attachedImage){
        int sStartAll, hStart, mStart, sStart;
        int sStopAll, hStop, mStop, sStop;

        sStartAll = attachedImage.getStarttimeMillis() / 1000;
        hStart = sStartAll / (60*60);
        mStart = (sStartAll - hStart * 60 * 60) / 60;
        sStart = (sStartAll - hStart * 60 * 60 - mStart * 60) % 60;

        String start = addZeroPadding(hStart + ":" + mStart + ":" + sStart);

        sStopAll = attachedImage.getStoptime() / 1000;
        hStop = sStopAll / (60*60);
        mStop = (sStopAll - hStop * 60 * 60) / 60;
        sStop = (sStopAll - hStop * 60 * 60 - mStop * 60) % 60;
        String stop = addZeroPadding(hStop + ":" + mStop + ":" + sStop);

        return "\"" + attachedImage.getImageTitle()
                + "\", Startzeit: " + start
                + ", Stoppzeit: " + stop;
    }

    private static String addZeroPadding(String s) {
        String [] parts = s.split(":");
        String paddedString = "";
        for(int i = 0; i < parts.length; i++){
            if(parts[i].length() < 2){
                paddedString += "0" + parts[i];
            } else{
                paddedString += parts[i];
            }
            if(i != 2){
                paddedString += ":";
            }
        }

        return paddedString;
    }
}
