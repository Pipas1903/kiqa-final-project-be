package com.school.kiqa.controller;

import com.school.kiqa.adapter.MigrationAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MigrationController {
    final MigrationAdapter migrationAdapter;

    @GetMapping("/makeup-api-to-kiqa-api")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<String> getProductsFromApi() {
        log.info("Received request to migrate products from MUA api to kiqa api");
        migrationAdapter.migrateApiToKiqaApi();
        log.info("returned products from api successfully");
        return ResponseEntity.ok("Processing started");
    }
}
