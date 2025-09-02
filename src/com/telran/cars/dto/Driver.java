package com.telran.cars.dto;

import java.io.Serializable;
import java.util.Objects;

public class Driver implements Serializable {
        private long lisenseId;
        private String name;
        private int birthYear;
        private String phone;


    public Driver(long lisenseId, String name, int birthYear, String phone) {
        this.lisenseId = lisenseId;
        this.name = name;
        this.birthYear = birthYear;
        this.phone = phone;
    }

    public long getLisenseId() {
        return lisenseId;
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return lisenseId == driver.lisenseId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lisenseId);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "lisenseId=" + getLisenseId() +
                ", name='" + getName() + '\'' +
                ", birthYear=" + getBirthYear() +
                ", phone='" + getPhone() + '\'' +
                '}';
    }
}
