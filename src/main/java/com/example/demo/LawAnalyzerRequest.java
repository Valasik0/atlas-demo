package com.example.demo;

import lombok.Data;
import java.util.List;

@Data
public class LawAnalyzerRequest {
    private String currentText;
    private List<LawHistory> history;
}
