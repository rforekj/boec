package com.BOEC.model.item;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class Book extends Item {
    private String author;
    private String publisher;
    private Date publishDate;
}
