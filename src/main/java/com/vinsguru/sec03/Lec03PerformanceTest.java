package com.vinsguru.sec03;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.vinsguru.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03PerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec03PerformanceTest.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
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
        var jsonPerson = new JsonPerson("sam",12,"sam@gmail.com",true,
                1000.2345,3443343434334L,-10000);

            json(jsonPerson);
            proto(person);
//        for(int i=0; i<5;i++){
//            runTest("json",() -> json(jsonPerson));
//            runTest("proto",() -> proto(person));
//        }
    }

    private static  void proto(Person person){
        try{
            var bytes = person.toByteArray();
            Person.parseFrom(bytes);
        }
        catch (InvalidProtocolBufferException e){
            throw new RuntimeException(e);
        }

    }

    private static void json(JsonPerson person) {
        try{
            var bytes = objectMapper.writeValueAsBytes(person);
            objectMapper.readValue(bytes,JsonPerson.class);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void runTest(String testName,Runnable runnable){
        var start = System.currentTimeMillis();
        for(int i=0; i<1_000_000;i++){
            runnable.run();
        }
        var end = System.currentTimeMillis();
        logger.info("time taken for {} - {} ms",testName,(end-start));
    }
}
