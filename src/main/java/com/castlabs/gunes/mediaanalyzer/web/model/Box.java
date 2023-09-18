package com.castlabs.gunes.mediaanalyzer.web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Box {
    private final String boxType;
    private final int boxSize;
    private final List<Box> subBoxes;

    private byte[] data;

    public Box(String boxType, int boxSize) {
        this.boxType = boxType;
        this.boxSize = boxSize;
        this.subBoxes = new ArrayList<>();
    }

    public void addSubBox(Box subBox) {
        subBoxes.add(subBox);
    }
}
