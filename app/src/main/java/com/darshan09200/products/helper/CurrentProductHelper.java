package com.darshan09200.products.helper;

import androidx.annotation.Nullable;

import com.darshan09200.products.model.Product;

public class CurrentProductHelper {
    public static CurrentProductHelper instance = new CurrentProductHelper();

    @Nullable
    private Product product;

    private CurrentProductHelper() {

    }

    @Nullable
    public Product getProduct() {
        return product;
    }

    public void setProduct(@Nullable Product product) {
        this.product = product;
    }
}
