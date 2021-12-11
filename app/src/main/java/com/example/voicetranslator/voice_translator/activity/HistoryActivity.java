package com.example.voicetranslator.voice_translator.activity;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicetranslator.Main_activity;
import com.example.voicetranslator.R;
import com.example.voicetranslator.voice_translator.adapter.HistoryAdapter;
import com.example.voicetranslator.voice_translator.model.RecyclerItemClickListener;
import com.example.voicetranslator.voice_translator.model.SearchData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<Object> dataClassArrayList = new ArrayList<>();
    ArrayList<SearchData> httpParamList;
    HistoryAdapter mRecyclerViewAdapter;
    RecyclerView mRecyclerview;
    TextView txt_empty;
    private LinearLayoutManager layoutManager;

    private boolean isOnline() {
        try {
            return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_history);

        mRecyclerview = (RecyclerView) findViewById(R.id.rv_historylist);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        txt_empty = (TextView) findViewById(R.id.txt_empty);
        mRecyclerview.hasFixedSize();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        String string = getSharedPreferences("mPref", 0).getString("History", "");
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HistoryActivity.this.onBackPressed();
            }
        });

        isOnline();

        httpParamList = (ArrayList) new Gson().fromJson(string, new TypeToken<ArrayList<SearchData>>() {
        }.getType());
        if (this.httpParamList != null) {
            this.dataClassArrayList.clear();
            for (int size = this.httpParamList.size() - 1; size >= 0; size--) {
                AddData(((SearchData) this.httpParamList.get(size)).getSearchText(), ((SearchData) this.httpParamList.get(size)).getResultText());
            }
        }

        mRecyclerViewAdapter = new HistoryAdapter(this, this.dataClassArrayList);
        mRecyclerview.setAdapter(this.mRecyclerViewAdapter);
        if (mRecyclerViewAdapter.getItemCount() > 0) {
            txt_empty.setVisibility(View.GONE);
        } else {
            txt_empty.setVisibility(View.VISIBLE);
        }
        RecyclerView recyclerView = this.mRecyclerview;
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            public void onItemClick(View view, int i) {
                SearchData searchData = (SearchData) HistoryActivity.this.dataClassArrayList.get(i);
                Main_activity.et_input.setText(searchData.getSearchText());
                Main_activity.et_output.setText(searchData.getResultText());
                HistoryActivity.this.finish();
            }

            public void onItemLongClick(View view, int i) {
            }
        }));
    }

    private void AddData(String str, String str2) {
        SearchData searchData = new SearchData();
        searchData.setSearchText(str);
        searchData.setResultText(str2);
        dataClassArrayList.add(searchData);
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}