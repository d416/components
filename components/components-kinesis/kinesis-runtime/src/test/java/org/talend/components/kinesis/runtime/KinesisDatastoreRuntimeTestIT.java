package org.talend.components.kinesis.runtime;

import org.apache.beam.sdk.repackaged.org.apache.commons.lang3.StringUtils;
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
