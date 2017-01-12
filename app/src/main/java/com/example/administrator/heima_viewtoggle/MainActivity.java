package com.example.administrator.heima_viewtoggle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.heima_viewtoggle.Ui.ToggleView;

public class MainActivity extends AppCompatActivity {

    private ToggleView toggleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();

    }

    private void initUi() {
        toggleview = (ToggleView) findViewById(R.id.toggleview);
//        toggleview.setSwitchBackgroundResource(R.drawable.switch_background);
//        toggleview.setSlideButtonResource(R.drawable.slide_button);
//        toggleview.setSwitchState(true);
        toggleview.setOnSwitchStateChangeListener(new ToggleView.OnSwitchStateChangeListener() {

            @Override
            public void onStateChange(boolean isSwitch) {

                Toast.makeText(getApplicationContext(), "isSwitch:" + isSwitch, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
