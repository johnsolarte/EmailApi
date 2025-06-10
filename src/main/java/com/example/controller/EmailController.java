package com.example.controller;


import com.example.dto.EmailRequest;
import com.example.model.EmailLog;
import com.example.Repository.EmailLogRepository;
import com.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailLogRepository emailLogRepository;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request);
        return "Email(s) sent";
    }

    @GetMapping("/history")
    public List<EmailLog> getHistory(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return emailLogRepository.findAll().stream()
                .filter(log -> (status == null || log.getStatus().equalsIgnoreCase(status)))
                .filter(log -> (fromDate == null || !log.getSentAt().toLocalDate().isBefore(fromDate)))
                .filter(log -> (toDate == null || !log.getSentAt().toLocalDate().isAfter(toDate)))
                .collect(Collectors.toList());
    }
}
