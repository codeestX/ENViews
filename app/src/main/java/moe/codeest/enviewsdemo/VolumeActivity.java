package moe.codeest.enviewsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import moe.codeest.enviews.ENVolumeView;

/**
 * Created by codeest on 16/11/13.
 */

public class VolumeActivity extends Activity{

    ENVolumeView volumeView;
    SeekBar sbVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);
        volumeView = (ENVolumeView) findViewById(R.id.view_volume);
        sbVolume = (SeekBar) findViewById(R.id.sb_volume);

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volumeView.updateVolumeValue(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
