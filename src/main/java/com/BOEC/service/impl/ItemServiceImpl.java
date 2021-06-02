package com.BOEC.service.impl;

import com.BOEC.model.item.Item;
import com.BOEC.repository.ItemRepository;
import com.BOEC.service.ItemService;
import com.BOEC.service.dto.ItemImportDto;
import com.BOEC.service.dto.ItemRespondDto;
import com.BOEC.service.factory.ItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemFactory itemFactory;

    @Override
    public Map<String, Object> listItem(String keyword, int page, int size) {
        if (page < 1 || size < 1) return null;
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Item> pageTuts;
        if (keyword == null)
            pageTuts = itemRepository.findAllByOrderByCreatedDateDesc(paging);
        else
            pageTuts = itemRepository.findByNameIsContaining(keyword, paging);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", pageTuts.getNumber() + 1);
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        response.put("listItem", itemFactory.itemsToItemRespondDtos(pageTuts.getContent()));
        return response;
    }

    @Override
    public int saveItem(ItemImportDto item) {
        return itemRepository.save(itemFactory.itemImportDtoToItem(item)).getId();
    }

    @Override
    public int deleteItem(int id) {
        try {
            itemRepository.deleteById(id);
        } catch (Exception e) {
            return 0;
        }
        return id;
    }

    @Override
    public ItemRespondDto findById(int id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent())
            return itemFactory.itemToItemRespondDto(itemRepository.findById(id).get());
        return null;
    }
}
