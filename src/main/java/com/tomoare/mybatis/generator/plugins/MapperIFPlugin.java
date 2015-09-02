package com.tomoare.mybatis.generator.plugins;

import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 *
 * @author tomoare
 */
public class MapperIFPlugin extends PluginAdapter {

    String mapperIfName;

    public boolean validate(List<String> warnings) {
        boolean valid = true;

        if (properties.containsKey("mapperIfName")) {
            mapperIfName = properties.getProperty("mapperIfName");
        } else {
            valid = false;
        }
        return valid;
    }

    /**
     * java mapper
     *
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(
            Interface interfaze,
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {

        FullyQualifiedJavaType mapperIF = new FullyQualifiedJavaType(mapperIfName);

        // clear method
        interfaze.getMethods().clear();

        // <PKEY, ENTITY, EXAMPLE>
        interfaze.addImportedType(mapperIF);
        interfaze.addSuperInterface(mapperIF);

        mapperIF.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType()));
        mapperIF.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        mapperIF.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getExampleType()));

        return true;
    }
}
