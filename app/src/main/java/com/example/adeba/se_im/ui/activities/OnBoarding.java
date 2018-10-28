package com.example.adeba.se_im.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adeba.se_im.R;
import com.example.adeba.se_im.ui.adapters.OnBoardingSliderAdapter;

public class OnBoarding extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mLinearLayout;

    private TextView [] mDots;

    private OnBoardingSliderAdapter sliderAdapter;

    private Button prevButton;
    private Button nextButton;

    private  int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        mSlideViewPager = (ViewPager) findViewById(R.id.SliderViewPager);
        mLinearLayout = (LinearLayout) findViewById(R.id.slide_linear_layout);
        nextButton = (Button) findViewById(R.id.nextButton);
        prevButton = (Button) findViewById(R.id.prevButton);

        sliderAdapter = new OnBoardingSliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);
        /**
         * OnClickListener
         */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }
    public static void startIntent(Context context) {
        Intent intent = new Intent(context, OnBoarding.class);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, OnBoarding.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mLinearLayout.removeAllViews();
        for(int i = 0; i < mDots.length; i++) {
            mDots[i] = new  TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextColor(getResources().getColor(R.color.purple_100));
            mLinearLayout.addView(mDots[i]);
        }

        if(mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.purple_100));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

            mCurrentPage = i;

            if(i == 0 ){
                nextButton.setEnabled(true);
                prevButton.setEnabled(false);
                prevButton.setVisibility(View.INVISIBLE);

                nextButton.setText("Next");
                prevButton.setText("");
            } else if (i == mDots.length - 1) {
                nextButton.setEnabled(true);
                prevButton.setEnabled(true);
                prevButton.setVisibility(View.VISIBLE);

                nextButton.setText("Finish");
                prevButton.setText("Back");
            } else {
                nextButton.setEnabled(true);
                prevButton.setEnabled(true);
                prevButton.setVisibility(View.VISIBLE);

                nextButton.setText("Next");
                prevButton.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
