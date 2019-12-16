package com.mbtechpro.tts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mbtechpro.tts.discreteSeekbar.TTSPreference;
import com.mbtechpro.tts_mediaplayer.R;


/**
 * Created by ashwanisingh on 13/10/17.
 */

public class TTSView extends LinearLayout {

    private ImageButton mPlayPauseBtn;
    private ProgressBar mProgressBar;
    /*private Button mPrevBtn;
    private Button mNextBtn;
    private Button mStopBtn;
    private Button mForwardFewSecBtn;
    private Button mBackwardFewSecBtn;
    private SeekBar mediaProgressBar;*/

    private String mSpeakWord;
    private String mArticleId;


    private boolean isPlaying = false;
    private boolean isPaused = false;




    public TTSView(Context context) {
        super(context);
        init(context);
    }

    public TTSView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TTSView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }


    private void setPlayPauseBtnText(int playOrPause) {
        mPlayPauseBtn.setImageResource(playOrPause);
    }


    private void init(Context context) {

        final View view = View.inflate(context, R.layout.tts_layout, this);

        mPlayPauseBtn = view.findViewById(R.id.playPauseButton);
        mProgressBar = view.findViewById(R.id.progressBar);

        /*mNextBtn = (Button) view.findViewById(R.id.nextButton);
        mPrevBtn = (Button) view.findViewById(R.id.prevButton);
        mStopBtn = (Button) view.findViewById(R.id.stopButton);
        mForwardFewSecBtn = (Button) view.findViewById(R.id.forwardFewSecButton);
        mBackwardFewSecBtn = (Button) view.findViewById(R.id.backwardFewSecButton);
        mediaProgressBar = (SeekBar) view.findViewById(R.id.mediaProgress);
        */

        setPlayPauseBtnText(R.mipmap.ic_audio);

        /*this.mediaProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //seekToPointInPlayback(progress);
                }
            }
        });*/


        mPlayPauseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSpeakWord != null && !TextUtils.isEmpty(mSpeakWord)) {

                    if(!isPlaying) {
                        TTSUtil.init(getContext(), mSpeakWord, mArticleId);
                    } else if(isPaused) {
                        TTSUtil.play(getContext());
                    } else {
                        TTSUtil.pause(getContext());
                    }
                }

            }
        });

        /*mNextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.stop(getContext());
            }
        });

        mPrevBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.previous(getContext());
            }
        });

        mStopBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.stop(getContext());
            }
        });

        mForwardFewSecBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.moveForwardFewSecond(getContext());
            }
        });

        mBackwardFewSecBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.moveBackwardFewSecond(getContext());
            }
        });*/

    }


    public void setSpeakWord(String speakWord, String articleId) {
        String txt = PlainTextParser.cleanTagPerservingLineBreaks(speakWord);
        txt = PlainTextParser.removeExtendedChars(txt);
        txt = PlainTextParser.removeUrl(txt);
        txt = Html.fromHtml(txt).toString();
        txt = txt.replaceAll("-", " ");
        mSpeakWord = txt;
        mArticleId = articleId;

        if(TTSPreference.getInstance(getContext()).isTTSServiceRunning()) {
            TTSUtil.sendCheckPlayingRequest(getContext(), articleId);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((mMessageReceiver), new IntentFilter(TTSUtil.INTENT_FILTER));
    }

    @Override
    protected void onDetachedFromWindow() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onDetachedFromWindow();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("KBR", "Got message");
            handleMessage(intent);
        }
    };


    private void handleMessage(Intent msg) {
        final Bundle data = msg.getExtras();
        if(data == null) {
            return;
        }
        switch (data.getInt(TTSUtil.COMMAND, 0)) {
            case TTSUtil.UPDATE_PROGRESS:
                // TODO,
                break;
            case TTSUtil.INITIALIZING:
                mPlayPauseBtn.setVisibility(INVISIBLE);
                mProgressBar.setVisibility(VISIBLE);
                // TODO,
                break;
            case TTSUtil.INITIALISED:
                mPlayPauseBtn.setVisibility(VISIBLE);
                mProgressBar.setVisibility(INVISIBLE);
                setIsPlaying(true);
                setPlayPauseBtnText(R.drawable.ic_pause_wrapper);
                Log.i("Ashwani", "TTSView INITIALISED");
                // TODO,
                break;
            case TTSUtil.IS_PLAYING:
                boolean isPlaying = data.getBoolean("isTtsPlaying");
                if(isPlaying) {
                    String state = data.getString("state");
                    if(state.equalsIgnoreCase("playing")) {
                        setIsPaused(false);
                        setIsPlaying(true);
                        setPlayPauseBtnText(R.drawable.ic_pause_wrapper);
                        Log.i("Ashwani", "TTSView IS_PLAYING if if");
                    } else if(state.equalsIgnoreCase("pause")) {
                        setIsPaused(true);
                        setIsPlaying(true);
                        setPlayPauseBtnText(R.mipmap.ic_audio);
                    }
                } else {
//                    setPlayPauseBtnText("Play");
                    setIsPaused(false);
                    setIsPlaying(false);
                    setPlayPauseBtnText(R.mipmap.ic_audio);
                }

                setIsPlaying(isPlaying);
                Log.i("", "");
                // TODO,
                break;
            case TTSUtil.INIT_FAILED:
                Log.i("", "");
                setIsPlaying(false);
                setIsPaused(false);
                setPlayPauseBtnText(R.mipmap.ic_audio);
                mPlayPauseBtn.setVisibility(VISIBLE);
                mProgressBar.setVisibility(INVISIBLE);
                // TODO,
                break;
            case TTSUtil.LANG_NOT_SUPPORTED:
                Log.i("", "");
                setIsPlaying(false);
                setIsPaused(false);
                setPlayPauseBtnText(R.mipmap.ic_audio);
                mPlayPauseBtn.setVisibility(VISIBLE);
                mProgressBar.setVisibility(INVISIBLE);
                // TODO,
                break;
            case TTSUtil.STOPPED:
                Log.i("", "");
                setIsPlaying(false);
                //setPlayPauseBtnText("Play");
                setPlayPauseBtnText(R.mipmap.ic_audio);
                mPlayPauseBtn.setVisibility(VISIBLE);
                mProgressBar.setVisibility(INVISIBLE);
                // TODO,
                break;
            case TTSUtil.COMPLETED:
                Log.i("", "");
                setIsPlaying(false);
                setIsPaused(false);
                //setPlayPauseBtnText("Play");
                setPlayPauseBtnText(R.mipmap.ic_audio);
                // TODO,
                break;
            case TTSUtil.PAUSED:
                Log.i("", "");
                setIsPaused(true);
                setIsPlaying(true);
                //setPlayPauseBtnText("Play");
                setPlayPauseBtnText(R.mipmap.ic_audio);
                mPlayPauseBtn.setVisibility(VISIBLE);
                mProgressBar.setVisibility(INVISIBLE);
                // TODO,
                break;
            case TTSUtil.PLAY:
                Log.i("", "");
                setIsPlaying(true);
                setIsPaused(false);
                //setPlayPauseBtnText("Play");
                setPlayPauseBtnText(R.drawable.ic_pause_wrapper);
                mPlayPauseBtn.setVisibility(VISIBLE);
                mProgressBar.setVisibility(INVISIBLE);
                Log.i("Ashwani", "TTSView PLAY");
                // TODO,
                break;
        }
    }





    public void setSpeech(String speech) {

    }


}
