package com.example.tiketkonser;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiketkonser.databinding.ItemTicketBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class TicketAdapter extends ListAdapter<ConcertTicket, TicketAdapter.TicketViewHolder> {

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ConcertTicket ticket);
    }

    public TicketAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<ConcertTicket> DIFF_CALLBACK = new DiffUtil.ItemCallback<ConcertTicket>() {
        @Override
        public boolean areItemsTheSame(@NonNull ConcertTicket oldItem, @NonNull ConcertTicket newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ConcertTicket oldItem, @NonNull ConcertTicket newItem) {
            return oldItem.getGroupName().equals(newItem.getGroupName()) &&
                    oldItem.getConcertTitle().equals(newItem.getConcertTitle()) &&
                    oldItem.getStatus().equals(newItem.getStatus());
        }
    };

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTicketBinding binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        private final ItemTicketBinding binding;

        public TicketViewHolder(ItemTicketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ConcertTicket ticket, OnItemClickListener listener) {
            binding.tvGroupName.setText(ticket.getGroupName());
            binding.tvConcertTitle.setText(ticket.getConcertTitle());
            binding.tvVenue.setText(ticket.getVenue());
            binding.tvDate.setText(ticket.getEventDate());
            binding.tvSection.setText(ticket.getSeatSection());
            binding.tvQuantity.setText(ticket.getQuantity() + "x");
            
            long totalPrice = ticket.getPrice() * ticket.getQuantity();
            binding.tvPrice.setText(formatRupiah(totalPrice));

            binding.tvGroupAvatar.setText(ticket.getGroupName().substring(0, Math.min(ticket.getGroupName().length(), 4)));
            
            // Status Badge
            binding.tvStatusBadge.setText(ticket.getStatus());
            int statusColor = getStatusColor(ticket.getStatus(), itemView.getContext());
            binding.tvStatusBadge.setBackgroundTintList(ColorStateList.valueOf(statusColor));

            // Group Color
            int groupColor = getGroupColor(ticket.getGroupName(), itemView.getContext());
            binding.groupColorStrip.setBackgroundColor(groupColor);
            binding.tvGroupAvatar.setBackgroundTintList(ColorStateList.valueOf(groupColor));
            binding.tvPrice.setTextColor(groupColor);

            itemView.setOnClickListener(v -> listener.onItemClick(ticket));
        }

        private String formatRupiah(long amount) {
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            return format.format(amount);
        }

        private int getStatusColor(String status, Context context) {
            switch (status) {
                case "Paid": return ContextCompat.getColor(context, R.color.status_paid);
                case "Pending": return ContextCompat.getColor(context, R.color.status_pending);
                case "Used": return ContextCompat.getColor(context, R.color.status_used);
                default: return ContextCompat.getColor(context, R.color.primary);
            }
        }

        private int getGroupColor(String group, Context context) {
            switch (group.toUpperCase()) {
                case "ITZY": return ContextCompat.getColor(context, R.color.itzy);
                case "AESPA": return ContextCompat.getColor(context, R.color.aespa);
                case "EXO": return ContextCompat.getColor(context, R.color.exo);
                case "TWICE": return ContextCompat.getColor(context, R.color.twice);
                case "NEWJEANS": return ContextCompat.getColor(context, R.color.newjeans);
                case "BLACKPINK": return ContextCompat.getColor(context, R.color.blackpink);
                case "STRAY KIDS": return ContextCompat.getColor(context, R.color.straykids);
                case "SEVENTEEN": return ContextCompat.getColor(context, R.color.seventeen);
                default: return ContextCompat.getColor(context, R.color.primary);
            }
        }
    }
}
