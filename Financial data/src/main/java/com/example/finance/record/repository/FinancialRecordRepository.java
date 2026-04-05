package com.example.finance.record.repository;

import com.example.finance.record.model.FinancialRecord;
import com.example.finance.record.model.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long>, JpaSpecificationExecutor<FinancialRecord> {
    @Query("select coalesce(sum(fr.amount), 0) from FinancialRecord fr where fr.type = :type")
    BigDecimal sumByType(@Param("type") RecordType type);

    @Query("select fr.category as category, fr.type as type, sum(fr.amount) as total from FinancialRecord fr group by fr.category, fr.type")
    List<Object[]> categoryTotals();

    List<FinancialRecord> findTop10ByOrderByDateDesc();
}
