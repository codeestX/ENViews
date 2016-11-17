package moe.codeest.enviewsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import moe.codeest.enviews.ENPlayView;

/**
 * Created by codeest on 16/11/8.
 */

public class PlayActivity extends Activity{

    ENPlayView playView;
    Button btnPause;
    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playView = (ENPlayView) findViewById(R.id.view_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playView.pause();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playView.play();
            }
        });
    }
}
