package com.vinsguru.test.sec10;

import com.google.common.util.concurrent.Uninterruptibles;
import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec10.BalanceCheckRequest;
import com.vinsguru.models.sec10.BankServiceGrpc;
import com.vinsguru.models.sec10.WithdrawRequest;
import com.vinsguru.sec10.DeadlineBankService;
import com.vinsguru.test.common.AbstractChannelTest;
import io.grpc.Deadline;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec05LazyChannelDemoTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec05LazyChannelDemoTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;


    @BeforeAll
    public void setup(){
//        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void blockingDeadlineTest(){
       var request = BalanceCheckRequest.newBuilder()
               .setAccountNumber(1)
               .build();
       Uninterruptibles.sleepUninterruptibly(3,TimeUnit.SECONDS);
       var response = this.blockingStub.getAccountBalance(request);
       logger.info("{}",response);

    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
