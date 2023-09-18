package com.castlabs.gunes.mediaanalyzer.service;

import com.castlabs.gunes.mediaanalyzer.web.model.Box;

import java.io.IOException;
import java.util.List;

public interface AnalysisService {
    List<Box> getMediaData(String mediaFileUrl) throws IOException;
}
