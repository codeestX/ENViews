package moe.codeest.enviewsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import moe.codeest.enviews.ENRefreshView;

/**
 * Created by codeest on 16/11/6.
 */

public class RefreshActivity extends Activity{

    ENRefreshView refreshView;
    Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        refreshView = (ENRefreshView) findViewById(R.id.view_refresh);
        btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshView.startRefresh();
            }
        });
    }
}
