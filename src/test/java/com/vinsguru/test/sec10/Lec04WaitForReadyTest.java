package com.vinsguru.test.sec10;

import com.google.common.util.concurrent.Uninterruptibles;
import com.vinsguru.common.GrpcServer;
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

public class Lec04WaitForReadyTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec04WaitForReadyTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;


    @BeforeAll
    public void setup(){
//        this.grpcServer.start();
        Runnable runnable = () -> {
            Uninterruptibles.sleepUninterruptibly(5,TimeUnit.SECONDS);
            this.grpcServer.start();
        };
        Thread.ofVirtual().start(runnable);
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void blockingDeadlineTest(){
        logger.info("sending the request");
        try{
            var request = WithdrawRequest.newBuilder()
                    .setAccountNumber(1)
                    .setAmount(50)
                    .build();
            var iterator = this.blockingStub.withWaitForReady()
                    .withDeadline(Deadline.after(9,TimeUnit.SECONDS))
                    .withdraw(request);

            while(iterator.hasNext()){
                logger.info("{}",iterator.next());
            }
        }
        catch (Exception e){
            logger.info("error");
        }

    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
