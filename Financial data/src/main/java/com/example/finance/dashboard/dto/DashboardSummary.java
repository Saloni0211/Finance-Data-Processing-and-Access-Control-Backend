package com.example.finance.dashboard.dto;

import com.example.finance.record.dto.RecordResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardSummary {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private Map<String, BigDecimal> incomeByCategory;
    private Map<String, BigDecimal> expenseByCategory;
    private List<TrendPoint> monthlyTrend;
    private List<RecordResponse> recentActivity;

    public DashboardSummary() {
    }

    public DashboardSummary(BigDecimal totalIncome, BigDecimal totalExpense, Map<String, BigDecimal> incomeByCategory, Map<String, BigDecimal> expenseByCategory, List<TrendPoint> monthlyTrend, List<RecordResponse> recentActivity) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = totalIncome.subtract(totalExpense);
        this.incomeByCategory = incomeByCategory;
        this.expenseByCategory = expenseByCategory;
        this.monthlyTrend = monthlyTrend;
        this.recentActivity = recentActivity;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
        recalcNet();
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
        recalcNet();
    }

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }

    public Map<String, BigDecimal> getIncomeByCategory() {
        return incomeByCategory;
    }

    public void setIncomeByCategory(Map<String, BigDecimal> incomeByCategory) {
        this.incomeByCategory = incomeByCategory;
    }

    public Map<String, BigDecimal> getExpenseByCategory() {
        return expenseByCategory;
    }

    public void setExpenseByCategory(Map<String, BigDecimal> expenseByCategory) {
        this.expenseByCategory = expenseByCategory;
    }

    public List<TrendPoint> getMonthlyTrend() {
        return monthlyTrend;
    }

    public void setMonthlyTrend(List<TrendPoint> monthlyTrend) {
        this.monthlyTrend = monthlyTrend;
    }

    public List<RecordResponse> getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(List<RecordResponse> recentActivity) {
        this.recentActivity = recentActivity;
    }

    private void recalcNet() {
        if (totalIncome != null && totalExpense != null) {
            this.netBalance = totalIncome.subtract(totalExpense);
        }
    }
}
