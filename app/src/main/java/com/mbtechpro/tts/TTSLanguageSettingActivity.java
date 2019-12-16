package com.mbtechpro.tts;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.mbtechpro.tts.discreteSeekbar.TTSPreference;
import com.mbtechpro.tts_mediaplayer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ashwanisingh on 23/10/17.
 */

public class TTSLanguageSettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ListView mLanguageListView;
    private ProgressBar mProgressBar;
    private LanguageAdapter mAdapter;

    private TextToSpeech mTts = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttslanguagesetting);
        mToolbar = findViewById(R.id.toolbar);
        mLanguageListView = findViewById(R.id.langListView);
        mProgressBar = findViewById(R.id.progressBar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Accent");
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

        mProgressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.bottomDivider).setVisibility(View.GONE);

        languageAvailableVerification(TTSLanguageSettingActivity.this);



    }


    private void languageAvailableVerification(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {

                        final TTSPreference ttsPreference = TTSPreference.getInstance(TTSLanguageSettingActivity.this);

                        final LanguageItem selectedItem = new LanguageItem();
                        selectedItem.country = ttsPreference.getCountry();
                        selectedItem.language = ttsPreference.getLanguage();
                        selectedItem.displayName = ttsPreference.getDisplayName();

                        final List<LanguageItem> languageList = new ArrayList<>();
                        final Locale[] locales = Locale.getAvailableLocales();

                        if (mTts != null) {
                            for (Locale locale : locales) {
                                int res = mTts.isLanguageAvailable(locale);

                                if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE && locale.getDisplayLanguage().startsWith("Eng")) {

                                    int langStat = mTts.isLanguageAvailable(new Locale(locale.getLanguage() + "-" + locale.getCountry()));

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

                        final int selectedIndex = languageList.indexOf(selectedItem);

                        if (selectedIndex != -1) {
                            selectedItem.isSelected = true;
                            selectedItem.isExist = true;
                            languageList.remove(selectedItem);
                            languageList.add(selectedIndex, selectedItem);
                        }

                        mAdapter = new LanguageAdapter(TTSLanguageSettingActivity.this, R.layout.language_item_row, languageList);
                        mLanguageListView.setAdapter(mAdapter);

                        mAdapter.setLanguageSelectionListener(new LanguageAdapter.LanguageSelectionListener() {
                            @Override
                            public void onLanguageSelection(LanguageItem item) {
                                final TTSPreference ttsPreference = TTSPreference.getInstance(TTSLanguageSettingActivity.this);
                                ttsPreference.setCountry(item.country);
                                ttsPreference.setLanguage(item.language);
                            }
                        });

                        mProgressBar.setVisibility(View.GONE);
                        findViewById(R.id.bottomDivider).setVisibility(View.VISIBLE);

                    }
                });


                ///

            }
        }, 500);
    }

    /**
     * This is Language Adapter
     */
    private static class LanguageAdapter extends ArrayAdapter<LanguageItem> {

        private LayoutInflater inflater;
        private int resLayoutId;
        private LanguageSelectionListener languageSelectionListener;

        public LanguageAdapter(@NonNull Context context, @LayoutRes int resource, List<LanguageItem> languageItems) {
            super(context, resource, languageItems);
            resLayoutId = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setLanguageSelectionListener(LanguageSelectionListener languageSelectionListener) {
            this.languageSelectionListener = languageSelectionListener;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View vi = convertView;
            ViewHolder holder;

            if (convertView == null) {
                vi = inflater.inflate(resLayoutId, null);
                holder = new ViewHolder();

                holder.accentLanguagerow = vi.findViewById(R.id.accentLanguageRowClick);
                holder.selectionIV = vi.findViewById(R.id.selection);
                holder.langTV = vi.findViewById(R.id.lang);
                holder.downloadIV = vi.findViewById(R.id.download);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            final LanguageItem item = getItem(position);

            holder.langTV.setText(item.displayName);

            if (item.isExist) {
                holder.downloadIV.setVisibility(View.GONE);
            } else {
                holder.downloadIV.setVisibility(View.VISIBLE);
            }

            if (item.isSelected) {
                holder.selectionIV.setImageResource(R.drawable.ic_radio_checked_wrapper);
            } else {
                holder.selectionIV.setImageResource(R.drawable.ic_radio_unchecked_wrapper);
            }

            holder.accentLanguagerow.setOnClickListener(selectionClickListener(item));
            holder.downloadIV.setOnClickListener(downloadClickListener());

            return vi;
        }


        private View.OnClickListener selectionClickListener(final LanguageItem item) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!item.isExist) {
                        Toast.makeText(v.getContext(), "Before selection you must download.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (int i = 0; i < getCount(); i++) {
                        getItem(i).isSelected = getItem(i).equals(item);
                    }

                    if (languageSelectionListener != null) {
                        languageSelectionListener.onLanguageSelection(item);

                        final TTSPreference ttsPreference = TTSPreference.getInstance(v.getContext());
                        ttsPreference.setLanguage(item.language);
                        ttsPreference.setCountry(item.country);
                        ttsPreference.setDisplayName(item.displayName);

                        TTSUtil.sendTTSLocale(v.getContext());

                    }

                    notifyDataSetChanged();

                }
            };
        }

        private View.OnClickListener downloadClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTSUtil.openTTSSettingScreen(v.getContext());
                }
            };
        }

        private static class ViewHolder {
            private LinearLayout accentLanguagerow;
            private ImageView selectionIV;
            private TextView langTV;
            private ImageView downloadIV;
        }


        public interface LanguageSelectionListener {
            void onLanguageSelection(LanguageItem item);
        }

    }


}
