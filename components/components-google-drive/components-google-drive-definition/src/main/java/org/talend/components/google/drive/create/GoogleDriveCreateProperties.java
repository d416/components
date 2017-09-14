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
package org.talend.components.google.drive.create;

import static org.talend.daikon.properties.property.PropertyFactory.newString;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.google.drive.GoogleDriveComponentProperties;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class GoogleDriveCreateProperties extends GoogleDriveComponentProperties {

    public Property<String> parentFolder = newString("parentFolder").setRequired();

    public Property<String> newFolder = newString("newFolder").setRequired();

    public GoogleDriveCreateProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        Schema schema = SchemaBuilder.builder().record("GoogleDriveCreate").fields() //
                .name("parentfolderID")//
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true")//
                .type().nullable().stringType().noDefault() //
                .name("newFolderID")//
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true")//
                .type().nullable().stringType().noDefault() //
                .endRecord();
        schema.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        schemaMain.schema.setValue(schema);

        parentFolder.setValue("root");
        newFolder.setValue("");
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(parentFolder);
        mainForm.addRow(newFolder);
        mainForm.addRow(schemaMain.getForm(Form.REFERENCE));
    }

}
