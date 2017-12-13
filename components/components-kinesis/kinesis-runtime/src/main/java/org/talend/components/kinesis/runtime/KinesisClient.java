package org.talend.components.kinesis.runtime;

import org.apache.beam.sdk.io.kinesis.TalendKinesisProvider;
import org.talend.components.kinesis.KinesisDatasetProperties;
import org.talend.components.kinesis.KinesisDatastoreProperties;
import org.talend.components.kinesis.KinesisRegion;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;

public class KinesisClient {

    public static TalendKinesisProvider getProvider(KinesisDatastoreProperties datastore) {
        return new TalendKinesisProvider(datastore.specifyCredentials.getValue(), datastore.accessKey.getValue(),
                datastore.secretKey.getValue(), Regions.DEFAULT_REGION, datastore.specifySTS.getValue(),
                datastore.roleArn.getValue(), datastore.roleSessionName.getValue(), datastore.roleExternalId.getValue(),
                datastore.specifyEndpoint.getValue(), datastore.endpoint.getValue());
    }

    public static AmazonKinesis create(KinesisDatastoreProperties datastore) {
        return getProvider(datastore).get();
    }

    public static TalendKinesisProvider getProvider(KinesisDatasetProperties dataset) {
        KinesisDatastoreProperties datastore = dataset.getDatastoreProperties();
        String region = dataset.region.getValue().getValue();
        if (KinesisRegion.OTHER.getValue().equals(region)) {
            region = dataset.unknownRegion.getValue();
        }
        return new TalendKinesisProvider(datastore.specifyCredentials.getValue(), datastore.accessKey.getValue(),
                datastore.secretKey.getValue(), Regions.fromName(region), datastore.specifySTS.getValue(),
                datastore.roleArn.getValue(), datastore.roleSessionName.getValue(), datastore.roleExternalId.getValue(),
                datastore.specifyEndpoint.getValue(), datastore.endpoint.getValue());
    }

    public static AmazonKinesis create(KinesisDatasetProperties dataset) {
        return getProvider(dataset).get();
    }
}
