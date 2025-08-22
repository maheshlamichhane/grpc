package com.vinsguru.test.sec09;


import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec08.BalanceCheckRequest;
import com.vinsguru.models.sec08.BankServiceGrpc;
import com.vinsguru.models.sec09.ErrorMessage;
import com.vinsguru.models.sec09.ValidationCode;
import com.vinsguru.sec08.BankService;
import com.vinsguru.test.common.AbstractChannelTest;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec01UnaryInputValidationTest extends AbstractChannelTest {

    private static final Metadata.Key<ErrorMessage> ERROR_MESSAGE_KEY = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());

    private static final Logger logger = LoggerFactory.getLogger(Lec01UnaryInputValidationTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void syncBlockingInputValidationTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class,() -> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(12)
                    .build();
            this.blockingStub.getAccountBalance(request);
        });

       Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT,getValidationCode(ex));

    }

    protected ValidationCode getValidationCode(Throwable throwable){
        return Optional.ofNullable(Status.trailersFromThrowable(throwable))
                .map(m -> m.get(ERROR_MESSAGE_KEY))
                .map(ErrorMessage::getValidationCode)
                .orElseThrow();

    }



    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
