package com.darshan09200.products.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.darshan09200.products.model.Product;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DatabaseClient {
    private static DatabaseClient mInstance;
    private final AppDatabase appDatabase;
    private final Context context;
    private final ProductDao productDao;

    private LiveData<List<Product>> allProducts;

    private DatabaseClient(Context context) {
        this.context = context;

        appDatabase = Room.databaseBuilder(this.context, AppDatabase.class, "Products").build();

        productDao = appDatabase.productDao();
        Future<LiveData<List<Product>>> future = AppDatabase.databaseWriteExecutor.submit(() -> productDao.getAllProducts());
        try {
            allProducts = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> filterProducts(String query) {
        Future<LiveData<List<Product>>> future = AppDatabase.databaseWriteExecutor.submit(() -> productDao.filterProducts(query));
        LiveData<List<Product>> result = null;
        try {
            result = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Product getProduct(long id) {
        Future<Product> future = AppDatabase.databaseWriteExecutor.submit(() -> productDao.getProduct(id));
        Product result = null;
        try {
            result = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insert(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> productDao.insert(product));
    }

    public void delete(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> productDao.delete(product));
    }

}
