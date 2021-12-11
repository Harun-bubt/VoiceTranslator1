package com.example.voicetranslator.voice_translator.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.voicetranslator.R;
import com.example.voicetranslator.voice_translator.trans.ListData;
import com.example.voicetranslator.voice_translator.trans.PreferenceTranslator;
import com.example.voicetranslator.voice_translator.trans.TransUtils;

import java.util.List;

public class MyBaseAdapter extends Adapter<MyBaseAdapter.C0940a> {

    public boolean check;
    public Context ctx;
    private List<ListData> myList;

    class C0940a extends ViewHolder {
        public ImageButton btnPlayWord;
        final MyBaseAdapter mAdapter;
        private ProgressBar progressPlayWord;
        private RelativeLayout rlPlayWord;
        public TextView tvWordName;

        public C0940a(MyBaseAdapter myBaseAdapter, View view) {
            super(view);
            mAdapter = myBaseAdapter;
            tvWordName = (TextView) view.findViewById(R.id.tvWordName);
            progressPlayWord = (ProgressBar) view.findViewById(R.id.progressPlayWord);
            rlPlayWord = (RelativeLayout) view.findViewById(R.id.rlPlayWord);
            btnPlayWord = (ImageButton) view.findViewById(R.id.btnPlayWord);
            btnPlayWord.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                }
            });
            float m3954f = (float) PreferenceTranslator.m3943a(myBaseAdapter.ctx).m3954f();
            double m3917a = (double) TransUtils.m3917a(m3954f, myBaseAdapter.ctx);
            Double.isNaN(m3917a);
            Double.isNaN(m3917a);
            int i = (int) (m3917a * 1.5d);
            int i2 = i / 6;
            if (myBaseAdapter.check) {
                tvWordName.setTextSize(m3954f);
                btnPlayWord.getLayoutParams().height = i;
                btnPlayWord.getLayoutParams().width = i;
                btnPlayWord.setPadding(i2, i2, i2, i2);
                progressPlayWord.getLayoutParams().height = i;
                progressPlayWord.getLayoutParams().width = i;
                progressPlayWord.setPadding(i2, i2, i2, i2);
                return;
            }
            this.rlPlayWord.setVisibility(View.GONE);
            this.tvWordName.setPadding(i2 * 2, 0, 0, 0);
        }
    }

    public MyBaseAdapter(List<ListData> list, boolean z, Context context) {
        this.myList = list;
        this.check = z;
        this.ctx = context;
    }

    public C0940a m3881a(ViewGroup viewGroup, int i) {
        return new C0940a(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tran_item, viewGroup, false));
    }

    public void m3882a(C0940a c0940a, int i) {
        final ListData listData = (ListData) this.myList.get(i);
        c0940a.tvWordName.setText(listData.m3893b());
        c0940a.btnPlayWord.setTag(Integer.valueOf(i));
        c0940a.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TransUtils.m3925b(MyBaseAdapter.this.ctx, listData.m3893b());
            }
        });
    }

    public int getItemCount() {
        return this.myList.size();
    }

    public C0940a onCreateViewHolder(ViewGroup viewGroup, int i) {
        return m3881a(viewGroup, i);
    }

    public void onBindViewHolder(C0940a c0940a, int i) {
        m3882a(c0940a, i);
    }
}
