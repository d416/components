package org.talend.components.kinesis.integration;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.util.ServiceLoader;

import org.junit.Test;
import org.talend.components.api.ComponentInstaller;
import org.talend.components.kinesis.KinesisComponentFamilyDefinition;

public class KinesisComponentFamilyDefinitionTest {

    @Test
    public void testServiceLoader() throws Exception {
        ServiceLoader<ComponentInstaller> spiLoader = ServiceLoader.load(ComponentInstaller.class);
        assertThat(spiLoader, hasItem(isA(KinesisComponentFamilyDefinition.class)));
    }

}
