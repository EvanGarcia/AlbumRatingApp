package com.example.evangarcia.lab2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.evangarcia.lab2.MainActivity.EXTRA_MAIN_CURRENT_RATING;

public class SettingsActivity extends AppCompatActivity {


    private static final String EXTRA_RATINGS_RESET_CLICKED = "com.example.garcia.inclasslab2.ratings_reset_clicked";
    private static final String EXTRA_FONT_LARGER_CLICKED = "com.example.garcia.inclasslab2.font_larger_clicked";
    private static final String EXTRA_FONT_SMALLER_CLICKED = "com.example.garcia.inclasslab2.font_smaller_clicked";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Match variables with their xml counterpart
        Button mResetRatingsButton = (Button) findViewById(R.id.ReseRatingsButton);
        Button mFontLargerButton = (Button) findViewById(R.id.FontChangeLargerButton);
        Button mFontSmallerButton = (Button) findViewById(R.id.FontChangeSmallerButton);

        //If large layout only allow landscape view
        if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }

        //Get CurrentTextSize from MainActivity
        Intent data = getIntent();

        final float currentTextTwo = data.getFloatExtra(EXTRA_MAIN_CURRENT_RATING, 0);



        mResetRatingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data = new Intent();
                data.putExtra(EXTRA_RATINGS_RESET_CLICKED, true);
                setResult(RESULT_OK, data);
            }
        });

        mFontLargerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentTextTwo >= 70)
                {
                    Toast.makeText(SettingsActivity.this, R.string.TextTooLarge, Toast.LENGTH_LONG).show();
                }

                else
                {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_FONT_LARGER_CLICKED, true);
                    setResult(RESULT_OK, data);
                }
            }
        });


        mFontSmallerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentTextTwo <= 30)
                {
                    Toast.makeText(SettingsActivity.this, R.string.TextTooSmall, Toast.LENGTH_LONG).show();
                }

                Intent data = new Intent();
                data.putExtra(EXTRA_FONT_SMALLER_CLICKED, true);
                setResult(RESULT_OK, data);
            }
        });
    }


    public static boolean wasRatingsResetClicked(Intent result)
    {
        return result.getBooleanExtra(EXTRA_RATINGS_RESET_CLICKED, false);
    }

    public static boolean wasFontLargerClicked(Intent result)
    {
        return result.getBooleanExtra(EXTRA_FONT_LARGER_CLICKED, false);
    }


    public static boolean wasFontSmallerClicked(Intent result)
    {
        return result.getBooleanExtra(EXTRA_FONT_SMALLER_CLICKED, false);
    }
}
