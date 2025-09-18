package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/analyze-json")
    @ResponseBody
    public List<Map<String, Object>> analyzeJson(@RequestBody LawAnalyzerRequest request) {
        String jsonString = analyzerService.analyzeLaw(request);

        try {
            jsonString = jsonString
                    .replaceAll("`", "")
                    .replaceAll("json", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, List.class);
        } catch (Exception e) {
            return List.of(Map.of("date", "error", "change", e.getMessage(), "raw", jsonString));
        }
    }
}
