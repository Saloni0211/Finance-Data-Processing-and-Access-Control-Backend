package com.example.finance.record.service;

import com.example.finance.record.dto.RecordCreateRequest;
import com.example.finance.record.dto.RecordResponse;
import com.example.finance.record.dto.RecordUpdateRequest;
import com.example.finance.common.exception.ResourceNotFoundException;
import com.example.finance.record.model.FinancialRecord;
import com.example.finance.record.model.RecordType;
import com.example.finance.user.model.User;
import com.example.finance.record.repository.FinancialRecordRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class FinancialRecordService {
    private final FinancialRecordRepository recordRepository;

    public FinancialRecordService(FinancialRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Transactional
    public RecordResponse createRecord(RecordCreateRequest request, User createdBy) {
        FinancialRecord record = new FinancialRecord();
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());
        record.setCreatedBy(createdBy);
        FinancialRecord saved = recordRepository.save(record);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RecordResponse> findRecords(RecordType type, String category, LocalDate startDate, LocalDate endDate) {
        Specification<FinancialRecord> spec = Specification.where(null);
        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
        }
        if (startDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), startDate));
        }
        if (endDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), endDate));
        }
        return recordRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "date")).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecordResponse updateRecord(Long id, RecordUpdateRequest request) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + id));
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());
        FinancialRecord saved = recordRepository.save(record);
        return toResponse(saved);
    }

    @Transactional
    public void deleteRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + id));
        recordRepository.delete(record);
    }

    @Transactional(readOnly = true)
    public FinancialRecord getById(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found: " + id));
    }

    private RecordResponse toResponse(FinancialRecord record) {
        String createdBy = record.getCreatedBy() != null ? record.getCreatedBy().getUsername() : null;
        return new RecordResponse(record.getId(), record.getAmount(), record.getType(), record.getCategory(), record.getDate(), record.getNotes(), createdBy, record.getCreatedAt(), record.getUpdatedAt());
    }
}
