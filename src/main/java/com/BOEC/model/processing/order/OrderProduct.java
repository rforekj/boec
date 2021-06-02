package com.BOEC.model.processing.order;

import com.BOEC.model.processing.Product;
import com.BOEC.model.processing.cart.Cart;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private int quantity;
}
