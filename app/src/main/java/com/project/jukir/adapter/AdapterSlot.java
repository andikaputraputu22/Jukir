package com.project.jukir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.project.jukir.R;
import com.project.jukir.models.LokasiLantaiParkir;

import java.util.List;

public class AdapterSlot extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<LokasiLantaiParkir> items;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, LokasiLantaiParkir obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterSlot(Context context, List<LokasiLantaiParkir> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public LinearLayout clickParent;
        public OriginalViewHolder(View v) {
            super(v);
            mView = v;
            clickParent = (LinearLayout) mView.findViewById(R.id.clickParent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            LokasiLantaiParkir lokasiLantaiParkir = items.get(position);
            if (lokasiLantaiParkir.status) {
                view.clickParent.setBackgroundResource(R.drawable.bg_slot_empty);
            } else {
                view.clickParent.setBackgroundResource(R.drawable.bg_slot_filled);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(List<LokasiLantaiParkir> items) {
        notifyDataSetChanged();
    }
}
