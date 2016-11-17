package moe.codeest.enviewsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import moe.codeest.enviews.ENLoadingView;

/**
 * Created by codeest on 16/11/15.
 */

public class LoadingActivity extends Activity{

    ENLoadingView loadingView;
    Button btnShow;
    Button btnHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loadingView = (ENLoadingView) findViewById(R.id.view_loading);
        btnShow = (Button) findViewById(R.id.btn_show);
        btnHide = (Button) findViewById(R.id.btn_hide);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.show();
            }
        });
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.hide();
            }
        });
    }

}
