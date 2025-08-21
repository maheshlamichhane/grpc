package com.vinsguru.test.sec06;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec06.*;
import com.vinsguru.sec06.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec07BiDirectionalStreamingClientTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec07BiDirectionalStreamingClientTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceStub nonBlockingStub;

    @BeforeAll
    public void setup(){
        grpcServer.start();
        nonBlockingStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void transferTest() {
        var responseObserver = ResponseObserver.<TransferResponse>create();
        var requestObserver = this.nonBlockingStub.transfer(responseObserver);
        var requests = List.of(
                TransferRequest.newBuilder().setAmount(10).setFromAccount(6).setToAccount(6).build(),
                TransferRequest.newBuilder().setAmount(10).setFromAccount(7).setToAccount(7).build(),
                TransferRequest.newBuilder().setAmount(10).setFromAccount(6).setToAccount(7).build(),
                TransferRequest.newBuilder().setAmount(10).setFromAccount(7).setToAccount(6).build()
        );

        requests.forEach(requestObserver::onNext);
        requestObserver.onCompleted();
        responseObserver.await();


        Assertions.assertEquals(4,responseObserver.getItems().size());
        this.validate(responseObserver.getItems().get(0),TransferStatus.REJECTED,100,100);
        this.validate(responseObserver.getItems().get(1),TransferStatus.REJECTED,100,100);
        this.validate(responseObserver.getItems().get(2),TransferStatus.COMPLETED,90,110);
        this.validate(responseObserver.getItems().get(3),TransferStatus.COMPLETED,100,100);

    }

    private void validate(TransferResponse response,TransferStatus status,int fromAccountBalance,int toAccountBalance){
        Assertions.assertEquals(status,response.getStatus());
        Assertions.assertEquals(fromAccountBalance,response.getFromAccount().getBalance());
        Assertions.assertEquals(toAccountBalance,response.getToAccount().getBalance());
    }


    @AfterAll
    public void stop(){
        grpcServer.stop();
    }
}
