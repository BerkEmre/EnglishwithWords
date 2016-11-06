package com.antika.berk.engcardbegin;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run()
            {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this,GameActivity.class));
                SplashScreen.this.finish();
            }
        }, Integer.parseInt(getString(R.string.splash_screen_delay_MS)));
    }
}
