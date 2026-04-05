package com.example.finance.dashboard.dto;

import java.math.BigDecimal;

public class TrendPoint {
    private String month; // yyyy-MM
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal net;

    public TrendPoint() {
    }

    public TrendPoint(String month, BigDecimal income, BigDecimal expense) {
        this.month = month;
        this.income = income;
        this.expense = expense;
        this.net = income.subtract(expense);
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
        recalc();
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
        recalc();
    }

    public BigDecimal getNet() {
        return net;
    }

    private void recalc() {
        if (income != null && expense != null) {
            this.net = income.subtract(expense);
        }
    }
}
