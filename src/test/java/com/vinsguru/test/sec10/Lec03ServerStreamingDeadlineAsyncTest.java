package com.vinsguru.test.sec10;

import com.google.common.util.concurrent.Uninterruptibles;
import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec10.AccountBalance;
import com.vinsguru.models.sec10.BalanceCheckRequest;
import com.vinsguru.models.sec10.BankServiceGrpc;
import com.vinsguru.models.sec10.WithdrawRequest;
import com.vinsguru.sec10.DeadlineBankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Lec03ServerStreamingDeadlineAsyncTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec03ServerStreamingDeadlineAsyncTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }


    @Test
    public void blockingDeadlineTest(){
        try{
            var request = WithdrawRequest.newBuilder()
                    .setAccountNumber(1)
                    .setAmount(50)
                    .build();
            var iterator = this.blockingStub
                    .withDeadline(Deadline.after(2,TimeUnit.SECONDS))
                    .withdraw(request);

            while(iterator.hasNext()){
                logger.info("{}",iterator.next());
            }
        }
        catch (Exception e){
            logger.info("error");
        }

        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);

    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}
