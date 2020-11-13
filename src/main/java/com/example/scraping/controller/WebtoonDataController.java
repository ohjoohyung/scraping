package com.example.scraping.controller;

import com.example.scraping.model.Webtoon;
import com.example.scraping.service.WebtoonDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class WebtoonDataController {

    private final WebtoonDataService webtoonDataService;

    @GetMapping("/webtoon")
    public String webtoon(Model model) {
        Map<String, List<Webtoon>> map = webtoonDataService.getWebtoonList();
        model.addAttribute("map", map);
        return "Webtoon";
    }

}
