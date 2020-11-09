package util;

import gui.frame.AttachedImage;

public class Other {
    public static String getToolTipTextForJButton(AttachedImage attachedImage){
        int msStartAll, mStart, sStart, msStart;
        int msStopAll, mStop, sStop, msStop;

        msStartAll = attachedImage.getStarttimeMillis();
        mStart = msStartAll / (60*1000);
        sStart = (msStartAll - mStart * 60 * 1000) / 1000;
        msStart = (msStartAll - mStart * 60 *1000) % 1000;

        String start = addZeroPadding(mStart + ":" + sStart + ":" + msStart);

        msStopAll = attachedImage.getStoptime();
        mStop = msStopAll / (60*1000);
        sStop = (msStopAll - mStop * 60 * 1000) / 1000;
        msStop = (msStopAll - mStop * 60 * 1000) % 1000;
        String stop = addZeroPadding(mStop + ":" + sStop + ":" + msStop);

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
