package com.vinsguru.test.sec06;


import com.google.protobuf.Empty;
import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec06.BalanceCheckRequest;
import com.vinsguru.models.sec06.BankServiceGrpc;
import com.vinsguru.sec06.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec01UnarySyncBlockingClientTest extends AbstractChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(Lec01UnarySyncBlockingClientTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void getBalanceTest(){
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var balance = this.blockingStub.getAccountBalance(request);
        logger.info("unary balance received: {}",balance);
        Assertions.assertEquals(100,balance.getBalance());
    }

    @Test
    public void getAllBalanceTest(){
       var allAccounts = this.blockingStub.getAllAccounts(Empty.getDefaultInstance());
       logger.info("All accounts:{}",allAccounts);
       Assertions.assertEquals(10,allAccounts.getAccountsCount());
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
