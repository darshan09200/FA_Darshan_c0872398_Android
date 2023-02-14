package com.darshan09200.products.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.darshan09200.products.R;
import com.darshan09200.products.databinding.ActivityAddProductBinding;
import com.darshan09200.products.helper.CurrentProductHelper;
import com.darshan09200.products.helper.Helper;
import com.darshan09200.products.model.Product;
import com.darshan09200.products.model.ProductViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class AddProductActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;

    ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

        prepareUI();

        binding.addLocation.setOnClickListener(v -> {
            Intent mapIntent = new Intent(AddProductActivity.this, MapsActivity.class);
            startActivity(mapIntent);
        });

        binding.addProduct.setOnClickListener(v -> {
            String name = binding.productTitle.getEditText().getText().toString().trim();
            String description = binding.productDescription.getEditText().getText().toString().trim();
            String priceString = binding.productPrice.getEditText().getText().toString().trim();

            Double price = 0.0;

            Product product = CurrentProductHelper.instance.getProduct();
            if (product == null) {
                product = new Product();
            }

            String message = "";
            if (name.isEmpty()) {
                message = "Name Empty";
            } else if (description.isEmpty()) {
                message = "Description Empty";
            } else if (priceString.isEmpty()) {
                message = "Price Empty";
            } else if (product.getCoordinate() == null) {
                message = "Select a location for product";
            } else {
                try {
                    price = Double.parseDouble(priceString);
                    if (price <= 0) {
                        message = "Price should be greater than 0";
                    }
                } catch (Exception e) {
                    message = "Invalid price";
                }
            }
            if (message.length() > 0) {
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            } else {
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setUpdatedAt(new Date());
//                product.setCoordinate(new LatLng(43.7739109, -79.3444486));

                viewModel.insert(product);
                CurrentProductHelper.instance.setProduct(null);
                finish();

            }
        });

    }

    private void prepareUI() {
        Intent intent = getIntent();
        long productId = intent.getLongExtra("productId", 0);

        if (productId > 0) {
            ProductViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

            Product product = viewModel.getProduct(productId);

            if (product != null) {
                CurrentProductHelper.instance.setProduct(product);
                getSupportActionBar().setTitle("Update Product");
                binding.productTitle.getEditText().setText(product.getName());
                binding.productDescription.getEditText().setText(product.getDescription());
                binding.productPrice.getEditText().setText(product.getPrice().toString());
                binding.addProduct.setText(("Update Product"));
                return;
            }
        }
        getSupportActionBar().setTitle("Add Product");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (CurrentProductHelper.instance.getProduct() != null) {
            binding.addLocation.setText("Update Location");
            binding.mapImage.setVisibility(View.VISIBLE);
            String mapImageUrl = Helper.getStaticMapUrl(CurrentProductHelper.instance.getProduct().getCoordinate(), getResources().getString(R.string.api_key));
            Glide.with(this).load(mapImageUrl).into(binding.mapImage);
        } else {
            binding.addLocation.setText("Add Location");
            binding.mapImage.setVisibility(View.GONE);
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