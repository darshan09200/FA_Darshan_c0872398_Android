package com.darshan09200.products.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darshan09200.products.databinding.ProductItemBinding;
import com.darshan09200.products.helper.Helper;
import com.darshan09200.products.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private final List<Product> products;
    private final OnItemClickListener onItemClickListener;

    public ProductsAdapter(List<Product> products, OnItemClickListener onItemClickListener) {
        this.products = products;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Product product = products.get(position);

        viewHolder.binding.productTitle.setText(product.getName());
        viewHolder.binding.productPrice.setText(Helper.formatPrice(product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ProductItemBinding binding);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProductItemBinding binding;

        public ViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                onItemClickListener.onItemClick(getAdapterPosition(), binding);
            });

            binding.productView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(getAdapterPosition(), binding);
            });
        }


    }
}
