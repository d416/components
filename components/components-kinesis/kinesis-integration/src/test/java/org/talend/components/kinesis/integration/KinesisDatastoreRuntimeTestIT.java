package org.talend.components.kinesis.integration;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.talend.components.common.datastore.runtime.DatastoreRuntime;
import org.talend.components.kinesis.KinesisDatastoreDefinition;
import org.talend.components.kinesis.KinesisDatastoreProperties;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.runtime.RuntimeInfo;
import org.talend.daikon.runtime.RuntimeUtil;
import org.talend.daikon.sandbox.SandboxedInstance;

public class KinesisDatastoreRuntimeTestIT {

    private final KinesisDatastoreDefinition def = new KinesisDatastoreDefinition();

    public KinesisDatastoreProperties getDatastore() {
        KinesisDatastoreProperties datastore = new KinesisDatastoreProperties("kinesisDatastore");
        String awsAccessKey = System.getProperty("aws.accesskey");
        String awsSecretKey = System.getProperty("aws.secretkey");
        if(StringUtils.isEmpty(awsAccessKey) || StringUtils.isEmpty(awsSecretKey)) {
            datastore.specifyCredentials.setValue(false);
        }else {
            datastore.accessKey.setValue(awsAccessKey);
            datastore.secretKey.setValue(awsSecretKey);
        }
        return datastore;
    }

    @Test
    public void doHealthChecks() {

        KinesisDatastoreProperties props = getDatastore();
        RuntimeInfo ri = def.getRuntimeInfo(props);
        try(SandboxedInstance si = RuntimeUtil.createRuntimeClass(ri, getClass().getClassLoader())) {
            DatastoreRuntime runtime = (DatastoreRuntime)si.getInstance();
            runtime.initialize(null, props);
            Iterable<ValidationResult> validationResults = runtime.doHealthChecks(null);
            Assert.assertEquals(ValidationResult.OK, validationResults.iterator().next());

            // Wrong access key
            {
                KinesisDatastoreProperties wrongAccess = getDatastore();
                wrongAccess.accessKey.setValue("wrong");
                runtime.initialize(null, wrongAccess);
                validationResults = runtime.doHealthChecks(null);
                Assert.assertEquals(ValidationResult.Result.ERROR, validationResults.iterator().next().getStatus());
            }

            // Wrong screct key
            {
                KinesisDatastoreProperties wrongSecret = getDatastore();
                wrongSecret.secretKey.setValue("wrong");
                runtime.initialize(null, wrongSecret);
                validationResults = runtime.doHealthChecks(null);
                Assert.assertEquals(ValidationResult.Result.ERROR, validationResults.iterator().next().getStatus());
            }
        }
    }

}
