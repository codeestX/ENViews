package moe.codeest.enviewsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import moe.codeest.enviews.ENDownloadView;

/**
 * Created by codeest on 16/11/11.
 */

public class DownloadActivity extends Activity {

    private ENDownloadView downloadView;

    private Button btnStart;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        downloadView = (ENDownloadView) findViewById(R.id.view_download);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnReset = (Button) findViewById(R.id.btn_reset);

        downloadView.setDownloadConfig(2000, 20 , ENDownloadView.DownloadUnit.MB);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadView.start();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadView.reset();
            }
        });
    }

}
