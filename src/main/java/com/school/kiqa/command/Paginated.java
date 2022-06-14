package com.school.kiqa.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paginated<T> {
    private List<T> paginatedItems;
    private int itemsPerPage;
    private int pageNumber;
    private int totalNumberOfPages;
    private long totalNumberOfProducts;
}
