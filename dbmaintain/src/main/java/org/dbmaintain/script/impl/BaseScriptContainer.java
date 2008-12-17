/*
 * Copyright 2008 De Post, N.V. - La Poste, S.A. All rights reserved
 */
package org.dbmaintain.script.impl;

import static org.dbmaintain.config.DbMaintainProperties.PROPERTY_POSTPROCESSINGSCRIPTS_DIRNAME;
import static org.dbmaintain.config.DbMaintainProperties.PROPERTY_SCRIPT_ENCODING;
import static org.dbmaintain.config.DbMaintainProperties.PROPERTY_SCRIPT_PATCH_QUALIFIERS;
import static org.dbmaintain.config.DbMaintainProperties.PROPERTY_SCRIPT_QUALIFIER_PREFIX;
import static org.dbmaintain.config.DbMaintainProperties.PROPERTY_SCRIPT_TARGETDATABASE_PREFIX;
import static org.dbmaintain.config.DbMaintainProperties.PROPERTY_SCRIPT_EXTENSIONS;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.dbmaintain.config.PropertyUtils;
import org.dbmaintain.script.Script;
import org.dbmaintain.script.ScriptContainer;
import org.dbmaintain.util.DbMaintainException;

/**
 * Base class for a container for database scripts
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
abstract public class BaseScriptContainer implements ScriptContainer {

    /**
     * Name of the properties file that is packaged with the jar, that contains information about how
     * the scripts in the jar file are structured.
     */
    public static final String LOCATION_PROPERTIES_FILENAME = "META-INF/dbscripts.properties";

    protected List<Script> scripts;
    protected Set<String> scriptFileExtensions;
    protected String targetDatabasePrefix;
    protected String qualifierPrefix;
    protected Set<String> patchQualifiers;
    protected String postProcessingScriptDirName;
    protected String scriptEncoding;
    

    public List<Script> getScripts() {
        return scripts;
    }


    public Set<String> getScriptFileExtensions() {
        return scriptFileExtensions;
    }


    public String getTargetDatabasePrefix() {
        return targetDatabasePrefix;
    }

    
    public String getQualifierPrefix() {
        return qualifierPrefix;
    }


    public Set<String> getPatchQualifiers() {
        return patchQualifiers;
    }


    public String getPostProcessingScriptDirName() {
        return postProcessingScriptDirName;
    }


    public String getScriptEncoding() {
        return scriptEncoding;
    }


    /**
     * @param customProperties
     * @param defaultScriptFileExtensions
     * @param defaultTargetDatabasePrefix
     * @param defaultQualifierPrefix
     * @param defaultPatchQualifiers
     * @param defaultPostProcessingScriptDirName
     * @param defaultScriptEncoding
     */
    protected void initConfiguration(Properties customProperties, Set<String> defaultScriptFileExtensions, String defaultTargetDatabasePrefix,
                                   String defaultQualifierPrefix, Set<String> defaultPatchQualifiers, String defaultPostProcessingScriptDirName,
                                   String defaultScriptEncoding) {
        this.scriptFileExtensions = (customProperties != null && customProperties.containsKey(PROPERTY_SCRIPT_EXTENSIONS))
                ? new HashSet<String>(PropertyUtils.getStringList(PROPERTY_SCRIPT_EXTENSIONS, customProperties)) : defaultScriptFileExtensions;
        assertValidScriptExtensions();

        this.targetDatabasePrefix = (customProperties != null && customProperties.containsKey(PROPERTY_SCRIPT_TARGETDATABASE_PREFIX))
                ? PropertyUtils.getString(PROPERTY_SCRIPT_TARGETDATABASE_PREFIX, customProperties) : defaultTargetDatabasePrefix;
        this.qualifierPrefix = (customProperties != null && customProperties.containsKey(PROPERTY_SCRIPT_QUALIFIER_PREFIX))
                ? PropertyUtils.getString(PROPERTY_SCRIPT_QUALIFIER_PREFIX, customProperties) : defaultQualifierPrefix;
        this.patchQualifiers = (customProperties != null && customProperties.containsKey(PROPERTY_SCRIPT_PATCH_QUALIFIERS))
                ? new HashSet<String>(PropertyUtils.getStringList(PROPERTY_SCRIPT_PATCH_QUALIFIERS, customProperties)) : defaultPatchQualifiers;
        this.postProcessingScriptDirName = (customProperties != null && customProperties.containsKey(PROPERTY_POSTPROCESSINGSCRIPTS_DIRNAME))
                ? PropertyUtils.getString(PROPERTY_POSTPROCESSINGSCRIPTS_DIRNAME, customProperties) : defaultPostProcessingScriptDirName;
        this.scriptEncoding = (customProperties != null && customProperties.containsKey(PROPERTY_SCRIPT_ENCODING))
                        ? PropertyUtils.getString(PROPERTY_SCRIPT_ENCODING, customProperties) : defaultScriptEncoding;
    }

    protected void assertValidScriptExtensions() {
        // check whether an extension is configured
        if (scriptFileExtensions.isEmpty()) {
            throw new DbMaintainException("No script file extensions specified!");
        }
        // Verify the correctness of the script extensions
        for (String extension : scriptFileExtensions) {
            if (extension.startsWith(".")) {
                throw new DbMaintainException("Script file extension " + extension + " should not start with a '.'");
            }
        }
    }
}