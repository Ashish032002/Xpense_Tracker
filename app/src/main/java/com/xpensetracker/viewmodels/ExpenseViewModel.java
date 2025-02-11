package com.xpensetracker.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.xpensetracker.data.models.Expense;
import com.xpensetracker.data.repositories.ExpenseRepository;
import com.xpensetracker.data.dao.ExpenseDao;
import java.util.List;
import java.util.Date;

public class ExpenseViewModel extends AndroidViewModel {
    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void update(Expense expense) {
        repository.update(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return repository.getExpensesByCategory(category);
    }

    public LiveData<Double> getTotalExpenseForPeriod(long startDate, long endDate) {
        return repository.getTotalExpenseForPeriod(startDate, endDate);
    }

    public LiveData<List<ExpenseDao.CategorySum>> getExpenseSumByCategory() {
        return repository.getExpenseSumByCategory();
    }
}