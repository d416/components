package org.talend.components.kinesis.runtime;

import java.util.Set;

import org.talend.components.common.dataset.runtime.DatasetRuntime;
import org.talend.components.kinesis.KinesisDatasetProperties;

public interface IKinesisDatasetRuntime extends DatasetRuntime<KinesisDatasetProperties> {

    public Set<String> listStreams();

}
