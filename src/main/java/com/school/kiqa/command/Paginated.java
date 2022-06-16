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
@NoArgsConstructor
public class Paginated<T> {
    private List<T> paginatedItems;
    private int itemsPerPage;
    private int pageNumber;
    private int totalNumberOfPages;
    private long totalNumberOfProducts;

    public Paginated(List<T> paginatedItems, int itemsPerPage, int pageNumber, int totalNumberOfPages, long totalNumberOfProducts) {
        this.paginatedItems = paginatedItems;
        this.itemsPerPage = itemsPerPage;
        this.pageNumber = pageNumber + 1;
        this.totalNumberOfPages = totalNumberOfPages;
        this.totalNumberOfProducts = totalNumberOfProducts;
    }
}
