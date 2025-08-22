package com.vinsguru.test.sec11.interceptors;

import io.grpc.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DeadlineInterceptor implements ClientInterceptor {

    private final Duration duration;

    public DeadlineInterceptor(Duration duration) {
        this.duration = duration;
    }


    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {

        callOptions = callOptions.withDeadline(Deadline.after(duration.toMillis(),TimeUnit.SECONDS));
        return channel.newCall(methodDescriptor,callOptions);
    }
}
