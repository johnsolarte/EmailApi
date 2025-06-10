package com.example.dto;

import java.util.List;

import lombok.*;
@Data
public class EmailRequest {
    private List<String> to;
    private String subject;
    private String body;
    private boolean isHtml;
}
