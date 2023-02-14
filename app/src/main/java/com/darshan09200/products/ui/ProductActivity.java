package com.darshan09200.products.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.darshan09200.products.R;
import com.darshan09200.products.databinding.ActivityProductBinding;
import com.darshan09200.products.model.Product;
import com.darshan09200.products.model.ProductViewModel;

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
        ProductViewModel viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        Product product = viewModel.getProduct(productId);

        if(product != null){
            binding.productTitle.setText(product.getName());
            binding.productDescription.setText(product.getDescription());
            binding.productPrice.setText("$ "+product.getPrice());
//            binding.productTitle.setText(product.getName());
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