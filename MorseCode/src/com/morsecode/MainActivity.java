package com.morsecode;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.morsecode.model.GestureDetector;
import com.morsecode.model.MorseCodeCalculator;
import com.morsecode.model.ShakeListener;
import com.morsecode.view.CustomPanel;

public class MainActivity extends SherlockActivity {

    private ShakeListener mShaker;
    private MorseCodeCalculator mMorseCal;
    private TextView mResult;
    private String mInput;
    private CustomPanel mInteractionView;
    private int count;
    private boolean mIsEnable, mIsUpperCase;
    public static final String DOT = "0";
    public static final String LINE = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMorseCal = MorseCodeCalculator.getInstance();
        mInput = "";
        mIsEnable = true;

        GestureDetector g = new GestureDetector(this, true);
        mResult = (TextView) findViewById(R.id.textview_result);
        mResult.setOnTouchListener(g);
        mResult.setMovementMethod(new ScrollingMovementMethod());

        g = new GestureDetector(this, false);
        mInteractionView = (CustomPanel) findViewById(R.id.view_interaction);
        mInteractionView.setOnTouchListener(g);

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                if (!mIsEnable) {
                    return;
                }

                vibe.vibrate(100);
                if (mMorseCal != null && !mInput.isEmpty()) {
                    String result = mMorseCal.produce(mInput, mIsUpperCase);
                    if (!result.isEmpty()) {
                        mResult.append(result);
                        mInput = "";
                        count = 0;
                    }
                } else {
                    if (count == 1) {
                        mResult.append("\n");
                        count = 0;
                    } else {
                        mResult.append(" ");
                        ++count;
                    }
                }
            }
        });

        View customNav = LayoutInflater.from(this).inflate(
                R.layout.custom_menu, null);

        CheckBox chEnable = ((CheckBox) customNav
                .findViewById(R.id.checkbox_enable));
        chEnable.setChecked(mIsEnable);
        chEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                mIsEnable = isChecked;
                mInput = "";
            }
        });
        
        CheckBox chCase = ((CheckBox) customNav
                .findViewById(R.id.checkbox_case));
        chCase.setChecked(mIsUpperCase);
        chCase.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                mIsUpperCase = isChecked;
            }
        });

        getSupportActionBar().setCustomView(customNav);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onResume() {
        mShaker.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mShaker.pause();
        super.onPause();
    }

    public String getInput() {
        return mInput;
    }

    public void setInput(String input) {
        mInput = input;
    }

    public TextView getResult() {
        return mResult;
    }
    
    public CustomPanel getInteractionView() {
        return mInteractionView;
    }
}
