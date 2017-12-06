package org.apache.beam.sdk.io.kinesis.auth;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.apache.beam.sdk.repackaged.org.apache.commons.lang3.StringUtils;

public class BasicAWSCredentialsProvider implements AWSCredentialsProvider {
    private final String accessKey;
    private final String secretKey;

    public BasicAWSCredentialsProvider(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public AWSCredentials getCredentials() {
        if (!StringUtils.isEmpty(this.accessKey) && !StringUtils.isEmpty(this.secretKey)) {
            return new BasicAWSCredentials(this.accessKey, this.secretKey);
        } else {
            throw new AmazonClientException("Access key or secret key is null");
        }
    }

    public void refresh() {
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
