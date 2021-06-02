package com.BOEC.service;

import com.BOEC.service.dto.ItemImportDto;
import com.BOEC.service.dto.ItemRespondDto;

import java.util.Map;

public interface ItemService {
    Map<String, Object> listItem(String keyword, int page, int size);

    int saveItem(ItemImportDto item);

    int deleteItem(int id);

    ItemRespondDto findById(int id);

}
