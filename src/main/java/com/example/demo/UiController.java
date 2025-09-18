package com.example.demo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class UiController {

    private final LawAnalyzerService analyzerService;

    @GetMapping("/")
    public String index(Model model) {
        LawAnalyzerRequest request = new LawAnalyzerRequest();
        request.setHistory(List.of(new LawHistory()));
        model.addAttribute("lawRequest", request);
        return "index";
    }

    @PostMapping("/analyze-ui")
    public String analyzeUI(@ModelAttribute("lawRequest") LawAnalyzerRequest request,
                            Model model) {
        String result = analyzerService.analyzeLaw(request);
        model.addAttribute("result", result);

        // Aby šablona znovu měla alespoň jednu prázdnou historii pro další POST
        if (request.getHistory() == null || request.getHistory().isEmpty()) {
            request.setHistory(List.of(new LawHistory()));
        }
        model.addAttribute("lawRequest", request);

        return "index";
    }
}
