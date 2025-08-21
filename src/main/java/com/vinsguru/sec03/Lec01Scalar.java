package com.vinsguru.sec03;

import com.vinsguru.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01Scalar {

    private static final Logger logger = LoggerFactory.getLogger(Lec01Scalar.class);

    public static void main(String[] args) {
        var person = Person.newBuilder()
                .setLastName("sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setEmployed(true)
                .setSalary(1000.2345)
                .setBankAccountNumber(3443343434334L)
                .setBalance(-10000)
                .build();

        logger.info("{}",person);

    }
}
