package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для получения объявлений.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class Ads {

    private int count;
    private Ad[] results;

    public Ads(int count, Ad[] results) {
        this.count = count;
        this.results = results;
    }
}
