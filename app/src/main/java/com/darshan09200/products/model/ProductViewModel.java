package com.darshan09200.products.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.darshan09200.products.data.DatabaseClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private static Product lastDeleted;
    private final DatabaseClient databaseClient;
    private final LiveData<List<Product>> allProducts;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public ProductViewModel(@NonNull Application application) {
        super(application);

        databaseClient = DatabaseClient.getInstance(application);
        allProducts = Transformations.switchMap(searchQuery, input -> {
            if (input == null || input.equals("")) {
                return databaseClient.getAllProducts();
            } else {
                return databaseClient.filterProducts(input);
            }
        });
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void filterProducts(String query) {
        searchQuery.setValue(query.trim());
    }

    public Product getProduct(long id) {
        return databaseClient.getProduct(id);
    }


    public void insert(Product product) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            product.setUserId(user.getUid());
        }
        databaseClient.insert(product);
    }

    public void delete(Product product) {
        lastDeleted = product;
        databaseClient.delete(product);
    }

    public boolean recentlyDeleted() {
        return lastDeleted != null;
    }

    public void discardDeleted() {
        lastDeleted = null;
    }

    public boolean undoDelete() {
        if (lastDeleted != null) {
            insert(lastDeleted);
            lastDeleted = null;
            return true;
        }
        return false;
    }
}
