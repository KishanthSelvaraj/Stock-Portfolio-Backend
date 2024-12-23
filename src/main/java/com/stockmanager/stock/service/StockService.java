package com.stockmanager.stock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.stockmanager.stock.model.Stock;
import com.stockmanager.stock.repository.StockRepository;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockPriceService stockPriceService;

    private static final List<String> AVAILABLE_STOCKS = List.of("AAPL", "GOOG", "MSFT", "AMZN", "TSLA");

    public StockService(StockRepository stockRepository, StockPriceService stockPriceService) {
        this.stockRepository = stockRepository;
        this.stockPriceService = stockPriceService;
    }

    // Add a new stock to portfolio
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    // Get all stocks bought by the user
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Get stock by ID
    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    // Update stock details
    public Stock updateStock(Long id, Stock stockDetails) {
        Stock stock = getStockById(id);
        stock.setName(stockDetails.getName());
        stock.setTicker(stockDetails.getTicker());
        stock.setQuantity(stockDetails.getQuantity());
        stock.setBuyPrice(stockDetails.getBuyPrice());
        return stockRepository.save(stock);
    }

    // Delete stock
    public void deleteStock(Long id) {
        Stock stock = getStockById(id);
        stockRepository.delete(stock);
    }

    // Calculate total portfolio value
    public BigDecimal calculateTotalPortfolioValue() {
        // Retrieve all stocks from the repository
        List<Stock> stocks = stockRepository.findAll();
    
        // If no stocks are found, return zero
        if (stocks.isEmpty()) {
            return BigDecimal.ZERO;
        }
    
        // Calculate the total portfolio value by summing the value of each stock
        return stocks.stream()
                .map(stock -> {
                    // Get the current price of the stock
                    Double currentPrice = stockPriceService.getStockPrice(stock.getTicker());
    
                    // If the price is null, assume it's zero (or handle the error appropriately)
                    if (currentPrice == null) {
                        currentPrice = 0.0; // You could throw an exception or log an error here if needed
                    }
    
                    // Return the stock value as BigDecimal (currentPrice * quantity)
                    return BigDecimal.valueOf(currentPrice).multiply(BigDecimal.valueOf(stock.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up the stock values to get the total portfolio value
    }
    

    // Get the top-performing stock based on real-time prices
    public Stock getTopStock() {
        List<Stock> stocks = stockRepository.findAll();
        if (stocks.isEmpty()) {
            throw new RuntimeException("No stocks available in the portfolio. Please add some stocks first.");
        }
    
        return stocks.stream()
                .max((stock1, stock2) -> {
                    // Get the stock prices as Doubles
                    Double price1 = stockPriceService.getStockPrice(stock1.getTicker());
                    Double price2 = stockPriceService.getStockPrice(stock2.getTicker());
    
                    // If the price is null, default to 0.0
                    price1 = (price1 != null) ? price1 : 0.0;
                    price2 = (price2 != null) ? price2 : 0.0;
    
                    // Compare the prices
                    return Double.compare(price1, price2);
                })
                .orElseThrow(() -> new RuntimeException("Failed to determine the top-performing stock."));
    }
    
    
    // Add 5 random stocks to a new user's portfolio
    public List<Stock> addRandomStocksToNewUser() {
        Random rand = new Random();
        List<Stock> newStocks = new ArrayList<>();
    
        // Select 5 random stocks from the predefined list
        for (int i = 0; i < 5; i++) {
            String randomTicker = AVAILABLE_STOCKS.get(rand.nextInt(AVAILABLE_STOCKS.size()));
            Stock stock = new Stock();
            stock.setTicker(randomTicker);
            stock.setQuantity(1);  // Each random stock has a quantity of 1
            stock.setName(randomTicker);  // Set name same as ticker for simplicity
    
            // Fetch the stock price as Double
            Double price = stockPriceService.getStockPrice(randomTicker);
    
            // Check if the price is valid (non-null) and set the buy price
            if (price != null) {
                stock.setBuyPrice(BigDecimal.valueOf(price));  // Set the price as BigDecimal
            } else {
                stock.setBuyPrice(BigDecimal.ZERO);  // Default to 0 if price is null
            }
    
            newStocks.add(stockRepository.save(stock));  // Save the stock to repository
        }
    
        return newStocks;
    }
    
}
