package com.project.jukir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.jukir.R;
import com.project.jukir.models.DataLocation;
import com.project.jukir.utils.StaticController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSearchBuilding extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<DataLocation> items;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DataLocation obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterSearchBuilding(Context context, List<DataLocation> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public LinearLayout clickParent;
        public ImageView photoPlace;
        public TextView namePlace, slotPlace;
        public OriginalViewHolder(View v) {
            super(v);
            mView = v;
            clickParent = (LinearLayout) mView.findViewById(R.id.clickParent);
            photoPlace = (ImageView) mView.findViewById(R.id.photoPlace);
            namePlace = (TextView) mView.findViewById(R.id.namePlace);
            slotPlace = (TextView) mView.findViewById(R.id.slotPlace);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_building, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            DataLocation dataLocation = items.get(position);
            String slot = "Slot: 500";
            view.namePlace.setText(dataLocation.nama_lokasi);
            view.slotPlace.setText(slot);
            Picasso.with(ctx).load(StaticController.URL_PHOTO + dataLocation.gambar).into(view.photoPlace);
            view.clickParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(List<DataLocation> items) {
        notifyDataSetChanged();
    }
}
