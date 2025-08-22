package com.vinsguru.test.sec11;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec10.BankServiceGrpc;
import com.vinsguru.models.sec10.Money;
import com.vinsguru.models.sec10.WithdrawRequest;
import com.vinsguru.sec10.DeadlineBankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class Lec02ExecutorCallOptionsTest extends AbstractChannelTest {

    private static final Logger log  = LoggerFactory.getLogger(Lec02ExecutorCallOptionsTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceStub blockingStub;


    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newStub(channel);
    }


    @Test
    public void executorDemo(){
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(30)
                .build();

        this.blockingStub
                .withExecutor(Executors.newVirtualThreadPerTaskExecutor())
                .withdraw(request,observer);
        observer.await();
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}
