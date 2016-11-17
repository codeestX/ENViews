package moe.codeest.enviewsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnRefresh;
    Button btnPlay;
    Button btnDownload;
    Button btnScroll;
    Button btnVolume;
    Button btnSearch;
    Button btnLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnScroll = (Button) findViewById(R.id.btn_scroll);
        btnVolume = (Button) findViewById(R.id.btn_volume);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnLoading = (Button) findViewById(R.id.btn_loading);
        btnRefresh.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        btnScroll.setOnClickListener(this);
        btnVolume.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnLoading.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh:
                startActivity(new Intent(this, RefreshActivity.class));
                break;
            case R.id.btn_play:
                startActivity(new Intent(this, PlayActivity.class));
                break;
            case R.id.btn_download:
                startActivity(new Intent(this, DownloadActivity.class));
                break;
            case R.id.btn_scroll:
                startActivity(new Intent(this, ScrollActivity.class));
                break;
            case R.id.btn_volume:
                startActivity(new Intent(this, VolumeActivity.class));
                break;
            case R.id.btn_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.btn_loading:
                startActivity(new Intent(this, LoadingActivity.class));
        }
    }
}
