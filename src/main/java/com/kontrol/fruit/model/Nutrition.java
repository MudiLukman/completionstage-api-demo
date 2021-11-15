package com.kontrol.fruit.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Nutrition {
    private double carbohydrates;
    private double protein;
    private double fat;
    private double calories;
    private double sugar;
}
