package com.school.kiqa.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ProductFromApi {

    private Double price;

    private String description;

    private String brand;

    private String name;

    private Category product_type;

    private Boolean isActive;

    private String image_link;

    private List<Color> product_colors;
}
