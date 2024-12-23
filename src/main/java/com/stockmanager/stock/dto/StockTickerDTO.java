package com.stockmanager.stock.dto;

public class StockTickerDTO {

    private String ticker;
    private String name;

    // Constructor
    public StockTickerDTO(String ticker, String name) {
        this.ticker = ticker;
        this.name = name;
    }

    // Getters and Setters
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
