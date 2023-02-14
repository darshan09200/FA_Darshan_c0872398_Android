package com.darshan09200.products.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.darshan09200.products.R;
import com.darshan09200.products.databinding.ActivityProductBinding;
import com.darshan09200.products.helper.CurrentProductHelper;
import com.darshan09200.products.helper.Helper;
import com.darshan09200.products.model.Product;
import com.darshan09200.products.model.ProductViewModel;
import com.google.android.gms.maps.model.LatLng;

public class ProductActivity extends AppCompatActivity {

    ActivityProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        long productId = intent.getLongExtra("productId", 0);
        System.out.println(productId);
        ProductViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

        Product product = viewModel.getProduct(productId);

        if (product != null) {
            binding.productTitle.setText(product.getName());
            binding.productDescription.setText(product.getDescription());
            String doubleAsString = String.valueOf(product.getPrice());
            int indexOfDecimal = doubleAsString.indexOf(".");
            binding.productPrice.setText(Helper.formatPrice(product.getPrice()));
            if (product.getCoordinate() != null) {
                String mapImageUrl = Helper.getStaticMapUrl(product.getCoordinate(), getResources().getString(R.string.api_key));
                Glide.with(this).load(mapImageUrl).into(binding.productMapImage);
            } else {
                binding.productMapImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}