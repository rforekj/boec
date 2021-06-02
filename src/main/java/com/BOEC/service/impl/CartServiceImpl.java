package com.BOEC.service.impl;

import com.BOEC.model.processing.cart.Cart;
import com.BOEC.model.processing.cart.CartProduct;
import com.BOEC.model.processing.Product;
import com.BOEC.repository.*;
import com.BOEC.service.CartService;
import com.BOEC.service.exception.NotFoundIdException;
import com.BOEC.service.exception.OutOfStockException;
import com.BOEC.service.exception.ProductNotInCartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartProductRepository cartProductRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Override
    public void addProduct(int productId) throws NotFoundIdException, OutOfStockException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        int cartId = cartRepository.findByCustomer_Id(userRepository.findByEmail(username).get().getId()).getId();
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        Optional<Product> productOptional = productRepository.findById(productId);
        int numberInStock = 0;
        if (cartOptional.isPresent() && productOptional.isPresent()) {
            numberInStock = productOptional.get().getItem().getNumberInStock();
            if (numberInStock < 1)
                throw new OutOfStockException();
            Optional<CartProduct> cartProduct = cartProductRepository.findByCart_IdAndProduct_Id(cartId, productId);
            if (cartProduct.isPresent()) {
                cartProduct.get().setQuantity(cartProduct.get().getQuantity() + 1);
                cartProductRepository.save(cartProduct.get());
            } else {
                CartProduct newCartProduct = new CartProduct();
                Product product = new Product();
                product.setId(productId);
                newCartProduct.setProduct(product);
                Cart cart = new Cart();
                cart.setId(cartId);
                newCartProduct.setCart(cart);
                newCartProduct.setQuantity(1);
                cartProductRepository.save(newCartProduct);
            }
        } else {
            throw new NotFoundIdException();
        }

        productOptional.get().getItem().setNumberInStock(numberInStock - 1);
        productRepository.save(productOptional.get());
    }

    @Override
    public void removeProduct(int productId) throws NotFoundIdException, ProductNotInCartException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        int cartId = cartRepository.findByCustomer_Id(userRepository.findByEmail(username).get().getId()).getId();

        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()) throw new NotFoundIdException();
        Optional<CartProduct> cartProduct = cartProductRepository.findByCart_IdAndProduct_Id(cartId,productId);
        if(!cartProduct.isPresent()) throw new ProductNotInCartException();
        product.get().getItem().setNumberInStock(product.get().getItem().getNumberInStock() + cartProduct.get().getQuantity());
        itemRepository.save(product.get().getItem());
        cartProductRepository.deleteByCart_IdAndProduct_Id(cartId, productId);
    }

}
