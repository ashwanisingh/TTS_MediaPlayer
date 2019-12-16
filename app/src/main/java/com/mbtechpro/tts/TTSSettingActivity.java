package com.mbtechpro.tts;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mbtechpro.tts.discreteSeekbar.DiscreteSeekBar;
import com.mbtechpro.tts.discreteSeekbar.TTSPreference;
import com.mbtechpro.tts_mediaplayer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ashwanisingh on 17/10/17.
 */

public class TTSSettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextView mTtsSpeedVal;
    private ImageView mSpeedVisibilityBtn;

    private DiscreteSeekBar mSpeedSeekbar2;
    private DiscreteSeekBar mPitchSeekbar2;

    private TextView mSpeedDot1, mSpeedDot2, mSpeedDot3, mSpeedDot4, mSpeedDot5;

    private LinearLayout mSpeedSeekbarLayout;
    private LinearLayout mPitchSeekbarLayout;

    private TextView mTtsAccentVal;
    private TextView mTtsPitchVal;

    private ImageView mAccentSelectionVisibilityBtn;
    private ImageView mPitchVisibilityBtn;

    private TextToSpeech textToSpeech;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts_setting);

        mToolbar = findViewById(R.id.toolbar);

        mTtsSpeedVal = findViewById(R.id.ttsSpeedVal);
        mSpeedVisibilityBtn = findViewById(R.id.speedVisibilityBtn);
        mSpeedSeekbar2  = findViewById(R.id.speedSeekbar2);
        mPitchSeekbar2  = findViewById(R.id.pitchSeekbar);
        mSpeedSeekbarLayout = findViewById(R.id.speedSeekbarLayout);
        mPitchSeekbarLayout = findViewById(R.id.pitchSeekbarLayout);

        mSpeedDot1 = findViewById(R.id.speedDot1);
        mSpeedDot2 = findViewById(R.id.speedDot2);
        mSpeedDot3 = findViewById(R.id.speedDot3);
        mSpeedDot4 = findViewById(R.id.speedDot4);
        mSpeedDot5 = findViewById(R.id.speedDot5);


        mTtsAccentVal = findViewById(R.id.ttsAccentVal);
        mTtsPitchVal = findViewById(R.id.ttsPitchVal);
        mAccentSelectionVisibilityBtn = findViewById(R.id.accentSelectionVisibilityBtn);
        mPitchVisibilityBtn = findViewById(R.id.pitchVisibilityBtn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.read_aloud));
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        mToolbar.setLogo(null);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("", "");
        resetUI();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restore:
                TTSPreference.getInstance(getApplicationContext()).clearTTsSetting();

                initTTSForResetting();
//                resetUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void hideSpeedLayout() {
        mSpeedSeekbarLayout.setVisibility(View.GONE);
        mTtsSpeedVal.setVisibility(View.VISIBLE);
        mTtsSpeedVal.setText(TTSPreference.getInstance(this).getSpeedInString());
        mSpeedVisibilityBtn.setImageResource(R.drawable.ic_arrow_drop_down_wrapper);

        mSpeedSeekbar2.setIsHideFloater(false);
        mSpeedSeekbar2.hideFloater();
    }

    private void showSpeedLayout() {
        hidePitchLayout();
        mSpeedSeekbarLayout.setVisibility(View.VISIBLE);
        mTtsSpeedVal.setVisibility(View.GONE);
        mSpeedVisibilityBtn.setImageResource(R.drawable.ic_arrow_drop_up_wrapper);

        mSpeedSeekbar2.setIsHideFloater(true);
        mSpeedSeekbar2.showFloaterFromOutSide(1000);
    }


    private void hidePitchLayout() {
        mPitchSeekbarLayout.setVisibility(View.GONE);
        mTtsPitchVal.setVisibility(View.VISIBLE);
        mTtsPitchVal.setText(""+TTSPreference.getInstance(this).getPinchSeekbarPos()+"%");
        mPitchVisibilityBtn.setImageResource(R.drawable.ic_arrow_drop_down_wrapper);
        mPitchSeekbar2.setIsHideFloater(false);
        mPitchSeekbar2.hideFloater();
    }


    private void showPitchLayout() {
        hideSpeedLayout();
        mTtsPitchVal.setVisibility(View.GONE);
        mPitchSeekbarLayout.setVisibility(View.VISIBLE);
        mPitchVisibilityBtn.setImageResource(R.drawable.ic_arrow_drop_up_wrapper);
        mPitchSeekbar2.setIsHideFloater(true);
        mPitchSeekbar2.showFloaterFromOutSide(1000);
    }


    private final String GS_TIME_TRACKING_CATEGORY = "TTSSettingScreen";



    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    /**
     * Sets Speed Seekbar's dots color
     * @param val
     */
    private void setSpeedDotColor(int val , boolean fromUser) {

        if(!fromUser) {
//            if (val >= 0 && val < 14) {
//                // 0.5x,  val 1
//                val = 1;
//            } else if (val >= 14 && val < 37) {
//                // 0.75x, val 25
//                val = 27;
//            } else if (val >= 37 && val < 63) {
//                // 1x, val 50
//                val = 53;
//            } else if (val >= 63 && val < 87) {
//                // 1.5x, val 75
//                val = 77;
//            } else if (val >= 87 && val < 100) {
//                // 2x, val 100
//                val = 100;
//            }
        }

        if(val >= 0 && val < 14) {
            // 0.5x,  val 1
            mSpeedDot1.setTextColor(Color.parseColor("#006599"));
            mSpeedDot2.setTextColor(Color.parseColor("#dbdbdb"));
            mSpeedDot3.setTextColor(Color.parseColor("#dbdbdb"));
            mSpeedDot4.setTextColor(Color.parseColor("#dbdbdb"));
            mSpeedDot5.setTextColor(Color.parseColor("#dbdbdb"));
        } else if(val >= 14 && val < 37) {
            // 0.75x, val 25
            mSpeedDot1.setTextColor(Color.parseColor("#006599"));
            mSpeedDot2.setTextColor(Color.parseColor("#006599"));
            mSpeedDot3.setTextColor(Color.parseColor("#dbdbdb"));
            mSpeedDot4.setTextColor(Color.parseColor("#dbdbdb"));
            mSpeedDot5.setTextColor(Color.parseColor("#dbdbdb"));
        } else if(val >= 37 && val < 63) {
            // 1x, val 50
            mSpeedDot1.setTextColor(Color.parseColor("#006599"));
            mSpeedDot2.setTextColor(Color.parseColor("#006599"));
            mSpeedDot3.setTextColor(Color.parseColor("#006599"));
            mSpeedDot4.setTextColor(Color.parseColor("#dbdbdb"));
            mSpeedDot5.setTextColor(Color.parseColor("#dbdbdb"));
        } else if(val >= 63 && val < 87) {
            // 1.5x, val 75
            mSpeedDot1.setTextColor(Color.parseColor("#006599"));
            mSpeedDot2.setTextColor(Color.parseColor("#006599"));
            mSpeedDot3.setTextColor(Color.parseColor("#006599"));
            mSpeedDot4.setTextColor(Color.parseColor("#006599"));
            mSpeedDot5.setTextColor(Color.parseColor("#dbdbdb"));
        } else if(val >= 87 && val < 100) {
            // 2x, val 100
            mSpeedDot5.setTextColor(Color.parseColor("#006599"));
            mSpeedDot4.setTextColor(Color.parseColor("#006599"));
            mSpeedDot3.setTextColor(Color.parseColor("#006599"));
            mSpeedDot2.setTextColor(Color.parseColor("#006599"));
            mSpeedDot1.setTextColor(Color.parseColor("#006599"));
        }

        if(!fromUser) {
            mSpeedSeekbar2.setProgress(val);
        }
    }



    private void initTTSForResetting() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                final List<LanguageItem> languageList = new ArrayList<>();
                final Locale[] locales = Locale.getAvailableLocales();

                if (textToSpeech != null) {
                    for (Locale locale : locales) {
                        int res = textToSpeech.isLanguageAvailable(locale);

                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE && locale.getDisplayLanguage().startsWith("Eng")) {

                            int langStat = textToSpeech.isLanguageAvailable(new Locale(locale.getLanguage() + "-" + locale.getCountry()));

                            final LanguageItem item = new LanguageItem();
                            item.country = locale.getCountry();
                            item.language = locale.getLanguage();
                            item.displayName = locale.getDisplayLanguage() + "(" + locale.getDisplayCountry() + ")";
                            item.isExist = langStat == TextToSpeech.LANG_AVAILABLE;

                            if (!languageList.contains(item)) {
                                languageList.add(item);
                            }
                        }
                    }
                }


                final TTSPreference ttsPreference = TTSPreference.getInstance(TTSSettingActivity.this);

                final LanguageItem selectedItem = new LanguageItem();
                selectedItem.country = ttsPreference.getCountry();
                selectedItem.language = ttsPreference.getLanguage();
                selectedItem.displayName = ttsPreference.getDisplayName();

                final int selectedIndex = languageList.indexOf(selectedItem);

                if (selectedIndex != -1) {
                    selectedItem.isSelected = true;
                    selectedItem.isExist = true;

                    LanguageItem seleItem = languageList.get(selectedIndex);
                    selectedItem.displayName = seleItem.displayName;
                    selectedItem.language = seleItem.language;
                    selectedItem.country = seleItem.country;

                    languageList.remove(selectedItem);
                    languageList.add(selectedIndex, selectedItem);

                    ttsPreference.setCountry(languageList.get(selectedIndex).country);
                    ttsPreference.setLanguage(languageList.get(selectedIndex).language);
                    ttsPreference.setDisplayName(languageList.get(selectedIndex).displayName);

                }
                else if(languageList.size() > 0){
                    ttsPreference.setCountry(languageList.get(0).country);
                    ttsPreference.setLanguage(languageList.get(0).language);
                    ttsPreference.setDisplayName(languageList.get(0).displayName);
                }

                resetUI();

            }
        });
    }


    private void playTTS() {
        final String textString = "This is Ashwani, read aloud control test voice.";
        if(textToSpeech == null) {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {

                    if(textToSpeech.isSpeaking()) {
                        textToSpeech.stop();
                    }

                    final TTSPreference ttsPreference = TTSPreference.getInstance(TTSSettingActivity.this);
                    float speed = ttsPreference.getSpeed();
                    float pitch = ttsPreference.getPitch();

                    textToSpeech.setPitch(pitch);
                    textToSpeech.setSpeechRate(speed);

                    String country = ttsPreference.getCountry();
                    String language = ttsPreference.getLanguage();

                    Locale locale = new Locale(language, country);

                    int result = textToSpeech.setLanguage(locale);
                    textToSpeech.speak(textString, TextToSpeech.QUEUE_FLUSH, null);
                }
            });



        } else {
            if(textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }

            final TTSPreference ttsPreference = TTSPreference.getInstance(this);
            float speed = ttsPreference.getSpeed();
            float pitch = ttsPreference.getPitch();

            textToSpeech.setPitch(pitch);
            textToSpeech.setSpeechRate(speed);

            String country = ttsPreference.getCountry();
            String language = ttsPreference.getLanguage();

            Locale locale = new Locale(language, country);

            int result = textToSpeech.setLanguage(locale);

            textToSpeech.speak(textString, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onStop() {
        if(textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }

    public void resetUI(){

        mSpeedSeekbar2.setIndicatorPopupEnabled(true);
        mSpeedSeekbar2.setIsHideFloater(true);

        mPitchSeekbar2.setIndicatorPopupEnabled(true);
        mPitchSeekbar2.setIsHideFloater(true);

        final TTSPreference ttsPreference = TTSPreference.getInstance(TTSSettingActivity.this);

        mTtsSpeedVal.setText(ttsPreference.getSpeedInString());
        mTtsPitchVal.setText(""+ttsPreference.getPinchSeekbarPos()+"%");

        final int progress = TTSPreference.getInstance(this).getSpeedSeekbarPos();
        setSpeedDotColor(progress, false);
        String displayName = ttsPreference.getDisplayName();

        mTtsAccentVal.setText(displayName);


        /*mSpeedVisibilityBtn*/findViewById(R.id.textToSpeechSpeedLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSpeedSeekbarLayout.getVisibility() == View.GONE) {
                    showSpeedLayout();
                } else {
                    hideSpeedLayout();
                }
            }
        });

        /*mAccentSelectionVisibilityBtn*/findViewById(R.id.accentSelectionLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.openTTSAppLanguageSettingScreen(v.getContext());

            }
        });

        /*mPitchVisibilityBtn*/findViewById(R.id.layout_pitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPitchSeekbarLayout.getVisibility() == View.VISIBLE) {
                    hidePitchLayout();
                } else {
                    showPitchLayout();
                }
            }
        });


        mSpeedSeekbar2.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int val, boolean fromUser) {
                setSpeedDotColor(val, fromUser);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                int val = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
//                Log.i("Ashwani", "onStopTrackingTouch");

                int val = seekBar.getProgress();
                int finalPos = val;

                mSpeedSeekbar2.setProgress(finalPos);
                float speedRate;
                if(finalPos == 0) {
                    speedRate=((float) finalPos+1)/100;
                } else {
                    speedRate=((float) finalPos)/100;
                }

                TTSPreference.getInstance(TTSSettingActivity.this).setSpeedSeekbarPos(finalPos);
                TTSPreference.getInstance(TTSSettingActivity.this).setSpeed(speedRate);
                TTSUtil.sendTTSSpeed(TTSSettingActivity.this);

                playTTS();
            }
        });

        final int pitchVal = TTSPreference.getInstance(TTSSettingActivity.this).getPinchSeekbarPos();
        mPitchSeekbar2.setProgress(pitchVal);

        mPitchSeekbar2.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                final int val = seekBar.getProgress();
                float pitch=((float) val+1)/100;
                TTSPreference.getInstance(TTSSettingActivity.this).setPinchSeekbarPos(val);
                TTSPreference.getInstance(TTSSettingActivity.this).setPitch(pitch);
                TTSUtil.sendTTSPitch(TTSSettingActivity.this);

                playTTS();
            }
        });
    }






}
