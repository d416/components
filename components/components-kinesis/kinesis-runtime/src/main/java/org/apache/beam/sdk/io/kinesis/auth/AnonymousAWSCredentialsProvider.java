package org.apache.beam.sdk.io.kinesis.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;

public class AnonymousAWSCredentialsProvider implements AWSCredentialsProvider {

    public AnonymousAWSCredentialsProvider() {
    }

    public AWSCredentials getCredentials() {
        return new AnonymousAWSCredentials();
    }

    public void refresh() {
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
