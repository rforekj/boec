package com.BOEC.service.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ItemImportDto {
    private int id;
    private String name;
    private double price;
    private int numberInStock;
    private String description;

    private String author;
    private String publisher;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    private String brand;
    private String size;
    private String type;

    private String manufacturer;
    private String manufacturedYear;

    @NotNull
    private ItemType itemType;

    private MultipartFile image;

    public enum ItemType {
        BOOK,
        CLOTHES,
        ELECTRONIC
    }
}
