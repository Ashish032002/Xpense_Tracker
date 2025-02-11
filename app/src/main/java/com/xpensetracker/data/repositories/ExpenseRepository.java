package com.xpensetracker.data.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.xpensetracker.data.dao.ExpenseDao;
import com.xpensetracker.data.database.AppDatabase;
import com.xpensetracker.data.models.Expense;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private final ExpenseDao expenseDao;
    private final LiveData<List<Expense>> allExpenses;
    private final ExecutorService executorService;

    public ExpenseRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Expense expense) {
        executorService.execute(() -> expenseDao.insert(expense));
    }

    public void update(Expense expense) {
        executorService.execute(() -> expenseDao.update(expense));
    }

    public void delete(Expense expense) {
        executorService.execute(() -> expenseDao.delete(expense));
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return expenseDao.getExpensesByCategory(category);
    }

    public LiveData<Double> getTotalExpenseForPeriod(long startDate, long endDate) {
        return expenseDao.getTotalExpenseForPeriod(startDate, endDate);
    }

    public LiveData<List<ExpenseDao.CategorySum>> getExpenseSumByCategory() {
        return expenseDao.getExpenseSumByCategory();
    }
}