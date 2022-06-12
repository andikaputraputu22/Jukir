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
import com.project.jukir.models.DataWallet;
import com.project.jukir.models.ListLocation;
import com.project.jukir.utils.StaticController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterReport extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<ListLocation> items;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ListLocation obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterReport(Context context, List<ListLocation> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView nameUser, date, paymentMethod, nameEmployee, price;
        public OriginalViewHolder(View v) {
            super(v);
            mView = v;
            nameUser = (TextView) mView.findViewById(R.id.nameUser);
            nameEmployee = (TextView) mView.findViewById(R.id.nameEmployee);
            price = (TextView) mView.findViewById(R.id.price);
            date = (TextView) mView.findViewById(R.id.date);
            paymentMethod = (TextView) mView.findViewById(R.id.paymentMethod);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            ListLocation data = items.get(position);
            String price = StaticController.getFormatCurrency().format(data.total_harga);
            view.nameUser.setText(data.user.username);
            view.nameEmployee.setText(data.employee.nama);
            view.price.setText(price);
            view.date.setText(StaticController.dateFormatted(data.keluar, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
            view.paymentMethod.setText(data.metode_pembayaran.nama_metode_pembayaran);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(List<ListLocation> items) {
        notifyDataSetChanged();
    }
}
