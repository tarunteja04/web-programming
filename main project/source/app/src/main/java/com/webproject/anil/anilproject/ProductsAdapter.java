package com.webproject.anil.anilproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<Product> productList;
    AdapterCallback adapterCallback;
    int l;

    public ProductsAdapter(Context mCtx, List<Product> productList, AdapterCallback adapterCallback) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.adapterCallback=adapterCallback;
        l=1;
    }
    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.adapterCallback=adapterCallback;
        l=2;
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view;
        if (l == 1)
            view = inflater.inflate(R.layout.layout_products, null);
        else
            view = inflater.inflate(R.layout.layout_recommendation, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(product.getTitle());
        if(l==1) {
            holder.textViewShortDesc.setText(product.getShortdesc());
            holder.textViewRating.setText(String.valueOf(product.getRating()));
            holder.textViewPrice.setText(String.valueOf(product.getPrice())+"/"+String.valueOf(product.getMarketPrice()));
            holder.btnView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    adapterCallback.onClickCallback(product.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;
        Button btnView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageView = itemView.findViewById(R.id.imageView);
            if(l==1) {
                textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
                textViewRating = itemView.findViewById(R.id.textViewRating);
                textViewPrice = itemView.findViewById(R.id.textViewPrice);
                btnView = itemView.findViewById(R.id.btnView);
            }
        }
    }
}
