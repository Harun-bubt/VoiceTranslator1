package com.example.voicetranslator.voice_translator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.voicetranslator.R;
import com.example.voicetranslator.voice_translator.model.LangData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageAdapter extends Adapter<LanguageAdapter.ItemViewHolder> {
    Context context;
    private List<LangData> list;
    public List<LangData> mRecyclerViewItems = null;

    public class ItemViewHolder extends ViewHolder {
        TextView item_input;
        TextView item_result;

        public ItemViewHolder(View view) {
            super(view);
            this.item_input = (TextView) view.findViewById(R.id.item_input);
            this.item_result = (TextView) view.findViewById(R.id.item_result);
        }
    }

    public LanguageAdapter(Context context2, List<LangData> list2) {
        this.list = list2;
        this.mRecyclerViewItems = new ArrayList();
        this.mRecyclerViewItems.addAll(this.list);
        this.context = context2;
    }

    public int getItemCount() {
        return this.list.size();
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lang_item, viewGroup, false));
    }

    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        LangData langData = (LangData) this.list.get(i);
        String trim = langData.getmLang().trim();
        String trim2 = langData.getmLangCode().trim();
        itemViewHolder.item_input.setText(trim);
        TextView textView = itemViewHolder.item_result;
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(trim2);
        sb.append(")");
        textView.setText(sb.toString());
    }

    public void filter(String str) {
        String lowerCase = str.replaceAll(" ", "").toLowerCase(Locale.getDefault());
        this.list.clear();
        if (lowerCase.length() == 0) {
            this.list.addAll(this.mRecyclerViewItems);
        } else {
            for (LangData langData : this.mRecyclerViewItems) {
                if (langData.getmLang().toLowerCase(Locale.getDefault()).contains(lowerCase) || langData.getmLangCode().toLowerCase(Locale.getDefault()).contains(lowerCase)) {
                    this.list.add(langData);
                }
            }
        }
        notifyDataSetChanged();
    }
}