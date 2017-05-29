package pt.uma.mediaplayer_ssui;


import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import static android.graphics.drawable.Drawable.createFromPath;

/**
 * Created by ldin6 on 07/05/2017.
 * Its necessary to implement the parcelable class so custom arrayList
 * of objects can be sent using intentents , serialiable could be used
 * but parcelable is better performance wise
 */

public class IntelBin implements Parcelable{
    private int mId;
    private String mName;
    private String mDescription;
    private int mLevel;
    private int mFloor;
    private int mFloor_id;
    private String mBuilding;
    private String mLocation;
    private Boolean mSelected;




    public IntelBin(int mId, String mName, String mDescription, int mLevel, int mFloor, int mFloor_id, String mBuilding,String mLocation){
        this.mId =mId;
        this.mName=mName;
        this.mDescription=mDescription;
        this.mLevel=mLevel;
        this.mFloor=mFloor;
        this.mFloor_id=mFloor_id;
        this.mBuilding=mBuilding;
        this.mLocation=mLocation;
        mSelected=Boolean.FALSE;
    }

    //Parcel construtor from parceable
    public IntelBin(Parcel source){
        mId = source.readInt();
        mName = source.readString();
        mDescription = source.readString();
        mLevel = source.readInt();
        mFloor = source.readInt();
        mFloor_id = source.readInt();
        mBuilding =source.readString();
    }



    public String getName(){
        return mName;
    }

    public String getDescription(){
        return mDescription;
    }

    public int getLevel() {return mLevel;}

    public int getFloor() {return mFloor;}

    public int getFloorId(){return mFloor_id;}

    public String getBuilding() {return mBuilding;}

    public boolean isSelected(){
        return mSelected;
    }

    public void  setSelected(Boolean status){
        mSelected=status;
    }

    //List of selected musics remove non selected
    public static ArrayList<IntelBin> getSelectedSong(ArrayList<IntelBin> aList){
        ArrayList<IntelBin> toBeRemoved = new ArrayList<IntelBin>();
        for (IntelBin aEntry:aList)
        {
            if(!(aEntry.isSelected()))
                toBeRemoved.add(aEntry);
        }
        aList.removeAll(toBeRemoved);
        return aList;

    }

    //Media creator
    public static ArrayList<IntelBin> createIntelBinList() {
        ArrayList<IntelBin> intelBins = new ArrayList<IntelBin>();
        return intelBins;
    }

    //Parcelable implementation
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeInt(mFloor);
        dest.writeInt(mFloor_id);
        dest.writeString(mBuilding);
    }


    public int describeContents(){
        return 0;
    }



    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        public IntelBin createFromParcel(Parcel source) {
            return new IntelBin(source);
        }

        public IntelBin[] newArray(int size)
        {
            return new IntelBin[size];
        }
    };
}




