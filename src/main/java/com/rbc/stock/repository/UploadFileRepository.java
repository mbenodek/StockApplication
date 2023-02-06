package com.rbc.stock.repository;

import com.rbc.stock.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface  UploadFileRepository  extends MongoRepository<Stock,Integer>{

    public List<Stock> findByStock(String stockName);

}
