package com.vinsguru.sec02;


import com.vinsguru.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoDemo {

    private static final Logger logger = LoggerFactory.getLogger(ProtoDemo.class);

    public static void main(String[] args) {
        // Create person1
        var person1 = createPerson();

        //create another instance with same values
        var person2 = createPerson();

        // compare
        logger.info("equals {}",person1.equals(person2));
        logger.info("== {}",person1 == person2);

        //mutuable? No

        //create another instance with diff values
        var person3 = person1.toBuilder().setName("mike").build();

        // compare
        logger.info("equals {}",person1.equals(person3));
        logger.info("== {}",person1 == person3);

        //null?
        var person4 = person1.toBuilder().clearName().build();
        logger.info("person4: {}",person4);

    }

    private static Person createPerson(){
        return Person.newBuilder()
                .setName("sam")
                .setAge(13)
                .build();
    }
}
