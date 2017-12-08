package org.talend.components.kinesis.runtime;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.components.kinesis.KinesisRegion;

import com.amazonaws.services.kinesis.AmazonKinesis;

public class KinesisDatasetRuntimeTestIT {

    final static Set<String> streamsName = new HashSet(Arrays.asList("streams1", "streams2", "streams3"));

    final static AmazonKinesis amazonKinesis = KinesisClient.create(KinesisTestConstants.getLocalDatastore());

    @BeforeClass
    public static void initStreams() {
        for (String streamName : streamsName) {
            amazonKinesis.createStream(streamName, 1);
        }
    }

    @AfterClass
    public static void cleanStreams() {
        for (String streamName : streamsName) {
            amazonKinesis.deleteStream(streamName);
        }
    }

    KinesisDatasetRuntime runtime;

    @Before
    public void init() {
        runtime = new KinesisDatasetRuntime();
    }

    // Can't use localstack to list streams by region
    @Test
    public void listStreams() {
        runtime.initialize(null, KinesisTestConstants.getDatasetForListStreams(KinesisTestConstants.getLocalDatastore(),
                KinesisRegion.DEFAULT, null));
        Set<String> streams = runtime.listStreams();
        assertEquals(streams, streamsName);
    }

}
