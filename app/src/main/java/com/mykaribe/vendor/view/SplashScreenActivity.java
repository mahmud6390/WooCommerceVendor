package com.mykaribe.vendor.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.mykaribe.vendor.R;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.PreferenceHelper;

/**
 * Created by USER on 18/1/2018.
 */
public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_DELAY = 3000;
    private Handler myHandler=new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_screen);
        goToMainScreen();
    }

    private void goToMainScreen() {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreenActivity.this.finish();
                int vendorId= PreferenceHelper.getInt(Constant.VENDOR_ID,0);
                String userName=PreferenceHelper.getString(Constant.USER_NAME,"");
                String password=PreferenceHelper.getString(Constant.PASSWORD,"");
                Logger.debugLog("SplashScreenActivity","goToMainScreen>>>vendorId:"+vendorId);
                if(vendorId!=0 && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }

            }
        },SPLASH_SCREEN_DELAY);

    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
