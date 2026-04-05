package com.example.finance.dashboard.service;

import com.example.finance.dashboard.dto.DashboardSummary;
import com.example.finance.dashboard.dto.TrendPoint;
import com.example.finance.record.dto.RecordResponse;
import com.example.finance.record.model.FinancialRecord;
import com.example.finance.record.model.RecordType;
import com.example.finance.record.repository.FinancialRecordRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final FinancialRecordRepository recordRepository;

    public DashboardService(FinancialRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummary getSummary() {
        BigDecimal totalIncome = recordRepository.sumByType(RecordType.INCOME);
        BigDecimal totalExpense = recordRepository.sumByType(RecordType.EXPENSE);

        Map<String, BigDecimal> incomeByCategory = new HashMap<>();
        Map<String, BigDecimal> expenseByCategory = new HashMap<>();
        for (Object[] row : recordRepository.categoryTotals()) {
            String category = (String) row[0];
            RecordType type = (RecordType) row[1];
            BigDecimal total = (BigDecimal) row[2];
            if (type == RecordType.INCOME) {
                incomeByCategory.put(category, total);
            } else {
                expenseByCategory.put(category, total);
            }
        }

        List<FinancialRecord> allRecords = recordRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
        List<TrendPoint> trends = buildTrend(allRecords, 6);
        List<RecordResponse> recent = recordRepository.findTop10ByOrderByDateDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new DashboardSummary(totalIncome, totalExpense, incomeByCategory, expenseByCategory, trends, recent);
    }

    private List<TrendPoint> buildTrend(List<FinancialRecord> records, int monthsBack) {
        Map<YearMonth, List<FinancialRecord>> byMonth = records.stream()
                .collect(Collectors.groupingBy(r -> YearMonth.from(r.getDate())));
        List<TrendPoint> points = new ArrayList<>();
        YearMonth current = YearMonth.now();
        for (int i = monthsBack - 1; i >= 0; i--) {
            YearMonth target = current.minusMonths(i);
            List<FinancialRecord> monthRecords = byMonth.getOrDefault(target, Collections.emptyList());
            BigDecimal income = monthRecords.stream()
                    .filter(r -> r.getType() == RecordType.INCOME)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal expense = monthRecords.stream()
                    .filter(r -> r.getType() == RecordType.EXPENSE)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            points.add(new TrendPoint(target.toString(), income, expense));
        }
        return points;
    }

    private RecordResponse toResponse(FinancialRecord record) {
        String createdBy = record.getCreatedBy() != null ? record.getCreatedBy().getUsername() : null;
        return new RecordResponse(record.getId(), record.getAmount(), record.getType(), record.getCategory(), record.getDate(), record.getNotes(), createdBy, record.getCreatedAt(), record.getUpdatedAt());
    }
}
