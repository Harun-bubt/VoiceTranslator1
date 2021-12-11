package com.example.voicetranslator.voice_translator.activity;

import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.SearchView.SearchAutoComplete;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.example.voicetranslator.R;
import com.example.voicetranslator.voice_translator.adapter.LanguageAdapter;
import com.example.voicetranslator.voice_translator.model.LangData;
import com.example.voicetranslator.voice_translator.model.RecyclerItemClickListener;
import com.example.voicetranslator.voice_translator.share.MyApplication;

import java.util.ArrayList;

public class LanguageActivity extends AppCompatActivity {

    public static SharedPreferences mSharedPreferences;
    ArrayList<LangData> dataClassArrayList = new ArrayList<>();
    private SearchView et_search;
    String[] langCodeList;
    String[] langList;
    LanguageAdapter mRecyclerViewAdapter;
    RecyclerView mRecyclerview;

    private boolean isOnline() {
        try {
            return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_lang);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mRecyclerview = (RecyclerView) findViewById(R.id.rv_lang);
        et_search = (SearchView) findViewById(R.id.et_search);
        mRecyclerview.hasFixedSize();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        ImageView imageView = (ImageView) findViewById(R.id.img_back);
        isOnline();
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LanguageActivity.this.onBackPressed();
            }
        });
        ((ImageView) this.et_search.findViewById(R.id.search_button)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.font_color), Mode.MULTIPLY);
        ((ImageView) this.et_search.findViewById(R.id.search_close_btn)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.font_color), Mode.MULTIPLY);
        ((ImageView) this.et_search.findViewById(R.id.search_voice_btn)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.font_color), Mode.MULTIPLY);
        ((ImageView) this.et_search.findViewById(R.id.search_mag_icon)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.font_color), Mode.MULTIPLY);
        SearchAutoComplete searchAutoComplete = (SearchAutoComplete) this.et_search.findViewById(R.id.search_src_text);
        searchAutoComplete.setHintTextColor(-1);
        searchAutoComplete.setTextColor(-1);
        searchAutoComplete.setBackgroundColor(getResources().getColor(R.color.font_color));
        langList = getResources().getStringArray(R.array.language_name);
        langCodeList = getResources().getStringArray(R.array.language_code);
        String string = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, "en");
        String string2 = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, "hi");
        if (this.langList != null) {
            this.dataClassArrayList.clear();
            int i = 0;
            while (true) {
                String[] strArr = this.langList;
                if (i >= strArr.length) {
                    break;
                }
                AddData(strArr[i], this.langCodeList[i]);
                i++;
            }
        }
        for (int i2 = 0; i2 < this.langList.length; i2++) {
            if (MyApplication.selected_lang_pos == 1 && string2.equals(this.langCodeList[i2])) {
                this.dataClassArrayList.remove(i2);
            } else if (MyApplication.selected_lang_pos == 2 && string.equals(this.langCodeList[i2])) {
                this.dataClassArrayList.remove(i2);
            }
            mRecyclerViewAdapter = new LanguageAdapter(this, this.dataClassArrayList);
        }
        mRecyclerview.setAdapter(this.mRecyclerViewAdapter);
        et_search.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (LanguageActivity.this.mRecyclerViewAdapter.getItemCount() > 0) {
                    LanguageActivity.this.mRecyclerViewAdapter.filter(str.toString());
                } else {
                    LanguageActivity.this.mRecyclerViewAdapter.filter(str.toString());
                }
                return false;
            }
        });
        RecyclerView recyclerView = this.mRecyclerview;
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            public void onItemLongClick(View view, int i) {
            }

            public void onItemClick(View view, int i) {
                LangData langData = (LangData) LanguageActivity.this.dataClassArrayList.get(i);
                String str = "TAG";
                if (MyApplication.selected_lang_pos == 1) {
                    LanguageActivity.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(LanguageActivity.this.getApplicationContext());
                    FastSave.getInstance().saveString(MyApplication.FIRST_LANG_CODE, langData.getmLangCode());
                    StringBuilder sb = new StringBuilder();
                    sb.append("onItemClick:f lang ");
                    sb.append(MyApplication.FIRST_LANG_CODE);
                }
                if (MyApplication.selected_lang_pos == 2) {
                    LanguageActivity.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(LanguageActivity.this.getApplicationContext());
                    FastSave.getInstance().saveString(MyApplication.SEC_LANG_CODE, langData.getmLangCode());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onItemClick:s lang ");
                    sb2.append(MyApplication.SEC_LANG_CODE);
                }
                LanguageActivity.this.finish();
            }
        }));
    }

    private void AddData(String str, String str2) {
        LangData langData = new LangData();
        langData.setmLang(str);
        langData.setmLangCode(str2);
        this.dataClassArrayList.add(langData);
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}