package pt.uma.mediaplayer_ssui;

import java.util.Comparator;

/**
 * Created by ruben on 09/05/2017.
 */

public class Utilities {

    //Function to convert milliseconds time to Timer format
    //Hours:Minutes:Seconds

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondString = "";

        //Convert total duration into time
        int hours = (int)(milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) /(1000*60);
        int seconds = (int)((milliseconds %(1000*60*60)) % (1000*60) /1000);
        //Add hours if there

        if(hours > 0) {
            finalTimerString = hours + ":";
        }

        if(seconds < 10){
            secondString = "0" + seconds;
        }else{
            secondString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondString;

        return finalTimerString;
    }

    //Function to get Progress Percentage

    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int)(currentDuration / 1000);
        long totalSeconds = (int)(totalDuration / 1000);

        //Percentage
        percentage = (((double)currentSeconds)/totalSeconds)*100;

        return percentage.intValue();
    }

    //Calculates the progress to reach total duration
    public int progressToTimer(int progress, int totalDuration){
        int currentDuration;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }

    /*
    //Comparations for sorting the lists
    static Comparator<IntelBin> comparatorName  = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            return aMult.getName().compareTo(bMult.getName());
        }
    };

    static Comparator<IntelBin> comparatorNameDesc  = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            return bMult.getName().compareTo(aMult.getName());
        }
    };

    static Comparator<IntelBin> comparatorArtistDesc = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            return bMult.getArtist().compareTo(aMult.getArtist());
        }
    };

    static Comparator<IntelBin> comparatorArtist = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            return aMult.getArtist().compareTo(bMult.getArtist());
        }
    };

    static Comparator<IntelBin> comparatorGender = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            return aMult.getType().compareTo(bMult.getType());
        }
    };

    static Comparator<IntelBin> comparatorYearDesc = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            int a =aMult.getYear();
            int b =bMult.getYear();
            int cmp = a > b ? -1 : a < b ? +1 : 0;
            return cmp;
        }
    };

    static Comparator<IntelBin> comparatorYear = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            int a =aMult.getYear();
            int b =bMult.getYear();
            int cmp = a > b ? +1 : a < b ? -1 : 0;
            return cmp;
        }
    };

    static Comparator<IntelBin> comparatorDuration = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            float a =aMult.getDuration();
            float b =bMult.getDuration();
            int cmp = a > b ? +1 : a < b ? -1 : 0;
            return cmp;
        }
    };

    static Comparator<IntelBin> comparatorDurationDesc = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            float a =aMult.getDuration();
            float b =bMult.getDuration();
            int cmp = a > b ? -1 : a < b ? +1 : 0;
            return cmp;
        }
    };

    //Implements comparator based if music is checked or not.
    static Comparator<IntelBin> comparatorChecked = new Comparator<IntelBin>(){
        @Override
        public int compare(IntelBin aMult, IntelBin bMult){
            int a = (aMult.isSelected()) ? 1 : 0;
            int b = (aMult.isSelected()) ? 1 : 0;
            int cmp = a == b && a==1 ? -1 : 0;
            return cmp;
        }
    };
    */
}
