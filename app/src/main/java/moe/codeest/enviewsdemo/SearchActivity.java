package moe.codeest.enviewsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import moe.codeest.enviews.ENSearchView;

/**
 * Created by codeest on 16/11/13.
 */

public class SearchActivity extends Activity{

    ENSearchView searchView;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (ENSearchView) findViewById(R.id.view_search);
        btnStart = (Button) findViewById(R.id.btn_search);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.start();
            }
        });
    }
}
