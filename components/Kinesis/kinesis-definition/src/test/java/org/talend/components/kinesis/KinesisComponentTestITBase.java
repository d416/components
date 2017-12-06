package org.talend.components.kinesis;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.test.AbstractComponentTest;
import org.talend.components.kinesis.input.KinesisInputDefinition;
import org.talend.components.kinesis.output.KinesisOutputDefinition;

public abstract class KinesisComponentTestITBase extends AbstractComponentTest {

    @Inject
    ComponentService componentService;

    @Override
    public ComponentService getComponentService() {
        return componentService;
    }

    @Test
    public void assertComponentsAreRegistered() {
        assertThat(getComponentService().getComponentDefinition(KinesisInputDefinition.NAME), notNullValue());
        assertThat(getComponentService().getComponentDefinition(KinesisOutputDefinition.NAME), notNullValue());
    }
}
