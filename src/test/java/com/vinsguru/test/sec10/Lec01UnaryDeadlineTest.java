package com.vinsguru.test.sec10;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec10.BalanceCheckRequest;
import com.vinsguru.models.sec10.BankServiceGrpc;
import com.vinsguru.sec10.DeadlineBankService;
import com.vinsguru.test.common.AbstractChannelTest;
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

public class Lec01UnaryDeadlineTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec01UnaryDeadlineTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }


    @Test
    public void blockingDeadlineTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class,() ->{
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();
            var response = blockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .getAccountBalance(request);
        });

        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,ex.getStatus());


    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }
}
