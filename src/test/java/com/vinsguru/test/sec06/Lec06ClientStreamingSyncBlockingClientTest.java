package com.vinsguru.test.sec06;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec06.*;
import com.vinsguru.sec06.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec06ClientStreamingSyncBlockingClientTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec06ClientStreamingSyncBlockingClientTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceStub blockingStub;

    @BeforeAll
    public void setup(){
        grpcServer.start();
        blockingStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void depositTest() {
        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver = this.blockingStub.deposit(responseObserver);

        requestObserver.onNext(DepositRequest.newBuilder().setAccountNumber(5).build());
        IntStream.rangeClosed(1,10)
                .mapToObj(i -> Money.newBuilder().setAmount(10).build())
                .map(m -> DepositRequest.newBuilder().setMoney(m).build())
                .forEach(requestObserver::onNext);

        requestObserver.onCompleted();
       // requestObserver.onError(new RuntimeException());

        responseObserver.await();

        Assertions.assertEquals(0,ResponseObserver.create().getItems().size());
        Assertions.assertEquals(200,responseObserver.getItems().getFirst().getBalance());
        Assertions.assertNull(responseObserver.getThrowable());

    }


    @AfterAll
    public void stop(){
        grpcServer.stop();
    }
}
