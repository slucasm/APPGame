package dsa.eetac.upc.edu.appgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {
    private final int splashTime = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        final SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean registered = sharedPref.getBoolean("registered", false);
        String userName = sharedPref.getString("userName","");
        String password = sharedPref.getString("password","");
        Class dest;
        if (!registered) {
            dest = LoginActivity.class;
        } else {
            dest = PreGame_Activity.class;
        }

        /*Intent intent = new Intent(this, dest);
        startActivity(intent);
        finish();*/

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashScreenActivity.this, dest);
                if(dest == PreGame_Activity.class){
                    intent.putExtra("userName",userName);
                    intent.putExtra("password",password);
                }
                startActivity(intent);
                finish();
            };
        }, splashTime);
    }

}
