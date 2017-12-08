package org.talend.components.kinesis.integration;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.talend.components.kinesis.KinesisDatasetDefinition;
import org.talend.components.kinesis.KinesisDatasetProperties;
import org.talend.components.kinesis.KinesisRegion;
import org.talend.components.kinesis.runtime.IKinesisDatasetRuntime;
import org.talend.daikon.runtime.RuntimeInfo;
import org.talend.daikon.runtime.RuntimeUtil;
import org.talend.daikon.sandbox.SandboxedInstance;

public class KinesisDatasetRuntimeTestIT {

    private final KinesisDatasetDefinition def = new KinesisDatasetDefinition();

    // Can't use localstack to list streams by region
    @Test
    public void listStreams() {
        KinesisDatasetProperties props = KinesisTestConstants
                .getDatasetForListStreams(KinesisTestConstants.getDatastore(), KinesisRegion.DEFAULT, null);
        RuntimeInfo ri = def.getRuntimeInfo(props);
        try (SandboxedInstance si = RuntimeUtil.createRuntimeClass(ri, getClass().getClassLoader())) {
            IKinesisDatasetRuntime runtime = (IKinesisDatasetRuntime) si.getInstance();
            runtime.initialize(null, props);
            Set<String> streams = runtime.listStreams();
            assertEquals(streams, new HashSet<String>());
        }
    }

}
