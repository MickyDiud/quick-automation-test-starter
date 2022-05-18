package com.catalog.store.example.utils;

import org.openqa.selenium.WebDriver;

public class StringUtilities {

    public static String getItemNo(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
