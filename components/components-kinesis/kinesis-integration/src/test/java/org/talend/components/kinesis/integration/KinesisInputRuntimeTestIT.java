package org.talend.components.kinesis.integration;

import org.junit.Assert;
import org.junit.Test;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.components.kinesis.input.KinesisInputDefinition;
import org.talend.components.kinesis.input.KinesisInputProperties;
import org.talend.daikon.runtime.RuntimeInfo;
import org.talend.daikon.runtime.RuntimeUtil;
import org.talend.daikon.sandbox.SandboxedInstance;

public class KinesisInputRuntimeTestIT {

    private final KinesisInputDefinition def = new KinesisInputDefinition();

    @Test
    public void testRuntime() {
        KinesisInputProperties props = new KinesisInputProperties("KinesisInput");
        RuntimeInfo ri = def.getRuntimeInfo(ExecutionEngine.BEAM, props, ConnectorTopology.OUTGOING);
        try (SandboxedInstance si = RuntimeUtil.createRuntimeClass(ri, getClass().getClassLoader())) {
            Assert.assertEquals("KinesisInputRuntime", si.getInstance().getClass().getSimpleName());
        }
    }
}
