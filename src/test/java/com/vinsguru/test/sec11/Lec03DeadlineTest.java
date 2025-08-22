package com.vinsguru.test.sec11;

import com.vinsguru.models.sec11.AccountBalance;
import com.vinsguru.models.sec11.BalanceCheckRequest;
import com.vinsguru.test.common.ResponseObserver;
import com.vinsguru.test.sec11.interceptors.DeadlineInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec03DeadlineTest extends AbstractInterceptorTest{



    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new DeadlineInterceptor(Duration.ofSeconds(2)));
    }

    @Test
    public void blockingDeadlineTest1(){
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var response = bankBlockingStub
                .getAccountBalance(request);


    }

    @Test
    public void nonBlockingDeadlineTest2(){
        var observer = ResponseObserver.<AccountBalance>create();
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        bankStub
                .getAccountBalance(request,observer);
        observer.await();
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());

    }


}
