package org.talend.components.jdbc.dataset;

import org.apache.avro.Schema;
import org.talend.components.common.SchemaProperties;
import org.talend.components.common.dataset.DatasetProperties;
import org.talend.components.jdbc.CommonUtils;
import org.talend.components.jdbc.RuntimeSettingProvider;
import org.talend.components.jdbc.datastore.JDBCDatastoreProperties;
import org.talend.components.jdbc.runtime.dataprep.JDBCDatasetRuntime;
import org.talend.components.jdbc.runtime.setting.AllSetting;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.runtime.RuntimeInfo;
import org.talend.daikon.runtime.RuntimeUtil;
import org.talend.daikon.sandbox.SandboxedInstance;

public class JDBCDatasetProperties extends PropertiesImpl
        implements DatasetProperties<JDBCDatastoreProperties>, RuntimeSettingProvider {

    public JDBCDatastoreProperties datastore = new JDBCDatastoreProperties("datastore");

    public Property<String> sql = PropertyFactory.newString("sql").setRequired(true);

    public SchemaProperties main = new SchemaProperties("main") {

        @SuppressWarnings("unused")
        public void beforeSchema() {
            updateSchema();
        }

    };

    public void updateSchema() {
        JDBCDatasetDefinition definition = new JDBCDatasetDefinition();
        RuntimeInfo runtimeInfo = definition.getRuntimeInfo(this, null);
        try (SandboxedInstance sandboxedInstance = RuntimeUtil.createRuntimeClass(runtimeInfo, getClass().getClassLoader())) {
            JDBCDatasetRuntime runtime = (JDBCDatasetRuntime) sandboxedInstance.getInstance();
            runtime.initialize(null, this);
            Schema schema = runtime.getSchemaFromQuery(null, sql.getValue());
            main.schema.setValue(schema);
        }
    }

    public JDBCDatasetProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        sql.setValue("\"select * from mytable\"");
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = CommonUtils.addForm(this, Form.MAIN);
        mainForm.addRow(sql);

        mainForm.addRow(main.getForm(Form.REFERENCE));
    }

    @Override
    public JDBCDatastoreProperties getDatastoreProperties() {
        return datastore;
    }

    @Override
    public void setDatastoreProperties(JDBCDatastoreProperties datastoreProperties) {
        datastore = datastoreProperties;
    }

    @Override
    public AllSetting getRuntimeSetting() {
        AllSetting setting = new AllSetting();

        setting.setDriverPaths(datastore.getCurrentDriverPaths());
        setting.setDriverClass(datastore.driverClass.getValue());
        setting.setJdbcUrl(datastore.jdbcUrl.getValue());

        setting.setUsername(datastore.userPassword.userId.getValue());
        setting.setPassword(datastore.userPassword.password.getValue());

        setting.setSql(sql.getValue());

        return setting;
    }

}
