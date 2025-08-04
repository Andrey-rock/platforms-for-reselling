package ru.skypro.homework.dto;

public class Constants {
    public static final Ad AD1 = new Ad(1, "image", 2, 20, "Ad1");
    public static final Ad AD2 = new Ad(2, "image", 3, 30, "Ad2");
    public static final ExtendedAd EXTENDED_AD = new ExtendedAd(10, "Иван", "Иванов",
            "ivan@gmail.com", "link", "8(999)1111111", 1000, "Ad123");

    public static final Ads CONSTANT_ADS = new Ads(2, new Ad[]{AD1, AD2});

}

