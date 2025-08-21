package com.vinsguru.test.sec06;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec06.AccountBalance;
import com.vinsguru.models.sec06.BankServiceGrpc;
import com.vinsguru.models.sec06.Money;
import com.vinsguru.models.sec06.WithdrawRequest;
import com.vinsguru.sec06.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05ServerStreamingAsyncNonBlockingClientTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec05ServerStreamingAsyncNonBlockingClientTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceStub blockingStub;

    @BeforeAll
    public void setup(){
        grpcServer.start();
        blockingStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void asyncNonBlockingClientWithdrawTest() {
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(2)
                .setAmount(20)
                .build();
        var observer = ResponseObserver.<Money>create();
        this.blockingStub.withdraw(request,observer);
        logger.info("Hello"+observer.getItems().toString());
        observer.await();
        Assertions.assertEquals(2,observer.getItems().size());
        Assertions.assertEquals(10,observer.getItems().getFirst().getAmount());
        Assertions.assertNull(observer.getThrowable());
        logger.info("end");

    }


    @AfterAll
    public void stop(){
        grpcServer.stop();
    }
}
