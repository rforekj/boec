package com.BOEC.model.processing.order;

import com.BOEC.model.AbstractEntity;
import com.BOEC.model.processing.shipment.Shipment;
import com.BOEC.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    private double total;

    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "id")
    private Shipment shipment;

    private OrderStatus status;

    private String phone;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;


    public enum OrderStatus {
        WAIT_FOR_APPROVE,
        APPROVED,
        SHIPPING,
        PAID_BY_CARD,
        CANCEL
    }
}
