package pt.uma.mediaplayer_ssui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ldin6 on 07/05/2017.
 */

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaAdapter.ViewHolder>  {


    //Variables
    private List<Multimedia> mMultimedias;
    private Context mContext;
    private ArrayList<Multimedia> mMultimediasCopy = new ArrayList<>();
    private boolean misCheckedAll;

    public MultimediaAdapter(Context mContext, List<Multimedia> mMultimedias) {
        this.mMultimedias = mMultimedias;
        this.mContext = mContext;
        mMultimediasCopy.addAll(mMultimedias);

    }

    public void setCheckedAll(Boolean misCheckedAll){
        this.misCheckedAll=misCheckedAll;
    }

    public boolean isCheckedAll(){
        return misCheckedAll;
    }

    public void SortList(Comparator comparator) {
        Collections.sort(mMultimedias, comparator);
        notifyDataSetChanged();
    }

    private Context getContext() {
        return mContext;
    }


    @Override
    public MultimediaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate o layout custom
        View multimediaView = inflater.inflate(R.layout.layoutrow, parent, false);

        //Retorns a new ViewHolder
        ViewHolder viewHolder = new ViewHolder(multimediaView);
        return viewHolder;
    }
        //Goes through the list and sets the multimedia as selected
        public void checkAll(Boolean isChecked){
            for (int i=0 ; i<mMultimedias.size();i++){
                mMultimedias.get(i).setSelected(isChecked);
            }
            setCheckedAll(isChecked);
            notifyDataSetChanged();
        }


        //Searching based on song name or artist
        public void filter(String text){
            mMultimedias.clear();
            if(text.isEmpty())
            {
                mMultimedias.addAll(mMultimediasCopy);// Copy back in case searchtext is empty
            }
            else
            {
                text.toLowerCase();
                for(Multimedia aMultimedia:mMultimediasCopy)
                {
                    String name = aMultimedia.getName();
                    String artist =aMultimedia.getArtist();
                    if(name.equalsIgnoreCase(text)||artist.equalsIgnoreCase(text)){
                        mMultimedias.add(aMultimedia);
                    }
                }
            }
            notifyDataSetChanged();
        }


    @Override
    public void onBindViewHolder(MultimediaAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Multimedia multimedia = mMultimedias.get(position);
        Utilities milSecConverter = new Utilities();

        // Set item views based on the  views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(multimedia.getName());
        TextView textView1 = viewHolder.artistTextView;
        textView1.setText(multimedia.getArtist());
        ImageView albumView = viewHolder.albumImageView;
        albumView.setImageDrawable(multimedia.getImage());
        TextView yearView = viewHolder.yearTextView;
        yearView.setText(String.valueOf(multimedia.getYear()));
        TextView lenght = viewHolder.lengthTextView;
        lenght.setText(milSecConverter.milliSecondsToTimer(multimedia.getDuration()));


        //Selecting checkBox
        CheckBox mSelected = viewHolder.selectionBox;
        mSelected.setOnCheckedChangeListener(null);
        mSelected.setSelected(multimedia.isSelected());
        mSelected.setChecked(isCheckedAll()); // set the textbox checked if all are checked

        //Selects the song that was checked
        mSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mMultimedias.get(position).setSelected(isChecked);
                setCheckedAll(false);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mMultimedias.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView artistTextView;
        public ImageView albumImageView;
        public TextView yearTextView;
        public TextView lengthTextView;
        public CheckBox selectionBox;


        public ViewHolder(View itemView)
        {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.media_name);
            artistTextView = (TextView) itemView.findViewById(R.id.artist_name);
            albumImageView= (ImageView) itemView.findViewById(R.id.album_image);
            yearTextView =   (TextView) itemView.findViewById(R.id.music_year);
            lengthTextView = (TextView) itemView.findViewById(R.id.music_length);
            selectionBox = (CheckBox) itemView.findViewById(R.id.music_checkBox);


        }
    }

}