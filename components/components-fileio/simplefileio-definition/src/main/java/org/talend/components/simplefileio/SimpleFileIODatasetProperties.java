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

package org.talend.components.simplefileio;

import org.talend.components.common.dataset.DatasetProperties;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.ReferenceProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class SimpleFileIODatasetProperties extends PropertiesImpl implements DatasetProperties<SimpleFileIODatastoreProperties> {

    // HDFS
    public Property<SimpleFileIOFormat> format = PropertyFactory.newEnum("format", SimpleFileIOFormat.class).setRequired();

    public Property<String> path = PropertyFactory.newString("path", "").setRequired();

    public Property<RecordDelimiterType> recordDelimiter = PropertyFactory.newEnum("recordDelimiter", RecordDelimiterType.class)
            .setValue(RecordDelimiterType.LF);

    public Property<String> specificRecordDelimiter = PropertyFactory.newString("specificRecordDelimiter", "\\n");

    public Property<FieldDelimiterType> fieldDelimiter = PropertyFactory.newEnum("fieldDelimiter", FieldDelimiterType.class)
            .setValue(FieldDelimiterType.SEMICOLON);

    public Property<String> specificFieldDelimiter = PropertyFactory.newString("specificFieldDelimiter", ";");

    // S3
    public Property<String> bucket = PropertyFactory.newString("bucket");

    public Property<String> object = PropertyFactory.newString("object");

    public Property<Boolean> encryptDataInMotion = PropertyFactory.newBoolean("encryptDataInMotion", false);

    public Property<String> kmsForDataInMotion = PropertyFactory.newString("kmsForDataInMotion");

    public Property<Boolean> encryptDataAtRest = PropertyFactory.newBoolean("encryptDataAtRest", false);

    public Property<String> kmsForDataAtRest = PropertyFactory.newString("kmsForDataAtRest");

    public final transient ReferenceProperties<SimpleFileIODatastoreProperties> datastoreRef = new ReferenceProperties<>(
            "datastoreRef", SimpleFileIODatastoreDefinition.NAME);

    public SimpleFileIODatasetProperties(String name) {
        super(name);
    }

    @Override
    public SimpleFileIODatastoreProperties getDatastoreProperties() {
        return datastoreRef.getReference();
    }

    @Override
    public void setDatastoreProperties(SimpleFileIODatastoreProperties datastoreProperties) {
        datastoreRef.setReference(datastoreProperties);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        format.setValue(SimpleFileIOFormat.CSV);
        setDatastoreProperties(new SimpleFileIODatastoreProperties("init"));
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);

        // HDFS
        mainForm.addRow(format);
        mainForm.addRow(path);
        mainForm.addRow(recordDelimiter);
        mainForm.addRow(specificRecordDelimiter);
        mainForm.addRow(fieldDelimiter);
        mainForm.addRow(specificFieldDelimiter);

        // S3
        mainForm.addRow(bucket);
        mainForm.addRow(object);
        mainForm.addRow(encryptDataInMotion);
        mainForm.addRow(kmsForDataInMotion);
        mainForm.addRow(encryptDataAtRest);
        mainForm.addRow(kmsForDataAtRest);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        // Main properties
        if (form.getName().equals(Form.MAIN)) {
            // HDFS
            boolean useHDFS = FileSystemType.HDFS.equals(getDatastoreProperties().fileSystemType.getValue());

            form.getWidget(format).setVisible(useHDFS);
            form.getWidget(path).setVisible(useHDFS);

            boolean isCSV = format.getValue() == SimpleFileIOFormat.CSV;
            form.getWidget(recordDelimiter).setVisible(useHDFS && isCSV);
            form.getWidget(specificRecordDelimiter)
                    .setVisible(useHDFS && isCSV && recordDelimiter.getValue().equals(RecordDelimiterType.OTHER));

            form.getWidget(fieldDelimiter).setVisible(useHDFS && isCSV);
            form.getWidget(specificFieldDelimiter)
                    .setVisible(useHDFS && isCSV && fieldDelimiter.getValue().equals(FieldDelimiterType.OTHER));

            // S3
            boolean useS3 = FileSystemType.S3.equals(getDatastoreProperties().fileSystemType.getValue());
            form.getWidget(bucket.getName()).setVisible(useS3);
            form.getWidget(object.getName()).setVisible(useS3);
            form.getWidget(encryptDataInMotion.getName()).setVisible(useS3);
            boolean isVisibleEncryptDataInMotion = form.getWidget(encryptDataInMotion).isVisible()
                    && encryptDataInMotion.getValue();
            form.getWidget(kmsForDataInMotion.getName()).setVisible(isVisibleEncryptDataInMotion);
            form.getWidget(encryptDataAtRest.getName()).setVisible(useS3);
            boolean isVisibleEncryptDataAtRest = form.getWidget(encryptDataInMotion).isVisible()
                    && encryptDataInMotion.getValue();
            form.getWidget(kmsForDataAtRest.getName()).setVisible(isVisibleEncryptDataAtRest);
        }
    }

    public void afterFieldDelimiter() {
        refreshLayout(getForm(Form.MAIN));
    }

    public void afterRecordDelimiter() {
        refreshLayout(getForm(Form.MAIN));
    }

    public String getRecordDelimiter() {
        if (RecordDelimiterType.OTHER.equals(recordDelimiter.getValue())) {
            return specificRecordDelimiter.getValue();
        } else {
            return recordDelimiter.getValue().getDelimiter();
        }
    }

    public String getFieldDelimiter() {
        if (FieldDelimiterType.OTHER.equals(fieldDelimiter.getValue())) {
            return specificFieldDelimiter.getValue();
        } else {
            return fieldDelimiter.getValue().getDelimiter();
        }
    }

    public void afterFormat() {
        refreshLayout(getForm(Form.MAIN));
    }

    public enum RecordDelimiterType {
        LF("\n"),
        CR("\r"),
        CRLF("\r\n"),
        OTHER("Other");

        private final String value;

        private RecordDelimiterType(final String value) {
            this.value = value;
        }

        public String getDelimiter() {
            return value;
        }
    }

    public enum FieldDelimiterType {
        SEMICOLON(";"),
        COMMA(","),
        TABULATION("\t"),
        SPACE(" "),
        OTHER("Other");

        private final String value;

        private FieldDelimiterType(final String value) {
            this.value = value;
        }

        public String getDelimiter() {
            return value;
        }
    }
}
