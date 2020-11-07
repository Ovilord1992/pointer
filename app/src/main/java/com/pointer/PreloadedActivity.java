package com.pointer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class PreloadedActivity extends Activity {

    ImageView icCenter,icCenter2, imageAgraf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);


//        icCenter = findViewById(R.id.ic_center);
          imageAgraf = findViewById(R.id.yellow_logo);
//        icCenter2 = findViewById(R.id.imageLogoRight);
//
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.top_wave);
//        icCenter.setAnimation(animation);
//
//
//        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.top_wave1);
//        icCenter2.setAnimation(animation1);
//
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.top_wave);
        imageAgraf.setAnimation(animation2);

        //set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PreloadedActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
                //test
            }
        },1500);
    }

}
