/*
 * /*
 *  * Copyright 2010,  Unitils.org
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  */
package org.dbmaintain.database;

import org.dbmaintain.config.PropertyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.dbmaintain.config.DbMaintainProperties.*;
import static org.dbmaintain.config.PropertyUtils.*;

/**
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class DatabaseInfoFactory {

    protected Properties configuration;

    public DatabaseInfoFactory(Properties configuration) {
        this.configuration = configuration;
    }


    public List<DatabaseInfo> getDatabaseInfos() {
        List<DatabaseInfo> databaseInfos = new ArrayList<DatabaseInfo>();

        List<String> databaseNames = getStringList(PROPERTY_DATABASE_NAMES, configuration);
        if (databaseNames.isEmpty()) {
            databaseInfos.add(getUnnamedDatabaseInfo());
            return databaseInfos;
        }
        for (String databaseName : databaseNames) {
            boolean disabled = !isDatabaseIncluded(databaseName);
            DatabaseInfo databaseInfo = createDatabaseInfo(databaseName, disabled);
            databaseInfos.add(databaseInfo);
        }
        return databaseInfos;
    }


    protected DatabaseInfo getUnnamedDatabaseInfo() {
        String driverClassName = getProperty(null, PROPERTY_DRIVERCLASSNAME_END);
        String url = getProperty(null, PROPERTY_URL_END);
        String userName = getProperty(null, PROPERTY_USERNAME_END);
        String password = getProperty(null, PROPERTY_PASSWORD_END);
        String databaseDialect = getProperty(null, PROPERTY_DIALECT_END);
        List<String> schemaNames = getListProperty(null, PROPERTY_SCHEMANAMES_END);
        return new DatabaseInfo("<no-name>", databaseDialect, driverClassName, url, userName, password, schemaNames, false);
    }


    /**
     * @param databaseName The name that identifies the database, not null
     * @param disabled     True if this database is disabled
     * @return a DataSource that connects with the database as configured for the given database name
     */
    protected DatabaseInfo createDatabaseInfo(String databaseName, boolean disabled) {
        String driverClassName = getProperty(databaseName, PROPERTY_DRIVERCLASSNAME_END);
        String url = getProperty(databaseName, PROPERTY_URL_END);
        String userName = getProperty(databaseName, PROPERTY_USERNAME_END);
        String password = getProperty(databaseName, PROPERTY_PASSWORD_END);
        String databaseDialect = getProperty(databaseName, PROPERTY_DIALECT_END);
        List<String> schemaNames = getListProperty(databaseName, PROPERTY_SCHEMANAMES_END);
        return new DatabaseInfo(databaseName, databaseDialect, driverClassName, url, userName, password, schemaNames, disabled);
    }

    /**
     * @param databaseName the logical name that identifies the database
     * @return whether the database with the given name is included in the set of database to be updated by db maintain
     */
    protected boolean isDatabaseIncluded(String databaseName) {
        return PropertyUtils.getBoolean(PROPERTY_DATABASE_START + '.' + databaseName + '.' + PROPERTY_INCLUDED_END, true, configuration);
    }


    protected String getProperty(String databaseName, String propertyNameEnd) {
        if (databaseName != null) {
            String customPropertyName = PROPERTY_DATABASE_START + '.' + databaseName + '.' + propertyNameEnd;
            if (containsProperty(customPropertyName, configuration)) {
                return getString(customPropertyName, "", configuration);
            }
        }
        String defaultPropertyName = PROPERTY_DATABASE_START + '.' + propertyNameEnd;
        return getString(defaultPropertyName, "", configuration);
    }

    protected List<String> getListProperty(String databaseName, String propertyNameEnd) {
        if (databaseName != null) {
            String customPropertyName = PROPERTY_DATABASE_START + '.' + databaseName + '.' + propertyNameEnd;
            if (containsProperty(customPropertyName, configuration)) {
                return getStringList(customPropertyName, configuration, false);
            }
        }
        String defaultPropertyName = PROPERTY_DATABASE_START + '.' + propertyNameEnd;
        return getStringList(defaultPropertyName, configuration, false);
    }


}
