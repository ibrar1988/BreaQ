package com.techvedika.breaq.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techvedika.breaq.R;
import com.techvedika.breaq.models.ScanData;

import java.util.List;

/**
 * Created by Ibrar on 8/20/2018.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    private List<ScanData> mCartItemList;

    private CartItemAdapter.OnItemClickListener mListener;

    public CartItemAdapter(List<ScanData> list, CartItemAdapter.OnItemClickListener listener) {

        this.mCartItemList = list;

        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemName, tvItemCharacterist, tvItemDescription, tvRmoveItem,tvItemPrice;

        private LinearLayout parentLayout;

        private MyViewHolder(View view) {
            super(view);
            tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            tvItemCharacterist = (TextView) view.findViewById(R.id.tvItemCharacterist);
            tvItemDescription = (TextView) view.findViewById(R.id.tvItemDescription);
            tvRmoveItem = (TextView) view.findViewById(R.id.tvRmoveItem);
            tvItemPrice = (TextView) view.findViewById(R.id.tvItemPrice);
            parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
        }
    }

    @Override
    public CartItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new CartItemAdapter.MyViewHolder(itemView);
    }

    private ScanData data;

    @Override
    public void onBindViewHolder(CartItemAdapter.MyViewHolder holder, int position) {
        data = mCartItemList.get(position);
        holder.tvItemName.setText("MacAirBook");
        holder.tvItemCharacterist.setText("15 inch - Product -67J8H");
        holder.tvItemDescription.setText("Item description");
        holder.tvItemPrice.setText("79,000.00");
        holder.tvRmoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartItemList.size();
    }

    public interface OnItemClickListener {

        void onItemClick(ScanData item);
    }
}