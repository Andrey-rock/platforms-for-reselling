package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO для получения комментариев объявления.
 *
 * @author Andrei Bronskii, 2025
 * @version 0.0.1
 */
@Data
public class Comments {

    private int count;
    private Comment[] results;
}
