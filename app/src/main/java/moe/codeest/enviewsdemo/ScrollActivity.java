package moe.codeest.enviewsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import moe.codeest.enviews.ENScrollView;

/**
 * Created by codeest on 16/11/9.
 */

public class ScrollActivity extends Activity{

    ENScrollView scrollView;
    Button btnSelect;
    Button btnUnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        scrollView = (ENScrollView) findViewById(R.id.view_scroll);
        btnSelect = (Button) findViewById(R.id.btn_select);
        btnUnSelect = (Button) findViewById(R.id.btn_unselect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.select();
            }
        });
        btnUnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.unSelect();
            }
        });
    }
}
