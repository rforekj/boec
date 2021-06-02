package com.BOEC.service;

import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.exception.OutOfStockException;
import com.BOEC.service.exception.ProductNotInCartException;

public interface CartService {
    void addProduct(int product) throws NotFoundIdException, OutOfStockException;
    void removeProduct(int product) throws NotFoundIdException, ProductNotInCartException;
}
