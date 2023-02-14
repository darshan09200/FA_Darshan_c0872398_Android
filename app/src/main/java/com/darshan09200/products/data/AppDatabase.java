package com.darshan09200.products.data;

import androidx.room.RoomDatabase;

import com.darshan09200.products.model.Product;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(
        entities = {Product.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(4);

    public abstract ProductDao productDao();
}
