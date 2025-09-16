package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/law")
@AllArgsConstructor
public class LawAnalyzerController {
    private final LawAnalyzerService analyzerService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeLaw(@RequestBody LawAnalyzerRequest request) {
        String response = analyzerService.analyzeLaw(request);
        return ResponseEntity.ok(response);
    }
}
