package com.telran.cars.model;

import java.io.Serializable;

public abstract class AbstractRentCompany implements IRentCompany {
    protected int finePercent = 15;
    protected int gasPrice = 10;

    public int getFinePercent() {
        return finePercent;
    }

    public void setFinePercent(int finePercent) {
        this.finePercent = finePercent;
    }

    public int getGasPrice() {
        return gasPrice;
    }

    public void GasPrice(int gasPrice) {
        this.gasPrice = gasPrice;
    }

    protected double computeCost(int rentPricePerDay, int rentDays, int delay, int tankPercent, int tankVolume) {
        double cost = rentDays * rentPricePerDay;
        if (delay > 0) cost += additionalDelayCost(delay, rentPricePerDay);
        if (tankPercent < 100) cost += additionalGasCost(tankPercent, tankVolume);
        return cost;
    }

    private double additionalGasCost(int tankPercent, int tankVolume) {
        return tankVolume * ((100. - tankPercent)/100)*gasPrice;
    }

    private double additionalDelayCost(int delay, int rentPricePerDay) {
        return delay * rentPricePerDay * (1 + finePercent/100.);
    }


}
