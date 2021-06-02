package com.BOEC.service.impl;

import com.BOEC.model.processing.Product;
import com.BOEC.model.processing.cart.CartProduct;
import com.BOEC.repository.CartProductRepository;
import com.BOEC.repository.ItemRepository;
import com.BOEC.repository.ProductRepository;
import com.BOEC.service.ProductService;
import com.BOEC.service.dto.ProductCreateDto;
import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.exception.ProductNotInCartException;
import com.BOEC.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartProductRepository cartProductRepository;

    @Override
    public Map<String, Object> listProduct(String keyword, int page, int size) {
        if (page < 1 || size < 1) return null;
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Product> pageTuts;
        if (keyword == null)
            pageTuts = productRepository.findAllBy(paging);
        else
            pageTuts = productRepository.findAllByItem_NameIsContaining(keyword, paging);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", pageTuts.getNumber() + 1);
        response.put("totalProducts", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        response.put("listProduct", productMapper.productsToProductDtos(pageTuts.getContent()));
        return response;
    }

    @Override
    public int saveProduct(ProductCreateDto product) throws Exception {
        return productRepository.save(productMapper.productDtoToProduct(product)).getId();
    }

    @Override
    public int deleteProduct(int id) throws NotFoundIdException{
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            List<CartProduct> cartProducts = cartProductRepository.findByProduct_Id(id);
            for(CartProduct cartProduct:cartProducts) {
                product.get().getItem().setNumberInStock(product.get().getItem().getNumberInStock() + cartProduct.getQuantity());
            }
            cartProductRepository.deleteByProduct_Id(id);
            //itemRepository.updateIsPublic(product.get().getItem().getId());
            product.get().getItem().setPublish(false);
            itemRepository.save(product.get().getItem());
            productRepository.deleteById(id);
        } else {
            throw new NotFoundIdException();
        }
        return id;
    }
}
