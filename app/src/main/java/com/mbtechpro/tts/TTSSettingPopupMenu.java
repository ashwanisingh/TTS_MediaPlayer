package com.mbtechpro.tts;

import android.content.Context;
import android.graphics.Point;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.mbtechpro.tts_mediaplayer.R;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.LANG_AVAILABLE;
import static android.speech.tts.TextToSpeech.LANG_COUNTRY_AVAILABLE;
import static android.speech.tts.TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;
import static android.speech.tts.TextToSpeech.LANG_MISSING_DATA;
import static android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;

/**
 * Created by ashwanisingh on 11/10/17.
 */

public class TTSSettingPopupMenu extends PopupMenu implements View.OnClickListener {

    public static final byte ACCENT = 1;
    public static final byte PITCH = 2;
    public static final byte SPEED = 3;

    private final String lang1 = "En-In";
    private final String lang2 = "En-US";
    private final String lang3 = "En-GB";

    private Context mContext;
    private byte mRequestFor;

    private TTSPopupItemClick mTtsPopupItemClick;
    private PopupWindow popupWindow;


    public TTSSettingPopupMenu(Context context, byte requestFor, View anchor) {
        super(context, anchor);
        mContext = context;
        mRequestFor = requestFor;
        init(anchor);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    TextToSpeech mTts = null;

    private int isLanguageAvailable(Context context, String language, String country) {

        if(mTts == null) {
            mTts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    Log.i("", "");
                }
            });
        }
        return mTts.isLanguageAvailable(new Locale(language+"-"+country));
    }

    private void init(View anchor) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.tts_popup, null);

        TextView item1 = popupView.findViewById(R.id.item1);
        TextView item2 = popupView.findViewById(R.id.item2);
        TextView item3 = popupView.findViewById(R.id.item3);
        TextView item4 = popupView.findViewById(R.id.item4);
        TextView item5 = popupView.findViewById(R.id.item5);

        item1.setVisibility(View.GONE);
        item2.setVisibility(View.GONE);
        item3.setVisibility(View.GONE);
        item4.setVisibility(View.GONE);
        item5.setVisibility(View.GONE);

        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        item4.setOnClickListener(this);
        item5.setOnClickListener(this);


        if(mRequestFor == ACCENT) {
            item1.setText(lang1);
            item2.setText(lang2);
            item3.setText(lang3);
            item1.setVisibility(View.VISIBLE);
            item2.setVisibility(View.VISIBLE);
            item3.setVisibility(View.VISIBLE);
        } else if(mRequestFor == PITCH) {
            item1.setText("Pitch 1");
            item2.setText("Pitch 2");
            item3.setText("Pitch 3");
            item1.setVisibility(View.VISIBLE);
            item2.setVisibility(View.VISIBLE);
            item3.setVisibility(View.VISIBLE);
        } else if(mRequestFor == SPEED) {
//            Fast, Faster, Very fast and Fastest
            item1.setText("Fast");
            item2.setText("Faster");
            item3.setText("Normal");
            item4.setText("Very fast");
            item5.setText("Fastest");
            item1.setVisibility(View.VISIBLE);
            item2.setVisibility(View.VISIBLE);
            item3.setVisibility(View.VISIBLE);
            item4.setVisibility(View.VISIBLE);
            item5.setVisibility(View.VISIBLE);
        }


        isLanguageAvailable(mContext, "", "");


        int popupWidth = (int)dpToPixels(200, mContext);
        int popupHeight = (int)dpToPixels(150, mContext);

        popupWindow = new PopupWindow(
                popupView,
                popupWidth,
                popupHeight);


        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shadow_143418));
        popupWindow.setOutsideTouchable(true);

        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        Point p = new Point();
        p.x = location[0];
        p.y = location[1];

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Displaying the popup at the specified location, + offsets.
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
    }


    public void setTtsPopupItemClick(TTSPopupItemClick ttsPopupItemClick) {
        mTtsPopupItemClick = ttsPopupItemClick;
    }

    @Override
    public void onClick(View view) {

        final String va = ((TextView)view).getText().toString();

        if(mRequestFor == ACCENT) {

            int langStat = -1;

            if(va.equals(lang1)) {
                    langStat = isLanguageAvailable(mContext, "eng", "ind");
            } else if(va.equals(lang2)) {
                langStat = isLanguageAvailable(mContext, "eng", "usa");
            } else if(va.equals(lang3)) {
                langStat = isLanguageAvailable(mContext, "eng", "gb");
            }

            switch (langStat) {
                case LANG_AVAILABLE:
                    Log.i("LANG", "LANG_AVAILABLE");
                    break;

                case LANG_COUNTRY_AVAILABLE:
                    Log.i("LANG", "LANG_COUNTRY_AVAILABLE");
                    break;

                case LANG_COUNTRY_VAR_AVAILABLE:
                    Log.i("LANG", "LANG_COUNTRY_VAR_AVAILABLE");
                    break;

                case LANG_MISSING_DATA:
                    Log.i("LANG", "LANG_MISSING_DATA");

                    break;
                case LANG_NOT_SUPPORTED:

                    Log.i("LANG", "LANG_NOT_SUPPORTED");
                    break;
            }

            Log.i("", "");

        } else if(mTtsPopupItemClick != null) {
            mTtsPopupItemClick.onTTSPopupItemClick(mRequestFor, va);
            popupWindow.dismiss();
        }


    }


    public interface TTSPopupItemClick {
        void onTTSPopupItemClick(byte requestFor, String value);
    }

}
