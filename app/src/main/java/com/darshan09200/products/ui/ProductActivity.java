package com.darshan09200.products.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.darshan09200.products.R;
import com.darshan09200.products.databinding.ActivityProductBinding;
import com.darshan09200.products.helper.Helper;
import com.darshan09200.products.model.Product;
import com.darshan09200.products.model.ProductViewModel;

public class ProductActivity extends AppCompatActivity {

    ActivityProductBinding binding;
    ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        updateUI();
    }

    private void updateUI() {
        Intent intent = getIntent();
        long productId = intent.getLongExtra("productId", 0);

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
            binding.productEdit.setOnClickListener(v -> {
                Intent productIntent = new Intent(ProductActivity.this, AddProductActivity.class);
                productIntent.putExtra("productId", product.getId());
                startActivity(productIntent);
            });

            binding.productDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            viewModel.delete(product);
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();

            });

            getSupportActionBar().setTitle(product.getName());
        } else {
            getSupportActionBar().setTitle("Product");
            binding.productEdit.setVisibility(View.GONE);
            binding.productDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}