package com.telran.cars.utils;

import java.io.IOException;

public interface Persistable {
    void save(String fileName) throws IOException;
}
