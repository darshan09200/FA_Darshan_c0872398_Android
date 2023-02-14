package com.darshan09200.products.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.darshan09200.products.R;
import com.darshan09200.products.adapter.ProductsAdapter;
import com.darshan09200.products.databinding.FragmentHomeBinding;
import com.darshan09200.products.model.Product;
import com.darshan09200.products.model.ProductViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements ProductsAdapter.OnItemClickListener, MenuProvider {

    private FragmentHomeBinding binding;
    private final List<Product> products = new ArrayList<>();
    private ProductViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.productList.setAdapter(new ProductsAdapter(products, this));
        binding.productList.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty() {
                boolean emptyViewVisible = binding.productList.getAdapter().getItemCount() == 0;
                binding.emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
                binding.productList.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
            }
        });

        viewModel.getAllProducts().observe(getActivity(), favourites -> {
            this.products.clear();
            this.products.addAll(favourites);
            if (binding != null) {
                binding.productList.getAdapter().notifyDataSetChanged();
            }
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this);

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Product product = products.get(position);
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra("productId", product.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        if(isVisible()) {
            menu.clear();
            menuInflater.inflate(R.menu.home_menu, menu);
            System.out.println("called add");
        }
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.add_product){
            Product product = new Product();
            product.setName("Test 1");
            product.setDescription("Test 2");
            product.setPrice(100.0);
            product.setUpdatedAt(new Date());
            product.setCoordinate(new LatLng(43.7739109,-79.3444486));
            viewModel.insert(product);
            return true;
        }
        return false;
    }
}