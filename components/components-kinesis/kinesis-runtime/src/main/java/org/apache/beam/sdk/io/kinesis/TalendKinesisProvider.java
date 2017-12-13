// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.apache.beam.sdk.io.kinesis;

import org.apache.beam.sdk.io.kinesis.auth.AnonymousAWSCredentialsProvider;
import org.apache.beam.sdk.io.kinesis.auth.BasicAWSCredentialsProvider;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.internal.securitytoken.RoleInfo;
import com.amazonaws.auth.profile.internal.securitytoken.STSProfileCredentialsServiceProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;

public class TalendKinesisProvider implements KinesisClientProvider {

    private final boolean specifyCredentials;

    private final String accessKey;

    private final String secretKey;

    private final Regions region;

    private final boolean specifySTS;

    private final String roleArn;

    private final String roleSessionName;

    private final String externalId;

    private final boolean specifyEndpoint;

    private final String endpoint;

    // TODO add builder
    public TalendKinesisProvider(boolean specifyCredentials, String accessKey, String secretKey, Regions region,
            boolean specifySTS, String roleArn, String roleSessionName, String externalId, boolean specifyEndpoint,
            String endpoint) {
        this.specifyCredentials = specifyCredentials;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.specifySTS = specifySTS;
        this.roleArn = roleArn;
        this.roleSessionName = roleSessionName;
        this.externalId = externalId;
        this.specifyEndpoint = specifyEndpoint;
        this.endpoint = endpoint;
    }

    @Override
    public AmazonKinesis get() {
        AWSCredentialsProviderChain credentials = null;
        if (specifyCredentials) {
            credentials = new AWSCredentialsProviderChain(new BasicAWSCredentialsProvider(accessKey, secretKey),
                    new DefaultAWSCredentialsProviderChain(), new AnonymousAWSCredentialsProvider());
        } else {
            // do not be polluted by hidden accessKey/secretKey
            credentials = new AWSCredentialsProviderChain(new DefaultAWSCredentialsProviderChain(),
                    new AnonymousAWSCredentialsProvider());
        }
        if (specifySTS) {
            RoleInfo stsRoleInfo = new RoleInfo();
            stsRoleInfo.withRoleArn(roleArn);
            stsRoleInfo.withRoleSessionName(roleSessionName);
            stsRoleInfo.withExternalId(externalId);
            stsRoleInfo.withLongLivedCredentialsProvider(credentials);
            // need to consider DefaultAWSCredentialsProviderChain and AnonymousAWSCredentialsProvider after?
            credentials = new AWSCredentialsProviderChain(new STSProfileCredentialsServiceProvider(stsRoleInfo));
        }
        AmazonKinesisClient client = new AmazonKinesisClient(credentials);
        client.withRegion(region);
        if (specifyEndpoint) {
            client.setEndpoint(endpoint);
        }
        return client;
    }
}
