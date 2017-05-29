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


    //Comparations for sorting the lists
    static Comparator<Multimedia> comparatorName  = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            return aMult.getName().compareTo(bMult.getName());
        }
    };

    static Comparator<Multimedia> comparatorNameDesc  = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            return bMult.getName().compareTo(aMult.getName());
        }
    };

    static Comparator<Multimedia> comparatorArtistDesc = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            return bMult.getArtist().compareTo(aMult.getArtist());
        }
    };

    static Comparator<Multimedia> comparatorArtist = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            return aMult.getArtist().compareTo(bMult.getArtist());
        }
    };

    static Comparator<Multimedia> comparatorGender = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            return aMult.getType().compareTo(bMult.getType());
        }
    };

    static Comparator<Multimedia> comparatorYearDesc = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            int a =aMult.getYear();
            int b =bMult.getYear();
            int cmp = a > b ? -1 : a < b ? +1 : 0;
            return cmp;
        }
    };

    static Comparator<Multimedia> comparatorYear = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            int a =aMult.getYear();
            int b =bMult.getYear();
            int cmp = a > b ? +1 : a < b ? -1 : 0;
            return cmp;
        }
    };

    static Comparator<Multimedia> comparatorDuration = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            float a =aMult.getDuration();
            float b =bMult.getDuration();
            int cmp = a > b ? +1 : a < b ? -1 : 0;
            return cmp;
        }
    };

    static Comparator<Multimedia> comparatorDurationDesc = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            float a =aMult.getDuration();
            float b =bMult.getDuration();
            int cmp = a > b ? -1 : a < b ? +1 : 0;
            return cmp;
        }
    };

    //Implements comparator based if music is checked or not.
    static Comparator<Multimedia> comparatorChecked = new Comparator<Multimedia>(){
        @Override
        public int compare(Multimedia aMult,Multimedia bMult){
            int a = (aMult.isSelected()) ? 1 : 0;
            int b = (aMult.isSelected()) ? 1 : 0;
            int cmp = a == b && a==1 ? -1 : 0;
            return cmp;
        }
    };
}
