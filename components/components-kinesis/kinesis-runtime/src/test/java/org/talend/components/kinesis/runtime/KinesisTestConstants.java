package org.talend.components.kinesis.runtime;

import org.apache.beam.sdk.repackaged.org.apache.commons.lang3.StringUtils;
import org.talend.components.kinesis.KinesisDatasetProperties;
import org.talend.components.kinesis.KinesisDatastoreProperties;
import org.talend.components.kinesis.KinesisRegion;

public class KinesisTestConstants {

    private static final String LOCALSTACK_KINESIS_ENDPOINT;

    static {
        String localstackKinesisEndpoint = System.getProperty("localstack.kinesis.endpoint");
        LOCALSTACK_KINESIS_ENDPOINT =
                localstackKinesisEndpoint != null ? localstackKinesisEndpoint : "http://localhost:4568";
    }

    // for localstack test
    public static KinesisDatastoreProperties getLocalDatastore() {
        KinesisDatastoreProperties datastore = new KinesisDatastoreProperties("kinesisDatastore");
        datastore.init();
        datastore.specifyCredentials.setValue(true);
        datastore.accessKey.setValue("test");
        datastore.secretKey.setValue("test");
        datastore.specifyEndpoint.setValue(true);
        datastore.endpoint.setValue(LOCALSTACK_KINESIS_ENDPOINT);
        return datastore;
    }

    public static KinesisDatastoreProperties getDatastore() {
        KinesisDatastoreProperties datastore = new KinesisDatastoreProperties("kinesisDatastore");
        datastore.init();
        String awsAccessKey = System.getProperty("aws.accesskey");
        String awsSecretKey = System.getProperty("aws.secretkey");
        if (StringUtils.isEmpty(awsAccessKey) || StringUtils.isEmpty(awsSecretKey)) {
            datastore.specifyCredentials.setValue(false);
        } else {
            datastore.accessKey.setValue(awsAccessKey);
            datastore.secretKey.setValue(awsSecretKey);
        }
        return datastore;
    }

    public static KinesisDatasetProperties getDatasetForListStreams(KinesisDatastoreProperties datastore,
            KinesisRegion region, String customRegion) {
        KinesisDatasetProperties dataset = new KinesisDatasetProperties("kinesisDataset");
        dataset.init();
        dataset.setDatastoreProperties(datastore);
        dataset.region.setValue(region);
        if (KinesisRegion.OTHER.equals(region)) {
            dataset.unknownRegion.setValue(customRegion);
        }
        return dataset;
    }

}
