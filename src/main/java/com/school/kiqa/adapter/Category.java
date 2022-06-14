package com.school.kiqa.adapter;

import lombok.Getter;

@Getter
public enum Category {
    nail_polish("nails"),
    blush("face"),
    lipstick("lips"),
    mascara("eyes"),
    lip_liner("lips"),
    foundation("face"),
    eyeshadow("eyes"),
    eyeliner("eyes"),
    eyebrow("eyes"),
    bronzer("face");

    private final String CATEGORY;

    Category(String category) {
        this.CATEGORY = category;
    }
}
