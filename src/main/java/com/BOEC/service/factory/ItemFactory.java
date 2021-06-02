package com.BOEC.service.factory;

import com.BOEC.model.item.Book;
import com.BOEC.model.item.Clothes;
import com.BOEC.model.item.Electronic;
import com.BOEC.model.item.Item;
import com.BOEC.model.processing.Product;
import com.BOEC.repository.ItemRepository;
import com.BOEC.repository.ProductRepository;
import com.BOEC.service.dto.*;
import com.BOEC.service.util.MinioAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemFactory {

    @Autowired
    MinioAdapter minioAdapter;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ProductRepository productRepository;

    public Item itemImportDtoToItem(ItemImportDto importDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        if (importDto.getItemType().equals(ItemImportDto.ItemType.BOOK)) {
            Book item = new Book();
            item.setId(importDto.getId());
            item.setDescription(importDto.getDescription());
            item.setName(importDto.getName());
            if (importDto.getImage() != null)
                item.setImage(minioAdapter.uploadFile(importDto.getImage()));
            else {
                if (importDto.getId() != 0) {
                    item.setImage(itemRepository.findById(importDto.getId()).get().getImage());
                }
            }

            item.setNumberInStock(importDto.getNumberInStock());
            item.setPrice(importDto.getPrice());
            item.setAuthor(importDto.getAuthor());
            item.setPublishDate(importDto.getPublishDate());
            item.setPublisher(importDto.getPublisher());
            item.setCreatedBy(username);
            return item;
        } else if (importDto.getItemType().equals(ItemImportDto.ItemType.CLOTHES)) {
            Clothes item = new Clothes();
            item.setId(importDto.getId());
            item.setDescription(importDto.getDescription());
            item.setName(importDto.getName());
            if (importDto.getImage() != null)
                item.setImage(minioAdapter.uploadFile(importDto.getImage()));
            else {
                if (importDto.getId() != 0) {
                    item.setImage(itemRepository.findById(importDto.getId()).get().getImage());
                }
            }
            item.setNumberInStock(importDto.getNumberInStock());
            item.setPrice(importDto.getPrice());
            item.setBrand(importDto.getBrand());
            item.setSize(importDto.getSize());
            item.setType(importDto.getType());
            item.setCreatedBy(username);
            return item;
        } else {
            Electronic item = new Electronic();
            item.setId(importDto.getId());
            item.setDescription(importDto.getDescription());
            item.setName(importDto.getName());
            if (importDto.getImage() != null)
                item.setImage(minioAdapter.uploadFile(importDto.getImage()));
            else {
                if (importDto.getId() != 0) {
                    item.setImage(itemRepository.findById(importDto.getId()).get().getImage());
                }
            }
            item.setNumberInStock(importDto.getNumberInStock());
            item.setPrice(importDto.getPrice());
            item.setManufacturedYear(importDto.getManufacturedYear());
            item.setManufacturer(importDto.getManufacturer());
            item.setCreatedBy(username);

            return item;
        }
    }

    public ItemRespondDto itemToItemRespondDto(Item importDto) {
        if (importDto instanceof Book) {
            BookRespondDto item = new BookRespondDto();
            item.setId(importDto.getId());
            item.setDescription(importDto.getDescription());
            item.setName(importDto.getName());
            item.setImage(minioAdapter.getFile(importDto.getImage()));
            item.setNumberInStock(importDto.getNumberInStock());
            item.setPrice(importDto.getPrice());
            item.setAuthor(((Book) importDto).getAuthor());
            item.setPublisher(((Book) importDto).getPublisher());
            item.setPublishDate(((Book) importDto).getPublishDate());
            item.setItemType(ItemImportDto.ItemType.BOOK);
            item.setPublish(importDto.isPublish());
            Optional<Product> product = productRepository.findByItem_Id(importDto.getId());
            if (product.isPresent())
                item.setProductId(product.get().getId());
            return item;
        } else if (importDto instanceof Clothes) {
            ClothesRespondDto item = new ClothesRespondDto();
            item.setId(importDto.getId());
            item.setDescription(importDto.getDescription());
            item.setName(importDto.getName());
            item.setImage(minioAdapter.getFile(importDto.getImage()));
            item.setNumberInStock(importDto.getNumberInStock());
            item.setPrice(importDto.getPrice());
            item.setBrand(((Clothes) importDto).getBrand());
            item.setType(((Clothes) importDto).getType());
            item.setSize(((Clothes) importDto).getSize());
            item.setItemType(ItemImportDto.ItemType.CLOTHES);
            item.setPublish(importDto.isPublish());
            Optional<Product> product = productRepository.findByItem_Id(importDto.getId());
            if (product.isPresent())
                item.setProductId(product.get().getId());
            return item;
        } else if (importDto instanceof Electronic) {
            ElectronicRespondDto item = new ElectronicRespondDto();
            item.setId(importDto.getId());
            item.setDescription(importDto.getDescription());
            item.setName(importDto.getName());
            item.setImage(minioAdapter.getFile(importDto.getImage()));
            item.setNumberInStock(importDto.getNumberInStock());
            item.setPrice(importDto.getPrice());
            item.setManufacturer(((Electronic) importDto).getManufacturer());
            item.setManufacturedYear(((Electronic) importDto).getManufacturedYear());
            item.setItemType(ItemImportDto.ItemType.ELECTRONIC);
            item.setPublish(importDto.isPublish());
            Optional<Product> product = productRepository.findByItem_Id(importDto.getId());
            if (product.isPresent())
                item.setProductId(product.get().getId());
            return item;
        }
        return null;
    }

    public List<ItemRespondDto> itemsToItemRespondDtos(List<Item> items) {
        return items.stream().map(this::itemToItemRespondDto).collect(Collectors.toList());
    }
}
