package com.mykaribe.vendor.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.mykaribe.vendor.R;


public class ProfileImageView extends FrameLayout {
    CircleImageView profileImage;
    TextView profileImageName;

    public void initialize(Context context) {
        inflate(context, R.layout.profile_image_view, this);
        profileImage = (CircleImageView) findViewById(R.id.custom_profile_image);
        profileImageName = (TextView) findViewById(R.id.custom_profile_image_name);
    }

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ProfileImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public void setColorWithName(final String str, final int profileColor) {
        profileImage.setImageDrawable(new ColorDrawable(profileColor));
        profileImageName.setText(str);
    }

    public void setImage(Drawable drawable) {
        profileImage.setImageDrawable(drawable);
        profileImageName.setText("");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

            return super.onTouchEvent(event);

    }

}