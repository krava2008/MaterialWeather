package com.krava.dagger2test.presentation.view.behavoir;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.devspark.robototextview.widget.RobotoTextView;
import com.krava.dagger2test.R;

/**
 * Created by krava2008 on 23.10.16.
 */

public class MainWeatherTempBehavior extends CoordinatorLayout.Behavior<RobotoTextView> {
    private Context context;
    private float mCustomFinalYPosition;
    private float mCustomStartXPosition;
    private float mCustomStartToolbarPosition;
    private float mCustomStartHeight;
    private float mCustomFinalHeight;
    private float mainIconMaxSize;
    private float mStartToolbarPosition;
    private float mChangeBehaviorPoint;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mStartXPosition;
    private int mFinalXPosition;


    public MainWeatherTempBehavior(Context context, AttributeSet attrs) {
        this.context = context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeatherMainIconBehavior);
            mCustomFinalYPosition = a.getDimension(R.styleable.WeatherMainIconBehavior_finalYPosition, 0);
            mCustomStartXPosition = a.getDimension(R.styleable.WeatherMainIconBehavior_startXPosition, 0);
            mCustomStartToolbarPosition = a.getDimension(R.styleable.WeatherMainIconBehavior_startToolbarPosition, 0);
            mCustomStartHeight = a.getDimension(R.styleable.WeatherMainIconBehavior_startHeight, 0);
            mCustomFinalHeight = a.getDimension(R.styleable.WeatherMainIconBehavior_finalHeight, 0);

            a.recycle();
        }

        init();
    }

    private void init() {
        bindDimensions();
    }

    private void bindDimensions() {
        mainIconMaxSize = context.getResources().getDimension(R.dimen.image_width);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RobotoTextView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RobotoTextView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (mStartToolbarPosition);
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;


        child.setX(mFinalXPosition + (mStartXPosition - mFinalXPosition) * expandedPercentageFactor);
        child.setTextSize(18f + 64f * expandedPercentageFactor);

        if (expandedPercentageFactor < mChangeBehaviorPoint) {
            float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight()/2);
            child.setY(mStartYPosition - distanceYToSubtract);

            float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight - heightToSubtract);
            lp.height = (int) (mStartHeight - heightToSubtract);
            child.setLayoutParams(lp);
        } else {
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (mStartHeight/2);
            child.setY(mStartYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight);
            lp.height = (int) (mStartHeight);
            child.setLayoutParams(lp);
        }
        return true;
    }

    private void maybeInitProperties(RobotoTextView child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (dependency.getY());

        if (mFinalYPosition == 0)
            mFinalYPosition = (dependency.getHeight() /2);

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (mStartXPosition == 0)
            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));

        if (mFinalXPosition == 0)
            mFinalXPosition = context.getResources().getDimensionPixelOffset(R.dimen.weather_temp_final_x) + ((int) mCustomFinalHeight / 2);

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY();

        if (mChangeBehaviorPoint == 0) {
            mChangeBehaviorPoint = (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition));
        }
    }
}
