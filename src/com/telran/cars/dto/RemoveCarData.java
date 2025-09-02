package com.telran.cars.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RemoveCarData implements Serializable {
    Car car;
    List<RentRecord> removedRecords;


    public RemoveCarData() {
    }

    public RemoveCarData(Car car, List<RentRecord> removedRecords) {
        this.car = car;
        this.removedRecords = removedRecords;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<RentRecord> getRemovedRecords() {
        return removedRecords;
    }

    public void setRemovedRecords(List<RentRecord> removedRecords) {
        this.removedRecords = removedRecords;
    }

    @Override
    public String toString() {
        return "RemoveCarData{" +
                "car=" + getCar() +
                ", removedRecords=" + getRemovedRecords() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RemoveCarData that = (RemoveCarData) o;
        return Objects.equals(car, that.car) && Objects.equals(removedRecords, that.removedRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, removedRecords);
    }
}
