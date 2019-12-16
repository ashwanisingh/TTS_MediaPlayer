package com.mbtechpro.tts.discreteSeekbar;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ashwanisingh on 13/10/17.
 */

public class TTSPreference {

    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;

    private static TTSPreference mTTSPreference;

    private final String DISPLAY_NAME = "displayName";
    private final String LANGUAGE = "language";
    private final String COUNTRY = "country";
    private final String PITCH = "pitch";
    private final String SPEED = "speed";
    private final String SPEED_SEEKBAR_POS = "speedSeekbarPos";
    private final String PINCH_SEEKBAR_POS = "pinchSeekbarPos";
    private final String IS_TTS_SERVICE_RUNNING = "isTtsInitialised";

    private TTSPreference(Context context) {
        mSharedPreference = context.getSharedPreferences("TTSPref.xml", Context.MODE_PRIVATE);
        mEditor = mSharedPreference.edit();
    }

    public static TTSPreference getInstance(Context context) {
        if(mTTSPreference == null) {
            mTTSPreference = new TTSPreference(context);
        }

        return mTTSPreference;
    }

    public void setLanguage(String accent) {
        mEditor.putString(LANGUAGE, accent);
        mEditor.commit();
    }

    public void setCountry(String country) {
        mEditor.putString(COUNTRY, country);
        mEditor.commit();
    }

    public void setDisplayName(String displayName) {
        mEditor.putString(DISPLAY_NAME, displayName);
        mEditor.commit();
    }

    public void setPitch(float pitch) {
        mEditor.putFloat(PITCH, pitch);
        mEditor.commit();
    }

    public void setSpeed(float speed) {
        mEditor.putFloat(SPEED, speed);
        mEditor.commit();
    }

    public void setSpeedSeekbarPos(int pos) {
        mEditor.putInt(SPEED_SEEKBAR_POS, pos);
        mEditor.commit();
    }

    public void setPinchSeekbarPos(int pos) {
        mEditor.putInt(PINCH_SEEKBAR_POS, pos);
        mEditor.commit();
    }

    public String getLanguage() {
        return mSharedPreference.getString(LANGUAGE, "en");
    }

    public String getCountry() {
        return mSharedPreference.getString(COUNTRY, "IN");
    }

    public String getDisplayName() {
        return mSharedPreference.getString(DISPLAY_NAME, "");
    }

    public float getPitch() {
        return mSharedPreference.getFloat(PITCH, 1.0f);
    }

    public float getSpeed() {
        return mSharedPreference.getFloat(SPEED, 1.0f);
    }

    public String getSpeedInString() {
        int val = getSpeedSeekbarPos();
//        if(val >= 0 && val < 14) {
//            // 0.5x,  val 1
//            return "0.5x";
//        } else if(val >= 14 && val < 37) {
//            // 0.75x, val 25
//            return "0.75x";
//        } else if(val >= 37 && val < 63) {
//            // 1x, val 50
//            return "1x";
//        } else if(val >= 63 && val < 87) {
//            // 1.5x, val 75
//            return "1.5x";
//        } else if(val >= 87 && val <= 100) {
//            // 2x, val 100
//            return "2x";
//        }
//        return ""+val+"x";

        return val+"";
    }


    public int getSpeedSeekbarPos() {
        return mSharedPreference.getInt(SPEED_SEEKBAR_POS, 97);
    }

    public int getPinchSeekbarPos() {
        return mSharedPreference.getInt(PINCH_SEEKBAR_POS, 100);
    }

    public void setTTSServiceRunning(boolean isTTSServiceRunning) {
        mEditor.putBoolean(IS_TTS_SERVICE_RUNNING, isTTSServiceRunning);
        mEditor.commit();
    }

    public boolean isTTSServiceRunning() {
        return mSharedPreference.getBoolean(IS_TTS_SERVICE_RUNNING, false);
    }

    public void clearTTsSetting(){
        mEditor.clear();
        mEditor.commit();
    }


}
