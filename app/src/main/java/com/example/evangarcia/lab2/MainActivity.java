package com.example.evangarcia.lab2;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SETTINGS = 0;

    public static final String EXTRA_MAIN_CURRENT_RATING =
            "com.example.garcia.inclasslab2.main_current_rating";

    private ImageView mAlbumImage;
    private RatingBar mRatingBar;
    private TextView mAlbumNameTextView;
    private TextView mAlbumYearTextView;
    private TextView mAlbumNameLabel;
    private TextView mAlbumYearLabel;
    private Button mAlbumNextButton;
    private Button mAlbumPreviousButton;
    private int mCurrentAlbumIndex = 0;
    private float mCurrentTextSize = 0.0f;

    //Declare Album Objects
    private final Album[] mAlbum = new Album[]{

            new Album(R.string.whateverString, R.drawable.whatever, R.string.TSix, 5f, R.string.AccessabilityWhatever ),
            new Album(R.string.favouriteString, R.drawable.favourite, R.string.TSeven, 5f, R.string.AccessabilityFavourite),
            new Album(R.string.humbugString, R.drawable.humbug, R.string.TNine, 5f, R.string.AccessabilityHumbug),
            new Album(R.string.suckString, R.drawable.suckit, R.string.TEl, 5f, R.string.AccessabilitySuck ),
            new Album(R.string.amString, R.drawable.am, R.string.TTH, 5f, R.string.AccessabilityAM ),
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Match variables with their xml counterpart
        mAlbumNameTextView = (TextView) findViewById(R.id.NameTextView);
        mAlbumYearTextView = (TextView) findViewById(R.id.YearTextView);
        mAlbumImage = (ImageView) findViewById(R.id.AlbumImageView);
        mAlbumNextButton = (Button) findViewById(R.id.AlbumNextButton);
        mAlbumPreviousButton = (Button) findViewById(R.id.AlbumPreviousButton);
        mRatingBar = (RatingBar) findViewById(R.id.RatingBarView);
        mAlbumNameLabel = (TextView) findViewById(R.id.nameLabelTextView);
        mAlbumYearLabel = (TextView) findViewById(R.id.yearLabelTextView);

        //Initialize fields
        mAlbumImage.setImageResource(mAlbum[mCurrentAlbumIndex].getmImageResId());
        mAlbumImage.setContentDescription(getString(mAlbum[mCurrentAlbumIndex].getmContentDescriptor()));
        mAlbumNameTextView.setText(mAlbum[mCurrentAlbumIndex].getmNameResId());
        mAlbumYearTextView.setText(mAlbum[mCurrentAlbumIndex].getmYear());
        mRatingBar.setRating(mAlbum[mCurrentAlbumIndex].getmRatingId());

        mCurrentTextSize = mAlbumNameTextView.getTextSize();

        //If layout is large(tablet) then only allow landscape orientation
        if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }

        //start of sharedPreferences method for restoring saved data
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {

            //key: getString will obtain actual string resource points to, append "_rating"
            //value: the contact's current rating (float)
            mAlbum[i].setmRatingId(sharedPref.getFloat(getString(mAlbum[i].getmNameResId()) + "_rating", 0.0f));
        }

        update();



        mAlbumNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //% ensures we wrap back to index 0
                mCurrentAlbumIndex = (mCurrentAlbumIndex + 1) % 5;

                //Call animations
                RotateImageView();
                FadeInAnimation();
                ShakeAnimation();

                //update all text fields and the image view based on current state
                update();
            }
        });

        //define the behavior of the Next Button (go to previous contact)
        mAlbumPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentAlbumIndex = (mCurrentAlbumIndex - 1);

                //ensures we wrap back to index 8 (instead of -1)
                if (mCurrentAlbumIndex == -1)
                    mCurrentAlbumIndex = 4;

                //Call animations
                RotateImageView();
                FadeInAnimation();
                ShakeAnimation();
                //update all text fields and the image view based on current state
                update();

            }
        });

        //Set new rating when rating is changed on rating bar
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {


                mAlbum[mCurrentAlbumIndex].setmRatingId(v);
                mRatingBar.setRating(mAlbum[mCurrentAlbumIndex].getmRatingId());


            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Save shared preferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        for (int i = 0; i < 5; i++) {
            //key: getString will obtain actual string resource points to, append "_location"
            //value: the contact's current rating (float)
            editor.putFloat(getString(mAlbum[i].getmNameResId()) + "_rating", mAlbum[i].getmRatingId());
        }

        //commit edits to persistent storage (apply does this in the background)
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //Pass currentTextSize to SettingsActivity
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);

            float CurrentFontSize = mAlbumNameTextView.getTextSize();

            i.putExtra(EXTRA_MAIN_CURRENT_RATING, CurrentFontSize);

            startActivityForResult(i, REQUEST_CODE_SETTINGS);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        //Call certain functions if their buttons were clicked in SettingsActivity
        if (requestCode == REQUEST_CODE_SETTINGS) {
            if (data == null)
                return;

            if (SettingsActivity.wasRatingsResetClicked(data))
                reset();

            if(SettingsActivity.wasFontLargerClicked(data))
                makeFontLarger();

            if(SettingsActivity.wasFontSmallerClicked(data))
                makeFontSmaller();

        }


    }

    //Set all ratings back to 0
    private void reset() {

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        for(int i = 0; i < 5; i++)
        {
            mAlbum[i].setmRatingId(0.0f);
            mRatingBar.setRating(mAlbum[i].getmRatingId());
            editor.putFloat(getString(mAlbum[i].getmNameResId()) + "_rating", mAlbum[i].getmRatingId());
        }

        //commit edits to persistent storage (apply does this in the background)
        editor.apply();

        Toast.makeText(this, R.string.reset_clicked, Toast.LENGTH_LONG).show();
    }

    //Make all font larger
    private void makeFontLarger() {

        if (mAlbumNameTextView.getTextSize() >= 70) {
            Toast.makeText(this, R.string.TextTooLarge, Toast.LENGTH_LONG).show();
        } else {
            mAlbumNameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumNameLabel.getTextSize() + 10);
            mAlbumYearLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumYearLabel.getTextSize() + 10);
            mAlbumNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumNameTextView.getTextSize() + 10);
            mAlbumYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumYearTextView.getTextSize() + 10);
            mAlbumNextButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumNextButton.getTextSize() + 5);
            mAlbumPreviousButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumPreviousButton.getTextSize() + 5);

        }
    }

    //Make all font smaller
    private void makeFontSmaller(){

        if(mAlbumNameTextView.getTextSize() <= 30 )
        {
            Toast.makeText(this, R.string.TextTooSmall, Toast.LENGTH_LONG).show();
        }
        else
        {
            mAlbumNameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumNameLabel.getTextSize() - 10);
            mAlbumYearLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumYearLabel.getTextSize() - 10);
            mAlbumNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumNameTextView.getTextSize() - 10);
            mAlbumYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumYearTextView.getTextSize() - 10);
            mAlbumNextButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumNextButton.getTextSize() - 5);
            mAlbumPreviousButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAlbumPreviousButton.getTextSize() - 5);
        }


    }


    private void update() {
        //Update Fields
        mAlbumImage.setImageResource(mAlbum[mCurrentAlbumIndex].getmImageResId());
        mAlbumNameTextView.setText(mAlbum[mCurrentAlbumIndex].getmNameResId());
        mAlbumYearTextView.setText(mAlbum[mCurrentAlbumIndex].getmYear());
        mRatingBar.setRating(mAlbum[mCurrentAlbumIndex].getmRatingId());


    }

    //Rotation Animation
    private void RotateImageView()
    {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500); //Put desired duration per anim cycle here, in millisecon

        mAlbumImage.startAnimation(anim);
    }

    //FadeIn animation
    private void FadeInAnimation()
    {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);


        mAlbumNameLabel.startAnimation(fadeIn);
        mAlbumYearLabel.startAnimation(fadeIn);
        mAlbumNameTextView.startAnimation(fadeIn);
        mAlbumYearTextView.startAnimation(fadeIn);
        mAlbumNextButton.startAnimation(fadeIn);
        mAlbumPreviousButton.startAnimation(fadeIn);

    }

    //Shake animation
    private void ShakeAnimation()
    {
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.shake);

        mAlbumNextButton.startAnimation(animation1);
        mAlbumPreviousButton.startAnimation(animation1);

    }


}