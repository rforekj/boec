package com.BOEC.model.processing;

import com.BOEC.model.AbstractEntity;
import com.BOEC.model.item.Item;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Product extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    private double price;

    private String description;

    private float salePercent;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;


}
