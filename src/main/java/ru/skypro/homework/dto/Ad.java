package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для объявлений.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class Ad {

    private int id;
    private String image;
    private int pk;
    private int price;
    private String title;

    public Ad(int id, String image, int pk, int price, String title) {
        this.id = id;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }
}
