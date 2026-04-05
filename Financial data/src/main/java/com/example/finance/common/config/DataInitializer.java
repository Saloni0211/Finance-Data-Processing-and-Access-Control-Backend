package com.example.finance.common.config;

import com.example.finance.record.model.FinancialRecord;
import com.example.finance.record.model.RecordType;
import com.example.finance.user.model.Role;
import com.example.finance.user.model.User;
import com.example.finance.user.model.UserStatus;
import com.example.finance.record.repository.FinancialRecordRepository;
import com.example.finance.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository, FinancialRecordRepository recordRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User("admin", passwordEncoder.encode("admin123"), Role.ADMIN, UserStatus.ACTIVE);
                userRepository.save(admin);

                // Sample records for quick demo
                FinancialRecord salary = new FinancialRecord(new BigDecimal("5000"), RecordType.INCOME, "Salary", LocalDate.now().minusDays(3), "Monthly salary", admin);
                FinancialRecord groceries = new FinancialRecord(new BigDecimal("200"), RecordType.EXPENSE, "Groceries", LocalDate.now().minusDays(2), "Supermarket", admin);
                FinancialRecord stocks = new FinancialRecord(new BigDecimal("150"), RecordType.INCOME, "Investments", LocalDate.now().minusDays(10), "Dividend", admin);
                recordRepository.save(salary);
                recordRepository.save(groceries);
                recordRepository.save(stocks);
            }
        };
    }
}
