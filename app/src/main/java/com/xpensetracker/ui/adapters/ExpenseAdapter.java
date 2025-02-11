package com.xpensetracker.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.xpensetracker.R;
import com.xpensetracker.data.models.Expense;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ExpenseAdapter extends ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder> {
    private final OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Expense expense);
    }

    public ExpenseAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.mListener = listener;
    }

    private static final DiffUtil.ItemCallback<Expense> DIFF_CALLBACK = new DiffUtil.ItemCallback<Expense>() {
        @Override
        public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getAmount() == newItem.getAmount() &&
                    oldItem.getCategory().equals(newItem.getCategory()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense currentExpense = getItem(position);
        holder.bind(currentExpense, mListener);
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView amountTextView;
        private final TextView categoryTextView;
        private final TextView dateTextView;
        private final ImageView categoryIcon;
        private static final SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

        ExpenseViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            amountTextView = itemView.findViewById(R.id.text_view_amount);
            categoryTextView = itemView.findViewById(R.id.text_view_category);
            dateTextView = itemView.findViewById(R.id.text_view_date);
            categoryIcon = itemView.findViewById(R.id.image_view_category);
        }

        void bind(final Expense expense, final OnItemClickListener listener) {
            titleTextView.setText(expense.getTitle());
            amountTextView.setText(String.format(Locale.getDefault(), "₹%.2f", expense.getAmount()));
            categoryTextView.setText(expense.getCategory());
            dateTextView.setText(dateFormat.format(expense.getDate()));

            int iconResId = getCategoryIcon(expense.getCategory());
            categoryIcon.setImageResource(iconResId);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(expense);
                }
            });
        }

        private int getCategoryIcon(String category) {
            if (category == null) return R.drawable.ic_misc;

            switch (category.toLowerCase()) {
                case "food":
                    return R.drawable.ic_food;
                case "transport":
                    return R.drawable.ic_transport;
                case "shopping":
                    return R.drawable.ic_shopping;
                default:
                    return R.drawable.ic_misc;
            }
        }
    }
}