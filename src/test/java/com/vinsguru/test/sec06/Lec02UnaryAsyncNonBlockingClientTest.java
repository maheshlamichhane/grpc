package com.vinsguru.test.sec06;

import com.google.protobuf.Empty;
import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec06.AccountBalance;
import com.vinsguru.models.sec06.AllAccountRsesponse;
import com.vinsguru.models.sec06.BalanceCheckRequest;
import com.vinsguru.models.sec06.BankServiceGrpc;
import com.vinsguru.sec06.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import com.vinsguru.test.common.ResponseObserver;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec02UnaryAsyncNonBlockingClientTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec02UnaryAsyncNonBlockingClientTest.class);

    private  final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected  BankServiceGrpc.BankServiceStub nonBlockingStub;

    @BeforeAll
    public  void setup(){
        grpcServer.start();
        nonBlockingStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void getBalanceTest() throws InterruptedException {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.nonBlockingStub.getAccountBalance(request, observer);
        observer.await();
        Assertions.assertEquals(1,observer.getItems().size());
        Assertions.assertEquals(100,observer.getItems().getFirst().getBalance());
        Assertions.assertNull(observer.getThrowable());

    }

    @Test
    public void getAllBalanceTest() throws InterruptedException {
        var observer = ResponseObserver.<AllAccountRsesponse> create();
        this.nonBlockingStub.getAllAccounts(Empty.getDefaultInstance(),observer);
        observer.await();
        Assertions.assertEquals(1,observer.getItems().size());
        Assertions.assertEquals(10,observer.getItems().getFirst().getAccountsCount());
        Assertions.assertNull(observer.getThrowable());
    }

    @AfterAll
    public  void stop(){
        grpcServer.stop();
    }
}
