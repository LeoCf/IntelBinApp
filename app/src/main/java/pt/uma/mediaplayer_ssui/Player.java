package pt.uma.mediaplayer_ssui;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.media.AudioManager;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import java.util.ArrayList;


public class Player extends FragmentActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, OnGestureListener {


    private ArrayList<Multimedia> selectedSongs; //Songs to be Played
    private Utilities utilities;
    private ImageButton b1, b2, b3, b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();
    private SeekBar seekbar;
    private SeekBar volumeBar = null;
    private AudioManager audioManager = null;
    private TextView tx1, tx2, tx3, tx4;
    private int currentSongIndex = 0;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector nextShakeDetector;
    private GestureDetector detector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loadVariables();
        playPauseSong(0);

        seekbar.setOnSeekBarChangeListener(this);
        volumeBar.setOnSeekBarChangeListener(this);

        //Sensor Manager - declares onShake to change songs
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        nextShakeDetector = new ShakeDetector();
        nextShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                float gForce = nextShakeDetector.getgForce();
                if (gForce < 2.05) {
                    if (currentSongIndex < (selectedSongs.size() - 1)) {
                        playPauseSong(currentSongIndex + 1);
                        currentSongIndex = currentSongIndex + 1;
                    } else {
                        playPauseSong(0);
                        currentSongIndex = 0;
                    }
                }
                else{
                    if(currentSongIndex > 0)
                    {
                        playPauseSong(currentSongIndex - 1);
                        currentSongIndex = currentSongIndex - 1;
                    }
                    else {
                        //play last song
                        playPauseSong(0);
                        currentSongIndex = 0;
                    }
                }
            }
        });
    }


    public void loadVariables(){
        utilities = new Utilities();


        setContentView(R.layout.activity_player);

        //Intent where we get the songs
        selectedSongs = new ArrayList<Multimedia>();
        selectedSongs = getIntent().getParcelableArrayListExtra("selectedMusicList");

        //Buttons
        b1 = (ImageButton) findViewById(R.id.button0001);
        b2 = (ImageButton) findViewById(R.id.button0002);
        b3 = (ImageButton) findViewById(R.id.button0003);
        b4 = (ImageButton) findViewById(R.id.button0004);
        iv = (ImageView) findViewById(R.id.imageView0001);

        //Textviews
        tx1 = (TextView) findViewById(R.id.textView2);
        tx2 = (TextView) findViewById(R.id.textView3);
        tx3 = (TextView) findViewById(R.id.textView4);
        tx4 = (TextView) findViewById(R.id.textView5);

        //Sets the inital artist, name and album
        tx3.setText(selectedSongs.get(0).getName());
        tx4.setText(selectedSongs.get(0).getArtist());
        iv.setImageDrawable(selectedSongs.get(0).getImage());
        Uri uri = Uri.parse(selectedSongs.get(0).getPath());
        mediaPlayer = MediaPlayer.create(this,uri);

        //Play Button
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //If the song is playing, does nothing
                if(mediaPlayer.isPlaying())
                {}
                else
                {
                    //If the song is not playing, Resumes song
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.start();
                    }
                }
            }
        });

        //Pause Button
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                }
            }
        });


        //Previous Button - Checks if the current song index is the first song in the array
        //                - If it is not the first song, goes to the previous song in the array
        //                - If it is, stays on the first song of the array
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(currentSongIndex > 0)
                {
                    playPauseSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }
                else {
                    //play last song
                    playPauseSong(0);
                    currentSongIndex = 0;
                }

            }
        });

        //Next Button - Next button, goes through the array. When arrives to the last song, goes back to the first
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(currentSongIndex < (selectedSongs.size()-1))
                {
                    playPauseSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex+1;
                }
                else
                {
                    playPauseSong(0);
                    currentSongIndex = 0;
                }
            }
        });



        //Seekbar declaration - for time and volume bar
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setMax(mediaPlayer.getDuration());
        volumeBar = (SeekBar)findViewById(R.id.seekBarVolume);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setMax(audioManager.getStreamMaxVolume((AudioManager.STREAM_MUSIC)));
        volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Gesture detector
        detector = new GestureDetector(this, this);


        //Sets the gesture detector only in the album's image
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

    }

    //This function is responsable for setting the player for the current song and playing it
    public void playPauseSong(int songIndex) {
        //Play song
        try {
            mediaPlayer.reset();
            Uri uri = Uri.parse(selectedSongs.get(songIndex).getPath());
            mediaPlayer = MediaPlayer.create(this,uri);
            mediaPlayer.start();

            //Displaying Song Title and image
            tx3.setText(selectedSongs.get(songIndex).getName());
            tx4.setText(selectedSongs.get(songIndex).getArtist());
            iv.setImageDrawable(selectedSongs.get(songIndex).getImage());

            //Set Progress Bar Values
            seekbar.setProgress(0);
            seekbar.setMax(100);

            //update Progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    //Runnable responsible for updating the song's time
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            //Displaying total Duration Time
            tx2.setText(""+utilities.milliSecondsToTimer(totalDuration));

            //Displaying time completed playing
            tx1.setText(""+utilities.milliSecondsToTimer(currentDuration));

            //updating progress bar
            int progress = (int)(utilities.getProgressPercentage(currentDuration, totalDuration));

            seekbar.setProgress(progress);

            //Running this thread after 100 milliseconds
            myHandler.postDelayed(this, 100);

        }
    };

    //Updates the progress bar progress as the song is played
    public void updateProgressBar(){
        myHandler.postDelayed(UpdateSongTime, 100);
    }


    //Seeks changed in the progress bar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        switch (seekBar.getId()){
            //Music Progress Seekbar
            case R.id.seekBar:
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
                break;
            //Volume Seekbar
            case R.id.seekBarVolume:
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                break;
        }


    }

    //Tracks when the user starts pressing the seekbar
    @Override
    public void onStartTrackingTouch(SeekBar seekbar) {
        myHandler.removeCallbacks(UpdateSongTime);
    }

    //Tracks when the user stops pressing the seekbar
    @Override
    public void onStopTrackingTouch(SeekBar seekbar) {
        switch (seekbar.getId()){
            case R.id.seekBar:
                myHandler.removeCallbacks(UpdateSongTime);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = utilities.progressToTimer(seekbar.getProgress(), totalDuration);

                //forward or backward to certain seconds
                mediaPlayer.seekTo(currentPosition);

                //update timer progress again
                updateProgressBar();
                break;
            case R.id.seekBarVolume:
            {
                updateProgressBar();
                break;
            }
        }

    }

    //On Song Playing Completed
    @Override
    public void onCompletion(MediaPlayer arg0)
    {

        if(currentSongIndex < (selectedSongs.size() -1))
        {
            playPauseSong(currentSongIndex +1);
            currentSongIndex = currentSongIndex +1;
        }
        else
        {
            //play first song
            playPauseSong(0);
            currentSongIndex = 0;
        }
    }

    //On pressing the back button, clears the arraylist and stops reproducing the current song
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MultimediaList.class));
        mediaPlayer.stop();
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(nextShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(nextShakeDetector);
        super.onPause();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    //Function responsible for controlling the swipes
    //Left-to-right swipe - plays next song
    //Right-to-Left swipe - Plays previous song
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Swipe Next update
        if (e1.getX() < e2.getX()) {
            if(currentSongIndex < (selectedSongs.size()-1))
            {
                playPauseSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex+1;
            }
            else
            {
                playPauseSong(0);
                currentSongIndex = 0;
            }
        }
        //Swipe Backward update
        if (e1.getX() > e2.getX()) {
            if(currentSongIndex > 0)
            {
                playPauseSong(currentSongIndex - 1);
                currentSongIndex = currentSongIndex - 1;
            }
            else {
                //play last song
                playPauseSong(0);
                currentSongIndex = 0;
            }
        }

        return true;
    }



}
