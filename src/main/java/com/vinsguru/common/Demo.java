package com.vinsguru.common;

import com.vinsguru.sec06.BankService;

public class Demo {

    public static void main(String[] args) {
        GrpcServer.create(new BankService())
                .start()
                .await();
    }
}
