package com.translator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.translator.service.TranslatorService;


@RestController
@RequestMapping("/api/translate")
public class TranslatorController {

    @Autowired
    private TranslatorService translatorService;

    @PostMapping
    public String translate(@RequestBody String payload) {
        return translatorService.translate(payload);
    }
}