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
import com.project.jukir.models.DataWallet;
import com.project.jukir.utils.StaticController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterWallet extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<DataWallet> items;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DataWallet obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterWallet(Context context, List<DataWallet> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public LinearLayout clickParent;
        public ImageView image;
        public TextView name, saldo;
        public OriginalViewHolder(View v) {
            super(v);
            mView = v;
            clickParent = (LinearLayout) mView.findViewById(R.id.clickParent);
            image = (ImageView) mView.findViewById(R.id.image);
            name = (TextView) mView.findViewById(R.id.name);
            saldo = (TextView) mView.findViewById(R.id.saldo);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            DataWallet dataWallet = items.get(position);
            String saldo = StaticController.getFormatCurrency().format(dataWallet.saldo);
            view.name.setText(dataWallet.nama_metode_pembayaran);
            view.saldo.setText(saldo);
            Picasso.with(ctx).load(StaticController.URL_PHOTO_WALLET + dataWallet.icon).into(view.image);
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

    public void addItem(List<DataWallet> items) {
        notifyDataSetChanged();
    }
}
