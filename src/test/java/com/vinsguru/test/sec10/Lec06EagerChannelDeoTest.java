package com.vinsguru.test.sec10;

import com.vinsguru.test.common.AbstractChannelTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec06EagerChannelDeoTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec06EagerChannelDeoTest.class);

    @Test
    public void eagerChannelDemo(){
        log.info("{}",channel.getState(true));
    }
}
