package com.xpensetracker.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.xpensetracker.data.models.Expense;
import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    long insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByCategory(String category);

    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalExpenseForPeriod(long startDate, long endDate);

    @Query("SELECT category, SUM(amount) as total FROM expenses GROUP BY category")
    LiveData<List<CategorySum>> getExpenseSumByCategory();

    static class CategorySum {
        public String category;
        public double total;
    }
}