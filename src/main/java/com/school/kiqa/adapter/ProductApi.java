package com.school.kiqa.adapter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductApi {
    void migrateApiToKiqaApi();

    List<ProductFromApi> getProductsFromExternalApi();
}
