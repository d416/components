package org.talend.components.kinesis.runtime;

import org.apache.beam.sdk.io.kinesis.TalendKinesisProvider;
import org.talend.components.kinesis.KinesisDatasetProperties;
import org.talend.components.kinesis.KinesisDatastoreProperties;
import org.talend.components.kinesis.KinesisRegion;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;

public class KinesisClient {

    public static AmazonKinesis create(KinesisDatastoreProperties datastore) {
        TalendKinesisProvider clientProvider = new TalendKinesisProvider(datastore.specifyCredentials.getValue(),
                datastore.accessKey.getValue(), datastore.secretKey.getValue(), Regions.DEFAULT_REGION,
                datastore.specifySTS.getValue(), datastore.roleArn.getValue(), datastore.roleSessionName.getValue(),
                datastore.roleExternalId.getValue(), datastore.specifyEndpoint.getValue(),
                datastore.endpoint.getValue());
        return clientProvider.get();
    }

    public static AmazonKinesis create(KinesisDatasetProperties dataset) {
        KinesisDatastoreProperties datastore = dataset.getDatastoreProperties();
        String region = dataset.region.getValue().getValue();
        if (KinesisRegion.OTHER.getValue().equals(region)) {
            region = dataset.unknownRegion.getValue();
        }
        TalendKinesisProvider clientProvider = new TalendKinesisProvider(datastore.specifyCredentials.getValue(),
                datastore.accessKey.getValue(), datastore.secretKey.getValue(), Regions.fromName(region),
                datastore.specifySTS.getValue(), datastore.roleArn.getValue(), datastore.roleSessionName.getValue(),
                datastore.roleExternalId.getValue(), datastore.specifyEndpoint.getValue(),
                datastore.endpoint.getValue());
        return clientProvider.get();
    }
}
