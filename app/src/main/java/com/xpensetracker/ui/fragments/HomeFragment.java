package com.xpensetracker.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xpensetracker.R;
import com.xpensetracker.data.models.Expense;
import com.xpensetracker.ui.adapters.ExpenseAdapter;
import com.xpensetracker.viewmodels.ExpenseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements ExpenseAdapter.OnItemClickListener {
    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter adapter;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExpenseAdapter(this);
        recyclerView.setAdapter(adapter);

        // Setup PieChart
        pieChart = view.findViewById(R.id.pie_chart);
        setupPieChart();

        // Setup ViewModel
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), this::updateData);

        return view;
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(16f);
        pieChart.getLegend().setEnabled(true);
    }

    private void updateData(List<Expense> expenses) {
        adapter.submitList(expenses);
        updatePieChart(expenses);
    }

    private void updatePieChart(List<Expense> expenses) {
        // Group expenses by category
        Map<String, Float> categoryTotals = new HashMap<>();
        float totalAmount = 0;

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            float amount = (float) expense.getAmount();

            // Update category total
            if (categoryTotals.containsKey(category)) {
                categoryTotals.put(category, categoryTotals.get(category) + amount);
            } else {
                categoryTotals.put(category, amount);
            }

            totalAmount += amount;
        }

        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            float percentage = (entry.getValue() / totalAmount) * 100;
            entries.add(new PieEntry(percentage, entry.getKey()));
        }

        // Setup pie data
        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public void onItemClick(Expense expense) {
        // Handle expense item click
        // TODO: Implement expense detail view
    }
}