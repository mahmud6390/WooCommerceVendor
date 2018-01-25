package com.mykaribe.vendor.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mykaribe.vendor.R;
import com.mykaribe.vendor.controller.LoginController;
import com.mykaribe.vendor.controller.ProductIdController;
import com.mykaribe.vendor.listener.ILoginUiCallback;
import com.mykaribe.vendor.listener.IProductIdListCallback;
import com.mykaribe.vendor.model.Vendor;
import com.mykaribe.vendor.utils.App;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by USER on 17/1/2018.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,ILoginUiCallback,IProductIdListCallback {
    private EditText editTextUserName,editTextPassword;
    private Button buttonLogin;
    private ImageView imageViewPasswordHideShow;
    private ProgressBar progressBar;
    private boolean isShowPassword=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        editTextUserName=(EditText)findViewById(R.id.user_name_txt);
        editTextPassword=(EditText)findViewById(R.id.password_txt);
        buttonLogin=(Button)findViewById(R.id.login_btn);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        buttonLogin.setOnClickListener(this);
        imageViewPasswordHideShow=(ImageView)findViewById(R.id.password_hide_show);
        imageViewPasswordHideShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                if(!isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    new LoginController().sendLoginInfo(editTextUserName.getText().toString(),editTextPassword.getText().toString(),LoginActivity.this);

                }
                break;
            case R.id.password_hide_show:
                if(isShowPassword){
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewPasswordHideShow.setImageResource(R.drawable.password_hide);
                }else{
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewPasswordHideShow.setImageResource(R.drawable.password_show);
                }
                isShowPassword=!isShowPassword;
                break;

        }
    }
    private boolean isEmpty(){
        boolean isEmpty=false;
        if(editTextUserName.getText().toString().isEmpty()){
            editTextUserName.setError("user name empty");
            isEmpty=true;
        }
        if(editTextPassword.getText().toString().isEmpty()){
            editTextPassword.setError("password empty");
            isEmpty=true;
        }
        return isEmpty;
    }

    @Override
    public void onLoginSuccess(Vendor vendor) {
        progressBar.setVisibility(View.INVISIBLE);
        Logger.debugLog("LOGIN_RESPONSE","onLoginSuccess>>>"+vendor.getId()+":name:"+vendor.getNickName()+":"+vendor.getEmail()+":"+vendor.getImageUrl());
        PreferenceHelper.putInt(Constant.VENDOR_ID,vendor.getId());
        PreferenceHelper.putString(Constant.USER_NAME,vendor.getUserName());
        PreferenceHelper.putString(Constant.PASSWORD,vendor.getPassword());
        PreferenceHelper.putString(Constant.VENDOR_NAME,vendor.getNickName());
        PreferenceHelper.putString(Constant.VENDOR_EMAIL,vendor.getEmail());
        PreferenceHelper.putString(Constant.VENDOR_IMAGE,vendor.getImageUrl());
        int vendorId= PreferenceHelper.getInt(Constant.VENDOR_ID,0);
        Logger.debugLog("LOGIN_RESPONSE","onLoginSuccess>>>vendorId:"+vendorId);
        new ProductIdController().getProductIdList(vendor.getUserName(),vendor.getPassword(),this);

    }

    @Override
    public void onLoginFailed() {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, R.string.faile_login,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessList(ArrayList<Integer> productId) {
        Logger.debugLog("APP","onSuccessList>>>>productId:"+productId);
        App.setProductIdList(productId);
        startActivity(new Intent(this,HomeActivity.class));
        LoginActivity.this.finish();
    }

    @Override
    public void onFailed() {

    }
}
