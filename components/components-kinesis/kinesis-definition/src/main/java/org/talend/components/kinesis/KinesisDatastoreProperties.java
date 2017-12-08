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

package org.talend.components.kinesis;

import static org.talend.daikon.properties.presentation.Widget.widget;

import java.util.EnumSet;

import org.talend.components.common.datastore.DatastoreProperties;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class KinesisDatastoreProperties extends PropertiesImpl implements DatastoreProperties {

    public Property<Boolean> specifyCredentials = PropertyFactory.newBoolean("specifyCredentials", true).setRequired();

    public Property<String> accessKey = PropertyFactory.newString("accessKey");

    public Property<String> secretKey = PropertyFactory.newString("secretKey").setFlags(
            EnumSet.of(Property.Flags.ENCRYPT, Property.Flags.SUPPRESS_LOGGING));

    public Property<Boolean> specifySTS = PropertyFactory.newBoolean("specifySTS", false).setRequired();

    public Property<String> roleArn = PropertyFactory.newString("roleArn");

    public Property<String> roleSessionName = PropertyFactory.newString("roleSessionName");

    public Property<String> roleExternalId = PropertyFactory.newString("roleExternalId");

    public Property<Boolean> specifyEndpoint = PropertyFactory.newBoolean("specifyEndpoint", false).setRequired();

    public Property<String> endpoint = PropertyFactory.newString("endpoint");

    public KinesisDatastoreProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(specifyCredentials);
        mainForm.addRow(accessKey);
        mainForm.addRow(widget(secretKey).setWidgetType(Widget.HIDDEN_TEXT_WIDGET_TYPE));
        mainForm.addRow(specifySTS);
        mainForm.addRow(roleArn);
        mainForm.addRow(roleSessionName);
        mainForm.addRow(roleExternalId);
        mainForm.addRow(specifyEndpoint);
        mainForm.addRow(endpoint);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        // Main properties
        if (form.getName().equals(Form.MAIN)) {
            // handle S3
            final boolean isSpecifyCredentialsEnabled = specifyCredentials.getValue();
            accessKey.setRequired(isSpecifyCredentialsEnabled);
            secretKey.setRequired(isSpecifyCredentialsEnabled);
            form.getWidget(accessKey.getName()).setVisible(isSpecifyCredentialsEnabled);
            form.getWidget(secretKey.getName()).setVisible(isSpecifyCredentialsEnabled);

            final boolean isSpecifySTS = specifySTS.getValue();
            roleArn.setRequired(isSpecifySTS);
            roleSessionName.setRequired(isSpecifySTS);
            roleExternalId.setRequired(isSpecifySTS);
            form.getWidget(roleArn.getName()).setVisible(isSpecifySTS);
            form.getWidget(roleSessionName.getName()).setVisible(isSpecifySTS);
            form.getWidget(roleExternalId.getName()).setVisible(isSpecifySTS);

            endpoint.setRequired(specifyEndpoint.getValue());
            form.getWidget(endpoint.getName()).setVisible(specifyEndpoint.getValue());
        }
    }

    public void afterSpecifyCredentials() {
        refreshLayout(getForm(Form.MAIN));
    }

    public void afterSpecifySTS() {
        refreshLayout(getForm(Form.MAIN));
    }

    public void afterSpecifyEndpoint() {
        refreshLayout(getForm(Form.MAIN));
    }

}
