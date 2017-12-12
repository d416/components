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
package org.talend.components.marklogic.tmarklogicinput;

import java.util.EnumSet;
import java.util.Set;

import org.talend.components.api.component.AbstractComponentDefinition;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.marklogic.AbstractMarkLogicComponentDefinition;
import org.talend.components.marklogic.MarkLogicFamilyDefinition;
import org.talend.components.marklogic.RuntimeInfoProvider;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

/**
 * The MarkLogicInputDefinition acts as an entry point for all of services that
 * a component provides to integrate with the Runtime Platform (at design-time) and other
 * components (at run-time).
 */
public class MarkLogicInputDefinition extends AbstractMarkLogicComponentDefinition {

    public static final String COMPONENT_NAME = "tMarkLogicInput"; //$NON-NLS-1$

    public MarkLogicInputDefinition() {
        super(COMPONENT_NAME, ExecutionEngine.DI, ExecutionEngine.BEAM);
    }

    @Override
    public String[] getFamilies() {
        return new String[] { "Databases/MarkLogic", "Big Data/MarkLogic" }; //$NON-NLS-1$
    }

    /**
     * Defines a list of Return Properties (a.k.a After Properties).
     * These properties collect different metrics and information during component execution.
     * Values of these properties are returned after component finished his work.
     * Runtime Platform may use this method to retrieve a this list and show in UI
     * Here, it is defined 2 properties: <br>
     * 1) Error message
     * 2) Number of records processed
     * For Error message property no efforts are required from component developer to set its value.
     * Runtime Platform will set its value by itself in case of Exception in runtime.
     * As for Number of records property see Reader implementation in runtime part
     */
    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_TOTAL_RECORD_COUNT_PROP, RETURN_ERROR_MESSAGE_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return MarkLogicInputProperties.class;
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ExecutionEngine engine, ComponentProperties properties,
            ConnectorTopology connectorTopology) {
        assertEngineCompatibility(engine);
        assertConnectorTopologyCompatibility(connectorTopology);

        String runtimeInputPackageName = "org.talend.components.marklogic.runtime.input";
        String runtimeClassName = connectorTopology == ConnectorTopology.OUTGOING ?
                "MarkLogicSource" : "MarkLogicInputSink";

        return RuntimeInfoProvider.getCommonRuntimeInfo(runtimeInputPackageName + "." + runtimeClassName);

    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.OUTGOING, ConnectorTopology.INCOMING_AND_OUTGOING,
                ConnectorTopology.NONE); //NONE is workaround for jet_stub
    }

    @Override
    public boolean isSchemaAutoPropagate() {
        return true;
    }
}
