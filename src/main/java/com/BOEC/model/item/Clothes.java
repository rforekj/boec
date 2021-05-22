package com.BOEC.model.item;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Clothes extends Item{
    private String brand;
    private String size;
    private String type;
}
