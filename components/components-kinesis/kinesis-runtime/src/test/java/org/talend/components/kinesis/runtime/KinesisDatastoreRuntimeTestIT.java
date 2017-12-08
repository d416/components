package org.talend.components.kinesis.runtime;

import static org.talend.components.kinesis.runtime.KinesisTestConstants.getDatastore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.kinesis.KinesisDatastoreProperties;
import org.talend.daikon.properties.ValidationResult;

public class KinesisDatastoreRuntimeTestIT {

    KinesisDatastoreRuntime runtime;

    @Before
    public void init() {
        runtime = new KinesisDatastoreRuntime();
    }

    @Test
    public void doHealthChecks() {

        runtime.initialize(null, getDatastore());
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
