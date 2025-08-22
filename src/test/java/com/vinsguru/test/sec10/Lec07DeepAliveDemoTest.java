package com.vinsguru.test.sec10;

import com.google.common.util.concurrent.Uninterruptibles;
import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec10.BalanceCheckRequest;
import com.vinsguru.models.sec10.BankServiceGrpc;
import com.vinsguru.sec10.DeadlineBankService;
import com.vinsguru.test.common.AbstractChannelTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec07DeepAliveDemoTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec07DeepAliveDemoTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;


    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void keepAliveTest(){
       var request = BalanceCheckRequest.newBuilder()
               .setAccountNumber(1)
               .build();
       var response = this.blockingStub.getAccountBalance(request);
       logger.info("{}",response);

       //just blocking the thread for 30 sec
        Uninterruptibles.sleepUninterruptibly(30,TimeUnit.SECONDS);

    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
