package com.mbtechpro.tts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mbtechpro.tts.discreteSeekbar.TTSPreference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jedi.option.Option;

import static java.util.Arrays.asList;
import static jedi.functional.FunctionalPrimitives.firstOption;
import static jedi.functional.FunctionalPrimitives.isEmpty;

/**
 * Created by ashwanisingh on 13/10/17.
 */

public class SpeechService extends Service implements TextToSpeech.OnInitListener {


    private TextToSpeech textToSpeech;
    private Handler uiHandler;

    private boolean ttsAvailable = false;
    public static final Boolean IS_NOOK_TOUCH = "NOOK".equals(Build.PRODUCT);

    private Map<String, TTSPlaybackItem> ttsItemPrep = new HashMap<>();


    private TelephonyManager telephonyManager;
    private PowerManager powerManager;
    private AudioManager audioManager;

    private boolean mIsTTSInitialised = false;
    private String mPlayingArticleId;
    private String mPlayingFileName;

    private String mSpeechText;


    private boolean isFromMap = true;
    private TTSPlaybackQueue ttsPlaybackItemQueue;
    private TTSPlaybackQueue ttsPlaybackItemMap;
    private boolean isPauseRequested = false;


    private void initSystemServices() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (isFromMap) {
            ttsPlaybackItemMap = new TTSPlaybackQueue();
        } else {
            ttsPlaybackItemQueue = new TTSPlaybackQueue();
        }


    }


    @Override
    public void onCreate() {
        super.onCreate();
        initSystemServices();

        uiHandler = new Handler();

        TTSPreference.getInstance(this).setTTSServiceRunning(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uiHandler.removeCallbacksAndMessages(null);

        String actionKey = intent.getStringExtra(TTSUtil.ACTION_KEY);
        switch (actionKey) {
            case TTSUtil.ACTION_INIT:
                mSpeechText = intent.getStringExtra(TTSUtil.SPEECH_TEXT);
                final String newArticleId = intent.getStringExtra("articleId");

                if (!mIsTTSInitialised) {
                    sendUpdateMessage(null, TTSUtil.INITIALIZING);
                    mPlayingArticleId = newArticleId;
                    textToSpeech = new TextToSpeech(getApplicationContext(), this);
                } else if (mPlayingArticleId != null && !mPlayingArticleId.equals(newArticleId)) {
                    TTSPlaybackItem itemPause = this.ttsPlaybackItemMap.getPlaybackItem(mPlayingFileName);
                    if (itemPause != null) {
                        MediaPlayer mediaPlayerPause = itemPause.getMediaPlayer();
                        if (mediaPlayerPause != null && mediaPlayerPause.isPlaying()) {
                            mediaPlayerPause.pause();
//                            sendUpdateMessage(null, TTSUtil.PAUSED);
                        }
                    }

                    sendUpdateMessage(null, TTSUtil.INITIALIZING);
                    stopTextToSpeech();
                    uiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startTextToSpeech(false, newArticleId);
                        }
                    }, 1000);
                } else if (ttsIsRunning()) {
                    Log.i("", "");
                    // DOING PAUSE IF ALREADY PLAYING

                    TTSPlaybackItem itemPause = this.ttsPlaybackItemMap.getPlaybackItem(mPlayingFileName);

                    if (itemPause != null) {
                        MediaPlayer mediaPlayerPause = itemPause.getMediaPlayer();
                        if (mediaPlayerPause != null && mediaPlayerPause.isPlaying()) {
                            mediaPlayerPause.pause();
//                            sendUpdateMessage(null, TTSUtil.PAUSED);
                        }
                    }
                    sendUpdateMessage(null, TTSUtil.INITIALIZING);

//                        stopTextToSpeech();

                    uiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startTextToSpeech(false, newArticleId);
                        }
                    }, 1000);


                } else {
                    sendUpdateMessage(null, TTSUtil.INITIALIZING);
                    startTextToSpeech(false, newArticleId);
                }


                break;
            case TTSUtil.ACTION_CHECK_PLAYING:
                final String articleId = intent.getStringExtra("articleId");
                Bundle bundle = new Bundle();
                boolean isPlaying = false;

//                String playingArticleId = getArticleIdFromFilename(mPlayingFileName);

                if (mPlayingFileName != null && mPlayingArticleId != null && articleId != null && mPlayingArticleId.equals(articleId)) {
                    TTSPlaybackItem item = this.ttsPlaybackItemMap.getPlaybackItem(mPlayingFileName);
                    if (item != null && item.getMediaPlayer() != null && item.getMediaPlayer().isPlaying()) {
                        isPlaying = true;
                        bundle.putString("state", "playing");
                    } else if (item != null && item.getMediaPlayer() != null) {
                        isPlaying = true;
                        bundle.putString("state", "pause");
                    }
                }
                bundle.putBoolean("isTtsPlaying", isPlaying);
                sendUpdateMessage(bundle, TTSUtil.IS_PLAYING);

                Log.i("Ashwani", "ACTION_CHECK_PLAYING   ArticleId :: "+articleId+" # isPlaying :: "+isPlaying);

                break;
            case TTSUtil.ACTION_PLAY:
                final TTSPlaybackItem item = this.ttsPlaybackItemMap.getPlaybackItem(mPlayingFileName);

                if (item != null) {
                    MediaPlayer mediaPlayer = item.getMediaPlayer();
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        isPauseRequested = false;
                        mediaPlayer.start();
                        sendUpdateMessage(null, TTSUtil.PLAY);
                        Log.i("Ashwani", "ACTION_PLAY  if if play");
                    } else {
                        Log.i("Ashwani", "ACTION_PLAY  if else mediaPlayer null or not play");
                    }

                } else {
                    Log.i("Ashwani", "ACTION_PLAY  else stop");
                    stopTextToSpeech();
                }

                break;
            case TTSUtil.ACTION_PAUSE:
                final TTSPlaybackItem itemPause = this.ttsPlaybackItemMap.getPlaybackItem(mPlayingFileName);

                if (itemPause == null) {
                    stopTextToSpeech();
                    sendUpdateMessage(null, TTSUtil.STOPPED);
                    Log.i("Ashwani", "ACTION_PAUSE  if stop");
                } else {
                    MediaPlayer mediaPlayerPause = itemPause.getMediaPlayer();
                    if (mediaPlayerPause != null && mediaPlayerPause.isPlaying()) {
                        isPauseRequested = true;
                        mediaPlayerPause.pause();
                        sendUpdateMessage(null, TTSUtil.PAUSED);
                        Log.i("Ashwani", "ACTION_PAUSE  else if pause");
                    } else {
                        sendUpdateMessage(null, TTSUtil.STOPPED);
                        Log.i("Ashwani", "ACTION_PAUSE  else else stop");
                    }
                }
                Log.i("", "");
                // TODO
                break;
            case TTSUtil.ACTION_STOP:
                Log.i("Ashwani", "ACTION_STOP");
                stopTextToSpeech();

                mSpeechText = null;
                sendUpdateMessage(null, TTSUtil.STOPPED);

                // TODO
                break;
            case TTSUtil.ACTION_NEXT:
                Log.i("", "");
                performSkip(true);
                // TODO
                break;
            case TTSUtil.ACTION_PREVIOUS:
                Log.i("", "");
                performSkip(false);
                // TODO
                break;
            case TTSUtil.ACTION_MOVE_FORWARD_FEW_SECOND:
                Log.i("", "");
                // TODO
                break;
            case TTSUtil.ACTION_MOVE_BACKWARD_FEW_SECOND:
                Log.i("", "");
                // TODO
                break;
            case TTSUtil.ACTION_PITCH:
                if (textToSpeech != null) {
                    float pitch = TTSPreference.getInstance(this).getPitch();
                    textToSpeech.setPitch(pitch);
                }

                break;
            case TTSUtil.ACTION_SPEED:
                if (textToSpeech != null) {
                    float speed = TTSPreference.getInstance(this).getSpeed();
                    textToSpeech.setSpeechRate(speed);
                }

                break;
            case TTSUtil.ACTION_LOCALE:
                if (textToSpeech != null) {
                    final String country = TTSPreference.getInstance(this).getCountry();
                    final String language = TTSPreference.getInstance(this).getLanguage();
                    final Locale locale = new Locale(language, country);
                    int result = textToSpeech.setLanguage(locale);
                }
                break;
            case TTSUtil.ACTION_FORCE_CLOSE_SERVICE:
                isPauseRequested = true;
                final TTSPlaybackItem itemForce = ttsPlaybackItemMap.getPlaybackItem(mPlayingFileName);

                if (itemForce == null) {
                    stopTextToSpeech();
                } else {
                    MediaPlayer mediaPlayerPause = itemForce.getMediaPlayer();
                    if (mediaPlayerPause != null && mediaPlayerPause.isPlaying()) {
                        mediaPlayerPause.stop();
                        sendUpdateMessage(null, TTSUtil.STOPPED);
                    }
                }

                Intent intent1 = new Intent(SpeechService.this, SpeechService.class);
                stopService(intent1);

                /*uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(SpeechService.this, SpeechService.class);
                        stopService(intent1);
                    }
                }, 500);*/
                break;
        }

        return SpeechService.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        TTSPreference.getInstance(this).setTTSServiceRunning(false);
        super.onDestroy();
    }


    @Override
    public void onInit(int status) {

        ttsAvailable = (status == TextToSpeech.SUCCESS) && !IS_NOOK_TOUCH;

        if (ttsAvailable) {

            TTSPreference ttsPreference = TTSPreference.getInstance(this);
            float speed = ttsPreference.getSpeed();
            float pitch = ttsPreference.getPitch();

            textToSpeech.setPitch(pitch);
            textToSpeech.setSpeechRate(speed);

            String country = ttsPreference.getCountry();
            String language = ttsPreference.getLanguage();

            Locale locale = new Locale(language, country);

            int result = textToSpeech.setLanguage(locale);

            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {

                mIsTTSInitialised = true;
                startTextToSpeech(false, mPlayingArticleId);
                //sendUpdateMessage(null, TTSUtil.INITIALISED);

            } else {
                sendUpdateMessage(null, TTSUtil.LANG_NOT_SUPPORTED);
            }
        } else {
            Toast.makeText(this, "TTS is not available in this device.", Toast.LENGTH_SHORT).show();
            sendUpdateMessage(null, TTSUtil.INIT_FAILED);
        }
    }

    private boolean ttsIsRunning() {
        if (isFromMap) {
            return ttsPlaybackItemMap.isActive();
        } else {
            return ttsPlaybackItemQueue.isActive();
        }

    }

    private void ttsCompleteListener() {

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String wavFile) {
                Log.i("", "");
            }

            @Override
            public void onDone(String wavFile) {
                if (!ttsIsRunning()) {
                    textToSpeech.stop();
                    sendUpdateMessage(null, TTSUtil.STOPPED);
                    return;
                }

                if (!ttsItemPrep.containsKey(wavFile)) {
                    Log.e("", "Got onStreamingCompleted for " + wavFile + " but there is no corresponding TTSPlaybackItem!");
                    return;
                }

                final TTSPlaybackItem item = ttsItemPrep.remove(wavFile);

                try {
                    MediaPlayer mediaPlayer = item.getMediaPlayer();
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(wavFile);
                    mediaPlayer.prepare();
                    mPlayingArticleId = getArticleIdFromFilename(wavFile);
                    ttsPlaybackItemMap.addPlaybackItem(wavFile, item);
                } catch (Exception e) {
                    Log.e("", "Could not play", e);
                    return;
                }

                //If the queue is size 1, it only contains the player we just added,
                //meaning this is a first playback start.

                if (ttsPlaybackItemMap.sizeMap() == 1) {
                    sendUpdateMessage(null, TTSUtil.INITIALISED);
                    startPlayback(wavFile);
                }
            }

            @Override
            public void onError(String wavFile) {

                Log.i("", "");

            }
        });


    }


    private void startPlayback(final String fileName) {


        final TTSPlaybackItem item = this.ttsPlaybackItemMap.getPlaybackItem(fileName);

        if (item == null) {
            Bundle bundle = new Bundle();
            sendUpdateMessage(bundle, TTSUtil.COMPLETED);
            mPlayingFileName = null;

//            ttsPlaybackItemQueue.removeAllPlaybackItem();

            TTSUtil.sendTTSForceCloseService(this);

            return;
        }


        if (item.getMediaPlayer().isPlaying()) {
            return;
        }

        item.setOnSpeechCompletedCallback(new SpeechCompletedCallback() {
            @Override
            public void speechCompleted(TTSPlaybackItem item, MediaPlayer mediaPlayer) {

                String alreadyPlayedFileName = item.getFileName();

                if (!ttsPlaybackItemMap.isEmptyMap()) {
                    ttsPlaybackItemMap.removePlaybackItem(alreadyPlayedFileName);
                    mSpeechText = null;

                    Log.d("_AshwaniF", "============================================================");
                    Log.d("_AshwaniF", "Removed file " + item.getFileName());
                }



                if (ttsIsRunning() ) {

                    ///storage/sdcard/Android/data/com.mobstac.thehindu/cache/tts_19904904_index_1.wav

                    Log.i("_AshwaniF", "OldFile :: "+alreadyPlayedFileName);

                    int index = alreadyPlayedFileName.indexOf("_index_");
                    String oldFileIndex = alreadyPlayedFileName.substring(index + 7, alreadyPlayedFileName.length() - 4);
                    Log.i("_AshwaniF", "oldFileIndex :: "+oldFileIndex);

                    String oldFileInitial = alreadyPlayedFileName.substring(0, index);
                    Log.i("_AshwaniF", "oldFileInitial :: "+oldFileInitial);

                    int oldIndex = Integer.parseInt(oldFileIndex);
                    Log.i("_AshwaniF", "oldIndex :: "+oldIndex);

                    oldIndex++;
                    String newFile = oldFileInitial + "_index_" + oldIndex + ".wav";

                    if(!isPauseRequested) {
                        startPlayback(newFile);
                    }

                    Log.i("_AshwaniF", "NewFile Or PlayingFile :: "+newFile);

                }

                mediaPlayer.release();
                new File(item.getFileName()).delete();
            }
        });

        if(!isPauseRequested) {
            item.getMediaPlayer().start();
            mPlayingFileName = fileName;
            sendUpdateMessage(null, TTSUtil.PLAY);
        }

    }


    public Option<File> getTTSFolder() {
        return firstOption(
                asList(
                        ContextCompat.getExternalCacheDirs(this)
                )
        );
    }

    private void performSkip(boolean toEnd) {

        if (!ttsIsRunning()) {
            return;
        }

        TTSPlaybackItem item = null;

        if (isFromMap) {
            item = this.ttsPlaybackItemMap.getPlaybackItem(mPlayingArticleId);
        } else {
            item = this.ttsPlaybackItemQueue.peek();
        }

        if (item != null) {
            MediaPlayer player = item.getMediaPlayer();

            if (toEnd) {
                player.seekTo(player.getDuration());
            } else {
                player.seekTo(0);
            }

        }

    }

    private void stopTextToSpeech() {

        if(textToSpeech != null) {
            this.textToSpeech.stop();
            this.ttsPlaybackItemMap.removeAllPlaybackItem();
            this.ttsItemPrep.clear();
        }

    }

    private void showTTSFailed(final String message) {

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                stopTextToSpeech();
                StringBuilder textBuilder = new StringBuilder("TTS Failed");
                textBuilder.append("\n").append(message);

                Toast.makeText(SpeechService.this, textBuilder.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTextToSpeech(boolean isNeedToShowProgress, String articleId) {

        if (audioManager.isMusicActive()) {
            return;
        }

        if (isNeedToShowProgress) {
            sendUpdateMessage(null, TTSUtil.INITIALIZING);
        }

        ttsCompleteListener();

        Option<File> fosOption = getTTSFolder();

        if (isEmpty(fosOption)) {
            showTTSFailed("Could not get base folder for TTS");
        }

        File fos = fosOption.unsafeGet();

        if (fos.exists() && !fos.isDirectory()) {
            fos.delete();
        }

        fos.mkdirs();

        if (!(fos.exists() && fos.isDirectory())) {
            showTTSFailed("Failed to create folder " + fos.getAbsolutePath());
            return;
        }

        //Delete any old TTS files still present.
        for (File f : fos.listFiles()) {
            f.delete();
        }

        ttsItemPrep.clear();

        if (!ttsAvailable) {
            return;
        }

        if (isFromMap) {
            ttsPlaybackItemMap.activate();
        } else {
            this.ttsPlaybackItemQueue.activate();
        }

        streamTTSToDisk(articleId);
    }


    private void streamTTSToDisk(final String articleId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doStreamTTSToDisk(articleId);

                Log.i("TTTR", "doStreamTTS");

            }
        }).start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Splits the text to be spoken into chunks and streams
     * them to disk. This method should NOT be called on the
     * UI thread!
     */
    private void doStreamTTSToDisk(String articleId) {

        String textToSpeak = "";

        textToSpeak = mSpeechText;

        Log.i("TTTR", "doStreamTTS-1");

        List<String> parts = TextUtil.splitOnPunctuation(textToSpeak);
//        List<String> parts = new ArrayList<>();
        parts.add(textToSpeak);

        int offset = 0;

        try {

            Option<File> ttsFolderOption = getTTSFolder();

            if (isEmpty(ttsFolderOption)) {
                throw new TTSFailedException();
            }

            File ttsFolder = ttsFolderOption.unsafeGet();

            for (int i = 0; i < parts.size()-1 && ttsIsRunning(); i++) {

                Log.d("", "Streaming part " + i + " to disk.");

                String part = parts.get(i);

                boolean lastPart = i == parts.size() - 1;

                //Utterance ID doubles as the filename
                String pageName = "";

                try {
                    File pageFile = new File(ttsFolder, createFileName(articleId, i));
                    pageName = pageFile.getAbsolutePath();
                    pageFile.createNewFile();
                } catch (IOException io) {
                    String message = "Can't write to file \n" + pageName + " because of error\n" + io.getMessage();
                    Log.e("", message);
                    showTTSFailed(message);
                    mSpeechText = null;
                    sendUpdateMessage(null, TTSUtil.INIT_FAILED);
                }

                streamPartToDisk(pageName, part, offset, textToSpeak.length(), lastPart);

                offset += part.length() + 1;

                Thread.yield();
            }

            Log.i("TTTR", "DONE");

        } catch (TTSFailedException e) {
            //Just stop streaming
        }
    }


    private String createFileName(String articleId, int index) {
//        return "tts_" + UUID.randomUUID().getLeastSignificantBits() + ".wav";
        return "tts_" + articleId + "_index_" + index + ".wav";
//        return "tts_" + articleId+ ".wav";
    }

    private String getArticleIdFromFilename(String fileName) {
        if (fileName == null) {
            return "";
        }
        ///storage/sdcard/Android/data/com.mobstac.thehindu/cache/tts_19894460
        fileName = fileName.replace("/storage/emulated/0/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace("/storage/sdcard/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace("storage/sdcard/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace("storage/0/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace(".wav", "");

        int fir = fileName.indexOf("_");
        fileName = fileName.substring(0, fir);

        return fileName;
    }


    private String runningArticleIdIndexFilename(String fileName, String articleId, int index) {
        ///storage/sdcard/Android/data/com.mobstac.thehindu/cache/tts_19894460
        fileName = fileName.replace("/storage/emulated/0/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace("/storage/sdcard/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace("storage/sdcard/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace("storage/0/Android/data/com.mobstac.thehindu/cache/tts_", "");
        fileName = fileName.replace(".wav", "");

        int fir = fileName.indexOf("_");
        fileName = fileName.substring(0, fir);

        return fileName;
    }


    private void streamPartToDisk(String fileName, String part, int offset, int totalLength, boolean endOfPage)
            throws TTSFailedException {

        if (part.trim().length() > 0 || endOfPage) {

            HashMap<String, String> params = new HashMap<>();

            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, fileName);

            TTSPlaybackItem item = new TTSPlaybackItem(part, new MediaPlayer(), totalLength, offset, endOfPage, fileName);
            ttsItemPrep.put(fileName, item);

            int result;
            String errorMessage = "";


            int lent = part.length();

            /*if(lent > 4000) {
                lent = 4000;
            }
            part = part.substring(0, lent);*/

            try {
                result = textToSpeech.synthesizeToFile(part, params, fileName);

            } catch (Exception e) {
                result = TextToSpeech.ERROR;
                errorMessage = e.getMessage();
            }

            if (result != TextToSpeech.SUCCESS) {
                String message = "Can't write to file \n" + fileName + " because of error\n" + errorMessage;
                showTTSFailed(message);
                mSpeechText = null;
                sendUpdateMessage(null, TTSUtil.INIT_FAILED);
            }
        } else {

        }
    }

    /**
     * Checked exception to indicate TTS failure
     **/
    private static class TTSFailedException extends Exception {
    }


    private void sendUpdateMessage(Bundle bundle, int action) {
        Intent intent = new Intent(TTSUtil.INTENT_FILTER);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(TTSUtil.COMMAND, action);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }




}
