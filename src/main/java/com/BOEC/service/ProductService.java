package com.BOEC.service;


import com.BOEC.service.dto.ProductCreateDto;
import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.exception.ProductNotInCartException;

import java.util.Map;

public interface ProductService {
    Map<String, Object> listProduct(String keyword, int page, int size);

    int saveProduct(ProductCreateDto product) throws Exception;

    int deleteProduct(int id) throws NotFoundIdException;
}
