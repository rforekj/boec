package com.BOEC.service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookRespondDto extends ItemRespondDto {
    private String author;
    private String publisher;
    private Date publishDate;
}
