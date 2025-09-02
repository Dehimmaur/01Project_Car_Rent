package com.telran.cars.dto;

import com.telran.cars.dto.enums.State;

import java.io.Serializable;
import java.util.Objects;

public class Car implements Serializable {
    private String regNumber;
    private String color;
    private State state = State.EXCELLENT;
    private String modelName;
    private boolean inUse;
    private boolean flRemove;

    public Car() {
    }

    public Car(String regNumber, String modelName, String color) {
        this.regNumber = regNumber;
        this.modelName = modelName;
        this.color = color;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public void setFlRemove(boolean flRemove) {
        this.flRemove = flRemove;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getColor() {
        return color;
    }

    public State getState() {
        return state;
    }

    public String getModelName() {
        return modelName;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean isFlRemove() {
        return flRemove;
    }

    @Override
    public String toString() {
        return "Car{" +
                "regNumber='" + getRegNumber() + '\'' +
                ", color='" + getColor() + '\'' +
                ", state=" + getState() +
                ", modelName='" + getModelName() + '\'' +
                ", inUse=" + isInUse() +
                ", flRemove=" + isFlRemove() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return inUse == car.inUse && flRemove == car.flRemove && Objects.equals(regNumber, car.regNumber) && Objects.equals(color, car.color) && state == car.state && Objects.equals(modelName, car.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, color, state, modelName, inUse, flRemove);
    }
}
