package pt.uma.mediaplayer_ssui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ldin6 on 07/05/2017.
 */

public class IntelBinAdapter extends RecyclerView.Adapter<IntelBinAdapter.ViewHolder>  {


    //Variables
    private List<IntelBin> mIntelBins;
    private Context mContext;

    public IntelBinAdapter(Context mContext, List<IntelBin> mIntelBins) {
        this.mIntelBins = mIntelBins;
        this.mContext = mContext;

    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public IntelBinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate o layout custom
        View IntelBinView = inflater.inflate(R.layout.layoutrow, parent, false);

        //Retorns a new ViewHolder
        ViewHolder viewHolder = new ViewHolder(IntelBinView);
        return viewHolder;
    }


        /*
        //Searching based on song name or artist
        public void filter(String text){
            mIntelBins.clear();
            if(text.isEmpty())
            {
                mIntelBins.addAll(mMultimediasCopy);// Copy back in case searchtext is empty
            }
            else
            {
                text.toLowerCase();
                for(IntelBin aIntelBin :mMultimediasCopy)
                {
                    String name = aIntelBin.getName();
                    String artist = aIntelBin.getArtist();
                    if(name.equalsIgnoreCase(text)||artist.equalsIgnoreCase(text)){
                        mIntelBins.add(aIntelBin);
                    }
                }
            }
            notifyDataSetChanged();
        }
    */

    @Override
    public void onBindViewHolder(IntelBinAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final IntelBin intelBin = mIntelBins.get(position);
        Utilities milSecConverter = new Utilities();

        // Set item views based on the  views and data model

        TextView textView1 = viewHolder.intelBinNameView;
        textView1.setText(intelBin.getName());



        //Selecting checkBox
        CheckBox mSelected = viewHolder.selectionBox;
        mSelected.setOnCheckedChangeListener(null);
        mSelected.setSelected(intelBin.isSelected());

        //Selects the song that was checked
        mSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mIntelBins.get(position).setSelected(isChecked);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mIntelBins.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView intelBinNameView;
        public ImageView albumImageView;
        public TextView yearTextView;
        public TextView lengthTextView;
        public CheckBox selectionBox;


        public ViewHolder(View itemView)
        {
            super(itemView);
            intelBinNameView = (TextView) itemView.findViewById(R.id.intelBin_name);
            albumImageView= (ImageView) itemView.findViewById(R.id.intelBin_building);
            yearTextView =   (TextView) itemView.findViewById(R.id.intelBin_corridor);
            lengthTextView = (TextView) itemView.findViewById(R.id.intelBin_level);
            selectionBox = (CheckBox) itemView.findViewById(R.id.music_checkBox);


        }
    }

}