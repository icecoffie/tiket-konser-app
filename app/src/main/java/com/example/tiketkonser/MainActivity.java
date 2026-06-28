package com.example.tiketkonser;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiketkonser.databinding.ActivityMainBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TicketViewModel viewModel;
    private TicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        setupRecyclerView();
        setupObservers();
        setupSearchView();
        setupFab();
        setupSwipeToDelete();
    }

    private void setupRecyclerView() {
        adapter = new TicketAdapter(ticket -> {
            AddEditTicketBottomSheet sheet = new AddEditTicketBottomSheet(ticket, updatedTicket -> {
                viewModel.update(updatedTicket);
            });
            sheet.show(getSupportFragmentManager(), "EditTicket");
        });
        binding.rvTickets.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTickets.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getFilteredTickets().observe(this, tickets -> {
            adapter.submitList(tickets);
            binding.layoutEmpty.setVisibility(tickets.isEmpty() ? View.VISIBLE : View.GONE);
            binding.tvTotalTickets.setText(String.valueOf(tickets.size()));
        });

        viewModel.getTotalSpending().observe(this, total -> {
            if (total != null) {
                binding.tvTotalSpending.setText(formatRupiah(total));
            } else {
                binding.tvTotalSpending.setText("Rp 0");
            }
        });

        viewModel.getTotalTickets().observe(this, total -> {
            if (total != null) {
                binding.tvTotalTickets.setText(String.valueOf(total));
            }
        });

        viewModel.getDistinctGroups().observe(this, groups -> {
            setupChips(groups);
            binding.tvTotalGroups.setText(String.valueOf(groups.size()));
        });
    }

    private void setupChips(List<String> groups) {
        binding.chipGroupGroups.removeAllViews();
        
        Chip allChip = new Chip(this);
        allChip.setText("Semua");
        allChip.setCheckable(true);
        allChip.setChecked(true);
        allChip.setOnClickListener(v -> viewModel.setFilterGroup("Semua"));
        binding.chipGroupGroups.addView(allChip);

        for (String group : groups) {
            Chip chip = new Chip(this);
            chip.setText(group);
            chip.setCheckable(true);
            chip.setOnClickListener(v -> viewModel.setFilterGroup(group));
            binding.chipGroupGroups.addView(chip);
        }
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupFab() {
        binding.fabAdd.setOnClickListener(v -> {
            AddEditTicketBottomSheet sheet = new AddEditTicketBottomSheet(null, ticket -> {
                viewModel.insert(ticket);
            });
            sheet.show(getSupportFragmentManager(), "AddTicket");
        });
    }

    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                ConcertTicket ticket = adapter.getCurrentList().get(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Hapus Tiket")
                        .setMessage("Apakah Anda yakin ingin menghapus tiket ini?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            viewModel.delete(ticket);
                            Snackbar.make(binding.getRoot(), "Tiket dihapus", Snackbar.LENGTH_LONG)
                                    .setAction("Undo", v -> viewModel.insert(ticket))
                                    .show();
                        })
                        .setNegativeButton("Batal", (dialog, which) -> adapter.notifyItemChanged(position))
                        .setOnCancelListener(dialog -> adapter.notifyItemChanged(position))
                        .show();
            }
        }).attachToRecyclerView(binding.rvTickets);
    }

    private String formatRupiah(long amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(amount);
    }
}
