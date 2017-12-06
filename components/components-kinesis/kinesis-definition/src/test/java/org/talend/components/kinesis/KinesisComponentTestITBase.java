package org.talend.components.kinesis;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.test.AbstractComponentTest;
import org.talend.components.api.test.AbstractComponentTest2;
import org.talend.components.kinesis.input.KinesisInputDefinition;
import org.talend.components.kinesis.output.KinesisOutputDefinition;
import org.talend.daikon.definition.Definition;
import org.talend.daikon.definition.service.DefinitionRegistryService;

public abstract class KinesisComponentTestITBase extends AbstractComponentTest2 {

    @Inject
    DefinitionRegistryService defReg;

    @Override
    public DefinitionRegistryService getDefinitionRegistry() {
        return defReg;
    }

    @Test
    public void assertComponentsAreRegistered() {
        assertThat(getDefinitionRegistry().getDefinitionsMapByType(Definition.class).get(KinesisInputDefinition.NAME),
                notNullValue());
        // assertThat(getDefinitionRegistry().getDefinitionsMapByType(Definition.class).get(KinesisOutputDefinition.NAME),
        // notNullValue());
    }
}
