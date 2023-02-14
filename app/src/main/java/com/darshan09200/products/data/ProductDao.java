package com.darshan09200.products.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.darshan09200.products.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product ORDER BY updatedAt DESC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product WHERE id == :id")
    Product getProduct(long id);

}
