package com.darshan09200.products.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.darshan09200.products.R;
import com.darshan09200.products.adapter.ProductsAdapter;
import com.darshan09200.products.databinding.FragmentHomeBinding;
import com.darshan09200.products.databinding.ProductItemBinding;
import com.darshan09200.products.model.Product;
import com.darshan09200.products.model.ProductViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements ProductsAdapter.OnItemClickListener, MenuProvider {

    private final List<Product> products = new ArrayList<>();
    private FragmentHomeBinding binding;
    private ProductViewModel viewModel;
    private MenuItem searchViewItem;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ProductViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.productList.setAdapter(new ProductsAdapter(products, this));

        checkEmpty();

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


        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this);

        return binding.getRoot();
    }

    private void checkEmpty() {
        boolean emptyViewVisible = binding.productList.getAdapter().getItemCount() == 0;
        System.out.println(binding.productList.getAdapter().getItemCount());
        binding.emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
        binding.productList.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAllProducts().observe(getActivity(), products -> {
            this.products.clear();
            this.products.addAll(products);
            if (binding != null) {
                binding.productList.getAdapter().notifyDataSetChanged();
            }
        });

        if (viewModel.recentlyDeleted()) {
            Snackbar.make(binding.getRoot(), "Product deleted", Snackbar.LENGTH_LONG).setAction("UNDO", v -> {
                if (viewModel.undoDelete()) {
                    Snackbar.make(binding.getRoot(), "Product restored", Snackbar.LENGTH_LONG).show();
                }
            }).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    viewModel.discardDeleted();
                }
            }).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position, ProductItemBinding binding) {
        Product product = products.get(position);
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra("productId", product.getId());
        Pair<View, String> p1 = Pair.create(binding.productTitle, "productTitle");
        Pair<View, String> p2 = Pair.create(binding.productPrice, "productPrice");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        if (isVisible()) {
            menu.clear();
            menuInflater.inflate(R.menu.home_menu, menu);

            searchViewItem = menu.findItem(R.id.search_bar);
            searchView = (SearchView) searchViewItem.getActionView();
            searchView.setQueryHint("Search Products");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    viewModel.filterProducts(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    viewModel.filterProducts(newText);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_product) {
            Intent intent = new Intent(getActivity(), AddProductActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}