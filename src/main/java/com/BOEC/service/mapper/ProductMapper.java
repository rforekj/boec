package com.BOEC.service.mapper;

import com.BOEC.model.item.Item;
import com.BOEC.model.processing.Product;
import com.BOEC.repository.ItemRepository;
import com.BOEC.service.dto.ProductCreateDto;
import com.BOEC.service.dto.ProductRespondDto;
import com.BOEC.service.exception.ItemIsPublicException;
import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.factory.ItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductMapper {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemFactory itemFactory;

    public List<ProductRespondDto> productsToProductDtos(List<Product> products) {
        return products.stream().map(this::productToProductDto).collect(Collectors.toList());
    }

    public ProductRespondDto productToProductDto(Product product) {
        ProductRespondDto productRespondDto = new ProductRespondDto();
        productRespondDto.setId(product.getId());
        productRespondDto.setPrice(product.getPrice());
        productRespondDto.setSalePercent(product.getSalePercent());
        productRespondDto.setDescription(product.getDescription());
        productRespondDto.setItem(itemFactory.itemToItemRespondDto(product.getItem()));
        return productRespondDto;
    }

    public Product productDtoToProduct(ProductCreateDto productCreateDto) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Product product = new Product();
        product.setSalePercent(productCreateDto.getSalePercent());
        product.setDescription(productCreateDto.getDescription());
        product.setPrice(productCreateDto.getPrice());
        product.setCreatedBy(username);
        Optional<Item> item = itemRepository.findById(productCreateDto.getItemId());
        if (item.isPresent()) {
            product.setItem(item.get());
            if (item.get().isPublish()) {
                throw new ItemIsPublicException();
            }
        } else
            throw new NotFoundIdException();
        product.getItem().setPublish(true);
        return product;
    }
}
