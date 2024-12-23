package com.stockmanager.stock.repository;

import com.stockmanager.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
