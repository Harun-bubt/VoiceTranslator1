package com.example.voicetranslator.voice_translator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.voicetranslator.R;
import com.example.voicetranslator.voice_translator.model.SearchData;

import java.util.List;

public class HistoryAdapter extends Adapter<HistoryAdapter.ItemViewHolder> {
    Context context;
    public List<Object> mRecyclerViewItems;

    public class ItemViewHolder extends ViewHolder {
        LinearLayout cardView;
        TextView item_input;
        TextView item_result;

        public ItemViewHolder(View view) {
            super(view);
            cardView = (LinearLayout) view.findViewById(R.id.menu_item_card_view);
            item_input = (TextView) view.findViewById(R.id.item_input);
            item_result = (TextView) view.findViewById(R.id.item_result);
        }
    }

    public HistoryAdapter(Context context2, List<Object> list) {
        mRecyclerViewItems = list;
        context = context2;
    }

    public int getItemCount() {
        return this.mRecyclerViewItems.size();
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_data_item, viewGroup, false));
    }

    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        SearchData searchData = (SearchData) this.mRecyclerViewItems.get(i);
        String trim = searchData.getSearchText().trim();
        String trim2 = searchData.getResultText().trim();
        itemViewHolder.item_input.setText(trim);
        itemViewHolder.item_result.setText(trim2);
    }
}