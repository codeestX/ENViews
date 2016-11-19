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
    Button btnSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        scrollView = (ENScrollView) findViewById(R.id.view_scroll);
        btnSwitch = (Button) findViewById(R.id.btn_switch);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollView.isSelected()) {
                    scrollView.unSelect();
                } else {
                    scrollView.select();
                }
            }
        });
    }
}
