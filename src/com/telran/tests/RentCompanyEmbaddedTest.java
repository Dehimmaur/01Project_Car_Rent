package com.telran.tests;


import com.telran.cars.dto.*;

import static com.telran.cars.dto.enums.CarsReturnCode.*;
import static org.junit.jupiter.api.Assertions.*;

import com.telran.cars.dto.enums.State;
import com.telran.cars.model.IRentCompany;
import com.telran.cars.model.RentCompanyEmbadded;
import com.telran.cars.utils.Persistable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class RentCompanyEmbaddedTest {
    final String MODEL_NAME = "Model1";
    final int GAS_TANK = 50;
    final int PRICE_PER_DAY = 200;
    final long LICENSE = 1000L;
    final String REG_NUMBER = "100";
    final String COMPANY = "Company1";
    final String COUNTRY = "Country1";
    final String COLOR = "color1";
    final String NAME = "name1";
    final int YEAR_OB = 1990;
    final String PHONE = "123456789";
    final LocalDate RENT_DATE = LocalDate.of(2025, 8, 1);
    final int RENT_DAYS = 3;

    private Model model;
    private Car car;
    private Driver driver;

    private IRentCompany company;

    @BeforeEach
    void setUp() throws IOException {
        model = new Model(MODEL_NAME, GAS_TANK, COMPANY, COUNTRY, PRICE_PER_DAY);
        car = new Car(REG_NUMBER, MODEL_NAME, COLOR);
        driver = new Driver(LICENSE, NAME, YEAR_OB, PHONE);
        company = new RentCompanyEmbadded();
        ((Persistable) company).save("company2.data");
    }


    @Test
    void testEntities_OK () {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(model, company.getModel(MODEL_NAME));
        assertEquals(car, company.getCar(REG_NUMBER));
        assertEquals(driver, company.getDriver(LICENSE));
    }

    @Test
    void testAdd_car_NO_MODEL() {
        assertEquals(NO_MODEL, company.addCar(car));
        assertNull(company.getCar(REG_NUMBER));
    }

    @Test
    void testAdd_Duplicate_entities() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(MODEL_EXISTS, company.addModel(model));
        assertEquals(CAR_EXISTS, company.addCar(car));
        assertEquals(DRIVER_EXISTS, company.addDriver(driver));
    }

    @Test
    void testGet_When_not_Added() {
        assertNull(company.getCar(REG_NUMBER));
        assertNull(company.getDriver(LICENSE));
        assertNull(company.getModel(MODEL_NAME));
    }

    @Test
    void testSave_Restored_OK() throws IOException, ClassNotFoundException {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        String file = "companyTest.data";
        ((Persistable) company).save(file);
        IRentCompany restored = RentCompanyEmbadded.restoreFromFile(file);
        assertNotNull(restored);
        assertEquals(model, company.getModel(MODEL_NAME));
        assertEquals(car, company.getCar(REG_NUMBER));
        assertEquals(driver, company.getDriver(LICENSE));
    }

    @Test
    void testSave_Restored_Fail() throws IOException, ClassNotFoundException {
        String file = "companyTest_fail.data";
        IRentCompany restored = RentCompanyEmbadded.restoreFromFile(file);
        assertNotNull(restored);
        assertTrue(restored instanceof RentCompanyEmbadded);
        assertNull(restored.getModel(MODEL_NAME));
        assertNull(restored.getCar(REG_NUMBER));
        assertNull(restored.getDriver(LICENSE));
    }

    //sprint 2

    @Test
    void testRentCarOK() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(OK, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentCarNO_CAR() {
        assertEquals(OK, company.addModel(model));
        //assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(NO_CAR, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentCarNO_DRIVER() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        //assertEquals(OK, company.addDriver(driver));

        assertEquals(NO_DRIVER, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentCarRemove_Use() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        car.setFlRemove(true);
        assertEquals(CAR_REMOVED, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
        car.setFlRemove(false);
        car.setInUse(true);
        assertEquals(CAR_IN_USE, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentRecords() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        RentRecord expected = new RentRecord(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        List<RentRecord> res = company.getRentRecordAtDate(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS));
        assertEquals(1, res.size());
        assertEquals(expected, res.getFirst());
        List<RentRecord> res1 = company.getRentRecordAtDate(RENT_DATE.plusDays(RENT_DAYS), RENT_DATE.plusDays(10));
        assertTrue(res1.isEmpty());
        //List<RentRecord> res1 = company.getRentRecordAtDate(RENT_DATE.plusDays(RENT_DAYS), RENT_DATE.plusDays(10));
        //assertTrue(res1.isEmpty());
    }

    @Test
    void testCarsByDriver() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        List<Car> res = company.getCarsByDriver(LICENSE);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        res = company.getCarsByDriver(LICENSE + 1);
        assertTrue(res.isEmpty());
    }


    @Test
    void testDriversByCar() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        List<Driver> res = company.getDriversByCar(REG_NUMBER);
        assertEquals(1, res.size());
        assertEquals(driver, res.get(0));

        res = company.getDriversByCar(REG_NUMBER + 1);
        assertTrue(res.isEmpty());
    }

    @Test
    void testCarsByModel() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        List<Car> res = company.getCarsByModel(MODEL_NAME);

        assertEquals(1, res.size());
        assertEquals(car, res.getFirst());

        Car car1 = new Car(REG_NUMBER + 1, MODEL_NAME, COLOR + "R");
        car1.setInUse(true);
        assertEquals(OK, company.addCar(car1));
        res = company.getCarsByModel(MODEL_NAME);
        assertEquals(1, res.size());
        assertEquals(car, res.getFirst());

        car1.setInUse(false);
        car1.setFlRemove(true);
        res = company.getCarsByModel(MODEL_NAME);
        assertEquals(1, res.size());

        res = company.getCarsByModel(MODEL_NAME + " RR");
        assertTrue(res.isEmpty());

    }

    @Test
    void testRemoveCarInUse() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        RemoveCarData nC = new RemoveCarData(car, null);
        //System.out.println(nC);
        //System.out.println(company.removeCar(REG_NUMBER));
        assertEquals(nC, company.removeCar(REG_NUMBER));
        assertNull(company.removeCar(REG_NUMBER + 100));
        car.setFlRemove(true);
        assertNull(company.removeCar(REG_NUMBER));
    }

        @Test
        void testRemoveCarsNotInUse () {
            assertEquals(OK, company.addModel(model));
            assertEquals(OK, company.addCar(car));
            assertEquals(OK, company.addDriver(driver));
            RemoveCarData nC = new RemoveCarData(car, new ArrayList<>());
            assertEquals(nC, company.removeCar(REG_NUMBER));

    }



    //===================== HW Sprint 2=================
    @Test
    void testReturnCar_NoDamages_NoDelay() {
        company.addModel(model);
        company.addCar(car);
        company.addDriver(driver);

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        LocalDate returnDate = RENT_DATE.plusDays(RENT_DAYS);

        RemoveCarData result = company.returnCar(REG_NUMBER, LICENSE, returnDate, 0, 100);

        assertNotNull(result);
        assertEquals(car, result.getCar());
        assertNull(result.getRemovedRecords());
        assertEquals(State.EXCELLENT, car.getState());
        assertFalse(car.isInUse());
    }

    @Test
    void testReturnCar_WithDelay() {
        company.addModel(model);
        company.addCar(car);
        company.addDriver(driver);

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        LocalDate returnDate = RENT_DATE.plusDays(RENT_DAYS + 2);

        RemoveCarData result = company.returnCar(REG_NUMBER, LICENSE, returnDate, 0, 100);

        assertNotNull(result);
        assertEquals(car, result.getCar());
        RentRecord record = company.getRentRecordAtDate(RENT_DATE, returnDate.plusDays(1)).getFirst();
        assertTrue(record.getCost() > PRICE_PER_DAY * RENT_DAYS);
    }

    @Test
    void testReturnCar_WithDamages() {
        company.addModel(model);
        company.addCar(car);
        company.addDriver(driver);

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        LocalDate returnDate = RENT_DATE.plusDays(RENT_DAYS);

        RemoveCarData result = company.returnCar(REG_NUMBER, LICENSE, returnDate, 50, 50);

        assertNotNull(result);
        assertEquals(car, result.getCar());
        assertEquals(State.BAD, car.getState());
    }

    @Test
    void testReturnCar_RemoveCar() {
        company.addModel(model);
        company.addCar(car);
        company.addDriver(driver);

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        LocalDate returnDate = RENT_DATE.plusDays(RENT_DAYS);

        RemoveCarData result = company.returnCar(REG_NUMBER, LICENSE, returnDate, 120, 0);

        assertNotNull(result);
        assertEquals(car, result.getCar());
        assertNotNull(result.getRemovedRecords());
        assertNull(company.getCar(REG_NUMBER));
    }

    @Test
    void testNegative_ReturnCar_NotRented() {
        company.addModel(model);
        company.addCar(car);
        company.addDriver(driver);

        RemoveCarData result = company.returnCar(REG_NUMBER, LICENSE, RENT_DATE.plusDays(1), 0, 100);
        assertNotNull(result);
        assertNull(result.getCar());
        assertNull(result.getRemovedRecords());
    }

    @Test
    void testNegative_ReturnCar_WrongDriver() {
        company.addModel(model);
        company.addCar(car);
        company.addDriver(driver);

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        RemoveCarData result = company.returnCar(REG_NUMBER, LICENSE + 1, RENT_DATE.plusDays(RENT_DAYS), 0, 100);
        assertNotNull(result);
        assertNull(result.getCar());
    }

    @Test
    void testNegative_RemoveCar_NotExists() {
        assertNull(company.removeCar("Trololo_Car"));
    }

    @Test
    void testNegative_RemoveCar_AlreadyRemoved() {
        company.addModel(model);
        company.addCar(car);
        company.addDriver(driver);

        car.setFlRemove(true);
        assertNull(company.removeCar(REG_NUMBER));
    }






/**
    @Test
    void setGasPrice() {
    }

    @Test
    void getFinePercent() {
    }

    @Test
    void addModel() {
    }

    @Test
    void getModel() {
    }

    @Test
    void addCar() {
    }

    @Test
    void getCar() {
    }

    @Test
    void addDriver() {
    }

    @Test
    void getDriver() {
    }

    @Test
    void save() {
    }

    @Test
    void restoreFromFile() {
    }
    **/
}
