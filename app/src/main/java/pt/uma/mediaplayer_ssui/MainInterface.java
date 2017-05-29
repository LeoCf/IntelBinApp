package pt.uma.mediaplayer_ssui;

import java.io.IOException;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MainInterface extends AppCompatActivity {

    Button buttonLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove Title Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_interface);

        

        buttonLocal = (Button) findViewById(R.id.Button02);
        buttonLocal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainInterface.this, MultimediaList.class));
            }
        });


    }
}
