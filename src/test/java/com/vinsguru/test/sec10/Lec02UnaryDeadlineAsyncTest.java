package com.vinsguru.test.sec10;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec10.AccountBalance;
import com.vinsguru.models.sec10.BalanceCheckRequest;
import com.vinsguru.models.sec10.BankServiceGrpc;
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

import java.util.concurrent.TimeUnit;

public class Lec02UnaryDeadlineAsyncTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec02UnaryDeadlineAsyncTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newStub(channel);
    }


    @Test
    public void blockingDeadlineTest(){
        var observer = ResponseObserver.<AccountBalance>create();
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        blockingStub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .getAccountBalance(request,observer);
        observer.await();
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());

    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}
