package com.vinsguru.sec03;

import com.vinsguru.models.sec03.BodyStyle;
import com.vinsguru.models.sec03.Car;
import com.vinsguru.models.sec03.Cars;
import com.vinsguru.models.sec03.Dealer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Lec06Map {

    private static final Logger log = LoggerFactory.getLogger(Lec06Map.class);

    public static void main(String[] args) {
        var car1 = Car.newBuilder()
                .setMake("honda")
                .setModel("civic")
                .setYear(2000)
                .setBodyStyle(BodyStyle.COUPE)
                .build();
        var car2 = Car.newBuilder()
                .setMake("honda")
                .setModel("accord")
                .setYear(2002)
                .setBodyStyle(BodyStyle.SEDAN)
                .build();

        Cars cars1 = Cars.newBuilder()
                .addCars(car1)
                .addCars(car1)
                .build();

        Cars cars2 = Cars.newBuilder()
                .addCars(car2)
                .addCars(car2)
                .build();

        var dealer = Dealer.newBuilder()
                .putInventory(car1.getYear(), cars1)
                .putInventory(car2.getYear(),cars2)
                .build();

        log.info("{}:",dealer);

    }
}
