package com.vinsguru.test.sec08;


import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec08.AccountBalance;
import com.vinsguru.models.sec08.BalanceCheckRequest;
import com.vinsguru.models.sec08.BankServiceGrpc;
import com.vinsguru.sec08.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec02UnaryAsyncInputValidationTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec02UnaryAsyncInputValidationTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void asyncInputValidationTest(){
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(12)
                .build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.blockingStub.getAccountBalance(request,observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT,((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
    }



    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
