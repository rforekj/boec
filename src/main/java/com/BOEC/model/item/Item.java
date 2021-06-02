package com.BOEC.model.item;

import com.BOEC.model.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@Entity
public class Item extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;
    private String name;
    private double price;
    private int numberInStock;
    private String description;
    private String image;
    private boolean isPublish;
}
