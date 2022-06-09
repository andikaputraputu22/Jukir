package com.project.jukir.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.jukir.R;
import com.project.jukir.models.LokasiLantai;

import java.util.ArrayList;
import java.util.List;

public class AdapterLantai extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<LokasiLantai> items;
    private OnItemClickListener mOnItemClickListener;
    private List<LinearLayout> itemLayout = new ArrayList<>();
    private List<TextView> itemNameLantai = new ArrayList<>();
    private List<TextView> itemTotalSlot = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, LokasiLantai obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterLantai(Context context, List<LokasiLantai> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public LinearLayout clickParent;
        public TextView totalSlot, nameLantai;
        public OriginalViewHolder(View v) {
            super(v);
            mView = v;
            clickParent = (LinearLayout) mView.findViewById(R.id.clickParent);
            nameLantai = (TextView) mView.findViewById(R.id.nameLantai);
            totalSlot = (TextView) mView.findViewById(R.id.totalSlot);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lantai, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            LokasiLantai lokasiLantai = items.get(position);
            itemLayout.add(view.clickParent);
            itemNameLantai.add(view.nameLantai);
            itemTotalSlot.add(view.totalSlot);
            setBackground(itemLayout.get(0), itemNameLantai.get(0), itemTotalSlot.get(0));
            view.nameLantai.setText(lokasiLantai.lantai);
            view.totalSlot.setText(String.valueOf(lokasiLantai.slot_tersedia));
            view.clickParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBackground(view.clickParent, view.nameLantai, view.totalSlot);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
        }
    }

    private void setBackground(LinearLayout layout, TextView nameLantai, TextView totalSlot) {
        for (LinearLayout it : itemLayout) {
            it.setBackgroundResource(R.drawable.bg_lantai_unselected);
        }

        for (TextView it : itemNameLantai) {
            it.setTextColor(Color.parseColor("#FF000000"));
            it.setTextColor(Color.parseColor("#FF000000"));
        }

        for (TextView it : itemTotalSlot) {
            it.setTextColor(Color.parseColor("#FF000000"));
            it.setTextColor(Color.parseColor("#FF000000"));
        }

        layout.setBackgroundResource(R.drawable.bg_lantai_selected);
        nameLantai.setTextColor(Color.parseColor("#fca311"));
        totalSlot.setTextColor(Color.parseColor("#fca311"));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(List<LokasiLantai> items) {
        notifyDataSetChanged();
    }
}
