package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для получения информации об объявлении.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class ExtendedAd {

    private int pk;
    private String authorFirstName;
    private String authorLastName;
    private String maile;
    private String image;
    private String phone;
    private int price;
    private String title;
}
