package com.stockmanager.stock.controller;

import com.stockmanager.stock.model.Stock;
import com.stockmanager.stock.dto.StockTickerDTO;
import com.stockmanager.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.addStock(stock));
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock stockDetails) {
        return ResponseEntity.ok(stockService.updateStock(id, stockDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portfolio-value")
    public ResponseEntity<BigDecimal> getTotalPortfolioValue() {
        BigDecimal totalPortfolioValue = stockService.calculateTotalPortfolioValue();
        return ResponseEntity.ok(totalPortfolioValue);
    }

    @GetMapping("/top-stock")
    public ResponseEntity<Stock> getTopStock() {
        return ResponseEntity.ok(stockService.getTopStock());
    }

    @PostMapping("/random-stocks")
    public ResponseEntity<List<Stock>> addRandomStocksToNewUser() {
        List<Stock> newStocks = stockService.addRandomStocksToNewUser();
        return ResponseEntity.ok(newStocks);
    }

    @GetMapping("/available-tickers")
    public ResponseEntity<List<StockTickerDTO>> getAvailableTickers() {
        List<StockTickerDTO> availableStocks = List.of(
                new StockTickerDTO("AAPL", "Apple Inc."),
                new StockTickerDTO("GOOG", "Alphabet Inc. (Google)"),
                new StockTickerDTO("MSFT", "Microsoft Corporation"),
                new StockTickerDTO("AMZN", "Amazon.com, Inc."),
                new StockTickerDTO("TSLA", "Tesla, Inc.")
        );
        return ResponseEntity.ok(availableStocks);
    }
    
}
