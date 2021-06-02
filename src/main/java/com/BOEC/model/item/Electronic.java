package com.BOEC.model.item;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Electronic extends Item {
    private String manufacturer;
    private String manufacturedYear;
}
