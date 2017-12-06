package org.talend.components.kinesis.integration;

import org.talend.components.api.ComponentInstaller;
import org.talend.components.kinesis.KinesisComponentFamilyDefinition;
import org.junit.Test;
import java.util.ServiceLoader;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

public class KinesisComponentFamilyDefinitionTest {

    @Test
    public void testServiceLoader() throws Exception {
        ServiceLoader<ComponentInstaller> spiLoader = ServiceLoader.load(ComponentInstaller.class);
        assertThat(spiLoader, hasItem(isA(KinesisComponentFamilyDefinition.class)));
    }

}
