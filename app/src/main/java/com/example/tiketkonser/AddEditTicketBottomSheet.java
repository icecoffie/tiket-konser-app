package com.example.tiketkonser;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tiketkonser.databinding.BottomSheetAddEditBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddEditTicketBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetAddEditBinding binding;
    private final ConcertTicket ticketToEdit;
    private final OnTicketSavedListener listener;

    public interface OnTicketSavedListener {
        void onTicketSaved(ConcertTicket ticket);
    }

    public AddEditTicketBottomSheet(ConcertTicket ticket, OnTicketSavedListener listener) {
        this.ticketToEdit = ticket;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAddEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupDropdowns();
        setupDatePicker();

        if (ticketToEdit != null) {
            binding.tvSheetTitle.setText("Edit Tiket");
            binding.actGroupName.setText(ticketToEdit.getGroupName(), false);
            binding.etConcertTitle.setText(ticketToEdit.getConcertTitle());
            binding.etVenue.setText(ticketToEdit.getVenue());
            binding.etDate.setText(ticketToEdit.getEventDate());
            binding.actSection.setText(ticketToEdit.getSeatSection(), false);
            binding.etPrice.setText(String.valueOf(ticketToEdit.getPrice()));
            binding.etQuantity.setText(String.valueOf(ticketToEdit.getQuantity()));
            binding.actStatus.setText(ticketToEdit.getStatus(), false);
        }

        binding.btnSave.setOnClickListener(v -> saveTicket());
    }

    private void setupDropdowns() {
        String[] groups = {"ITZY", "aespa", "EXO", "TWICE", "NewJeans", "BLACKPINK", "Stray Kids", "SEVENTEEN"};
        binding.actGroupName.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, groups));

        String[] sections = {"VIP", "CAT 1", "CAT 2", "Festival"};
        binding.actSection.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, sections));

        String[] status = {"Paid", "Pending", "Used"};
        binding.actStatus.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, status));
    }

    private void setupDatePicker() {
        binding.etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                String date = String.format(java.util.Locale.getDefault(), "%02d %s %d", dayOfMonth, getMonthName(month), year);
                binding.etDate.setText(date);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month];
    }

    private void saveTicket() {
        String group = binding.actGroupName.getText().toString();
        String title = binding.etConcertTitle.getText().toString();
        String venue = binding.etVenue.getText().toString();
        String date = binding.etDate.getText().toString();
        String section = binding.actSection.getText().toString();
        String priceStr = binding.etPrice.getText().toString();
        String qtyStr = binding.etQuantity.getText().toString();
        String status = binding.actStatus.getText().toString();

        if (group.isEmpty() || title.isEmpty() || venue.isEmpty() || date.isEmpty() || section.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty() || status.isEmpty()) {
            Toast.makeText(requireContext(), R.string.fill_all, Toast.LENGTH_SHORT).show();
            return;
        }

        long price = Long.parseLong(priceStr);
        int qty = Integer.parseInt(qtyStr);

        ConcertTicket ticket;
        if (ticketToEdit == null) {
            ticket = new ConcertTicket(group, title, venue, date, section, price, qty, status);
        } else {
            ticket = ticketToEdit;
            ticket.setGroupName(group);
            ticket.setConcertTitle(title);
            ticket.setVenue(venue);
            ticket.setEventDate(date);
            ticket.setSeatSection(section);
            ticket.setPrice(price);
            ticket.setQuantity(qty);
            ticket.setStatus(status);
        }

        listener.onTicketSaved(ticket);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
