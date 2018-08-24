package com.techvedika.breaq.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        private ImageView imgItemImage;
        private TextView tvProductPrice, tvProductName, tvUpdateItem, tvRemoveItem, tvMaterialType,tvMaterialColor
                ,tvDescreaseItem, tvItemCount, tvIncreaseItem, tvTotalItemPrice,tvChargesMessage;

        private LinearLayout parentLayout;

        private MyViewHolder(View view) {
            super(view);
            imgItemImage = (ImageView) view.findViewById(R.id.imgItemImage);
            tvProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);
            tvProductName = (TextView) view.findViewById(R.id.tvProductName);
            tvUpdateItem = (TextView) view.findViewById(R.id.tvUpdateItem);
            tvRemoveItem = (TextView) view.findViewById(R.id.tvRemoveItem);
            tvMaterialType = (TextView) view.findViewById(R.id.tvMaterialType);
            tvMaterialColor = (TextView) view.findViewById(R.id.tvMaterialColor);
            tvDescreaseItem = (TextView) view.findViewById(R.id.tvDescreaseItem);
            tvItemCount = (TextView) view.findViewById(R.id.tvItemCount);
            tvIncreaseItem = (TextView) view.findViewById(R.id.tvIncreaseItem);
            tvTotalItemPrice = (TextView) view.findViewById(R.id.tvTotalItemPrice);
            tvChargesMessage = (TextView) view.findViewById(R.id.tvChargesMessage);
            parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
        }
    }

    @Override
    public CartItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new CartItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartItemAdapter.MyViewHolder holder, int position) {
        holder.tvProductPrice.setText(String.valueOf(mCartItemList.get(position).getProductPrice()));
        holder.tvProductName.setText(mCartItemList.get(position).getProductName());
        holder.tvMaterialType.setText(mCartItemList.get(position).getMaterialType());
        holder.tvMaterialColor.setText(mCartItemList.get(position).getMaterialColor());
        holder.tvItemCount.setText(String.valueOf(mCartItemList.get(position).getMaterialCount()));
        holder.tvTotalItemPrice.setText(String.valueOf(mCartItemList.get(position).getTotalItemPrice()));
    }

    @Override
    public int getItemCount() {
        return mCartItemList.size();
    }

    public interface OnItemClickListener {
        void onUpdateItem(ScanData item);
        void onRemoveItem(ScanData item);
        void onIncreaseItem();
        void onDecreaseItem();
    }
}