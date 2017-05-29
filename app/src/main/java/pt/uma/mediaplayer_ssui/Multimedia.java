package pt.uma.mediaplayer_ssui;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.id;
import static android.content.ContentValues.TAG;
import static android.graphics.drawable.Drawable.createFromPath;

/**
 * Created by ldin6 on 07/05/2017.
 * Its necessary to implement the parcelable class so custom arrayList
 * of objects can be sent using intentents , serialiable could be used
 * but parcelable is better performance wise
 */

public class Multimedia  implements Parcelable{
    private int mId;
    private String mName;
    private String mArtist;
    private int year;
    private String mType;
    private long mDuration;
    private String imagePath;
    private String songPath;
    private Boolean mSelected;
    private Button mPlay;


    public Multimedia(int mId,String mName,String mArtist,int year,long mDuration,String mType,String imagePath,String songPath){
        this.mId =mId;
        this.mName=mName;
        this.mArtist=mArtist;
        this.year=year;
        this.mDuration=mDuration;
        this.mType=mType;
        this.imagePath=imagePath;
        this.songPath=songPath;
        mSelected=Boolean.FALSE;
    }

    //Parcel construtor from parceable
    public Multimedia (Parcel source){
        mId = source.readInt();
        mName = source.readString();
        mArtist = source.readString();
        year = source.readInt();
        mDuration = source.readLong();
        mType = source.readString();
        imagePath = source.readString();
        songPath = source.readString();
    }



    public String getName(){
        return mName;
    }

    public String getArtist(){
        return mArtist;
    }

    public int getYear(){ return year;}

    public long getDuration(){
        return mDuration;
    }

    public String getType(){
        return mType;
    }

    public String getPath(){
        return songPath;
    }

    public Drawable getImage(){
        Drawable albumCover = createFromPath(this.imagePath);
        return albumCover;
    }

    public boolean isSelected(){
        return mSelected;
    }

    public void  setSelected(Boolean status){
        mSelected=status;
    }

    //List of selected musics remove non selected
    public static ArrayList<Multimedia> getSelectedSong(ArrayList<Multimedia> aList){
        ArrayList<Multimedia> toBeRemoved = new ArrayList<Multimedia>();
        for (Multimedia aEntry:aList)
        {
            if(!(aEntry.isSelected()))
                toBeRemoved.add(aEntry);
        }
        aList.removeAll(toBeRemoved);
        return aList;

    }

    //Media creator
    public static ArrayList<Multimedia> createMultimediaList() {
        ArrayList<Multimedia> multimedias = new ArrayList<Multimedia>();
        return multimedias;
    }

    //Parcelable implementation
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mArtist);
        dest.writeInt(year);
        dest.writeLong(mDuration);
        dest.writeString(mType);
        dest.writeString(imagePath);
        dest.writeString(songPath);
    }


    public int describeContents(){
        return 0;
    }



    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        public Multimedia createFromParcel(Parcel source) {
            return new Multimedia(source);
        }

        public Multimedia[] newArray(int size)
        {
            return new Multimedia[size];
        }
    };
}




