package com.darshan09200.products.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.darshan09200.products.data.DatabaseClient;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final DatabaseClient databaseClient;
    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        databaseClient = new DatabaseClient(application);
        allProducts = databaseClient.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public Product getProduct(long id) {
        return databaseClient.getProduct(id);
    }


    public void insert(Product product) {
        databaseClient.insert(product);
    }

    public void delete(Product product) {
        databaseClient.delete(product);
    }
}
