package com.example.finance.record.controller;

import com.example.finance.record.dto.RecordCreateRequest;
import com.example.finance.record.dto.RecordResponse;
import com.example.finance.record.dto.RecordUpdateRequest;
import com.example.finance.record.model.RecordType;
import com.example.finance.user.model.User;
import com.example.finance.record.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    private final FinancialRecordService recordService;

    public RecordController(FinancialRecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public List<RecordResponse> listRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return recordService.findRecords(type, category, startDate, endDate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RecordResponse createRecord(@Valid @RequestBody RecordCreateRequest request, @AuthenticationPrincipal User user) {
        return recordService.createRecord(request, user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RecordResponse updateRecord(@PathVariable Long id, @Valid @RequestBody RecordUpdateRequest request) {
        return recordService.updateRecord(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
    }
}
