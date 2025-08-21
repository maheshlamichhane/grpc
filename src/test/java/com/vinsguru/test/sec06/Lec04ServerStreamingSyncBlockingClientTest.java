package com.vinsguru.test.sec06;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec06.BalanceCheckRequest;
import com.vinsguru.models.sec06.BankServiceGrpc;
import com.vinsguru.models.sec06.Money;
import com.vinsguru.models.sec06.WithdrawRequest;
import com.vinsguru.sec06.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec04ServerStreamingSyncBlockingClientTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec04ServerStreamingSyncBlockingClientTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        grpcServer.start();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void blockingClientWithdrawTest() {
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(2)
                .setAmount(20)
                .build();
        Iterator<Money> iterator = this.blockingStub.withdraw(request);
        int count = 0;
        while (iterator.hasNext()){
            logger.info("received money: {}",iterator.next());
            count++;
        }
        logger.info("endsx ############");
        Assertions.assertEquals(2,count);
    }


    @AfterAll
    public void stop(){
        grpcServer.stop();
    }
}
