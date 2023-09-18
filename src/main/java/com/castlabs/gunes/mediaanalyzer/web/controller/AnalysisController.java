package com.castlabs.gunes.mediaanalyzer.web.controller;

import com.castlabs.gunes.mediaanalyzer.service.AnalysisService;
import com.castlabs.gunes.mediaanalyzer.web.model.Box;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController("/api")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/media-data")
    public ResponseEntity<List<Box>> getMediaData(@RequestParam("url") String mediaFileUrl) throws IOException {
        return new ResponseEntity<>(analysisService.getMediaData(mediaFileUrl), HttpStatus.OK);
    }
}
