package pt.uma.mediaplayer_ssui;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.ToggleButton;
import java.util.ArrayList;


public class MultimediaList extends AppCompatActivity {

    ArrayList<Multimedia> multimediasList; //Multimedia list to be presented
    ToggleButton btn_sortName;
    ToggleButton btn_sortYear;
    ToggleButton btn_sortArtist;
    ToggleButton btn_sortDuration;
    CheckBox    checkBoxAll;
    EditText    search_txt;
    Button      search_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia_list);
        //RecyclerView
        RecyclerView rvMultimedias = (RecyclerView) findViewById(R.id.rvMultimedias);
        ImageButton mPlay_btn = (ImageButton) findViewById(R.id.play_btn);
        //moveTaskToBack(true);
        btn_sortName =(ToggleButton) findViewById(R.id.sory_name);
        btn_sortYear =(ToggleButton) findViewById(R.id.sort_year);
        btn_sortArtist=(ToggleButton) findViewById(R.id.sort_artist);
        btn_sortDuration=(ToggleButton) findViewById(R.id.btn_duration);
        //btn_sortChecked =(ToggleButton) findViewById(R.id.sort_checked);
        multimediasList = Multimedia.createMultimediaList();
        search_txt  =(EditText) findViewById(R.id.search_txt);
        search_btn  =(Button)   findViewById(R.id.search_btn);
        checkBoxAll =(CheckBox) findViewById(R.id.check_all);
        getMedias();
        final MultimediaAdapter adapter = new MultimediaAdapter(this, multimediasList);
        rvMultimedias.setLayoutManager(new LinearLayoutManager(this));
        rvMultimedias.setAdapter(adapter);


        checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.checkAll(isChecked);
                }
            });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =search_txt.getText().toString();
                adapter.filter(text);
            }
        });

        btn_sortName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //sorting based on song name
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.SortList(Utilities.comparatorName);
                } else {
                    adapter.SortList(Utilities.comparatorNameDesc);
                }
            }
        });

        btn_sortYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //sorting based on song name
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.SortList(Utilities.comparatorYear);
                } else {
                    adapter.SortList(Utilities.comparatorYearDesc);
                }
            }
        });

        btn_sortArtist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //sorting based on song name
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.SortList(Utilities.comparatorArtist);
                } else {
                    adapter.SortList(Utilities.comparatorArtistDesc);
                }
            }
        });

        btn_sortDuration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //sorting based on song name
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.SortList(Utilities.comparatorDuration);
                } else {
                    adapter.SortList(Utilities.comparatorDurationDesc);
                }
            }
        });

        mPlay_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent playerStart = new Intent(MultimediaList.this, Player.class);
                playerStart.putParcelableArrayListExtra("selectedMusicList",Multimedia.getSelectedSong(multimediasList));
                startActivity(playerStart);
                finish();
            }
        });


}


    //Gets the songs from the Device
    public void getMedias() {
        ContentResolver contentResolver = getContentResolver();
       // System.gc();
       // String path = Environment.getExternalStorageDirectory().getPath();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, MediaStore.Audio.Albums.ALBUM);//last parameter sorts by album name
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) { // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int pathId = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int yearId = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
            do {
                //entry processing
                int thisId = cursor.getInt(idColumn);
                String thisPath = cursor.getString(pathId);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(artistColumn);
                int   thisYear    = cursor.getInt(yearId);
                long thisDuration = cursor.getLong(durationColumn);
                // String thisType = cursor.getString(typeColumn);
                int albumIdN = cursor.getInt(albumId);

                //Adding to the media list
                multimediasList.add(new Multimedia(thisId, thisTitle, thisArtist,thisYear, thisDuration, "mp3", getAlbumArt(albumIdN),thisPath));
            } while (cursor.moveToNext());
        }
    }

    //Method for searching the album art when we have the song album id
    public String getAlbumArt(int songAlbumId) {
        String albumPath=null;
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursorAlbum = contentResolver.query(uri, null, null, null, null);
        if (cursorAlbum == null)
        {
            // query failed, handle error.
        }
        else if (!cursorAlbum.moveToFirst())
        {
            // no media on the device
        }
        else
            {
                int albumIndex = cursorAlbum.getColumnIndex(MediaStore.Audio.Albums._ID);
                int albumArt = cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                do
                {
                    int thisId = cursorAlbum.getInt(albumIndex);
                    if(songAlbumId==thisId)
                        albumPath = cursorAlbum.getString(albumArt);

                } while (cursorAlbum.moveToNext()) ;


            }
        return albumPath;
    }

}
