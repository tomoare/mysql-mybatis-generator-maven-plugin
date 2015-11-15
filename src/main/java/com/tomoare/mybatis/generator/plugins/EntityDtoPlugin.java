package com.tomoare.mybatis.generator.plugins;

import java.util.List;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 *
 * @author tomoare
 */
public class EntityDtoPlugin extends PluginAdapter {

    private String preTableName;

    private IntrospectedTable introspectedTable;

    private String modelPackage;

    @Override
    public boolean validate(List<String> warnings) {
        modelPackage = properties.getProperty("modelPackage");
        return true;
    }

    @Override
    public void initialized(IntrospectedTable table) {
        super.initialized(table);

        String name = table.getBaseRecordType();
        String[] splitsName = name.split("\\.");
        preTableName = splitsName[splitsName.length - 1];
        table.setBaseRecordType(name + "Dto");
        this.introspectedTable = table;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {

        topLevelClass.getFields().clear();

        FullyQualifiedJavaType entity = new FullyQualifiedJavaType(modelPackage + preTableName);
        Parameter constractParameter = new Parameter(entity, "entity");

        Field entityField = new Field();
        entityField.setType(entity);
        entityField.setVisibility(JavaVisibility.PRIVATE);
        entityField.setName("entity");

        Method constractMethod = new Method();
        constractMethod.setConstructor(true);
        constractMethod.setName(topLevelClass.getType().getShortName());
        constractMethod.setVisibility(JavaVisibility.PUBLIC);
        constractMethod.addParameter(constractParameter);
        constractMethod.addBodyLine("this.entity = entity;");

        topLevelClass.addMethod(constractMethod);
        topLevelClass.addImportedType(entity);

        topLevelClass.addField(entityField);
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze,
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap,
            IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {

        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        method.getBodyLines().clear();
        StringBuilder sb = new StringBuilder();
        sb.append("return entity.");
        sb.append(JavaBeansUtil.getGetterMethodName(property, fqjt));
        sb.append("();");
        method.addBodyLine(sb.toString());

        if (fqjt.getFullyQualifiedName().contains("Enum")) {
            Method mm = new Method();
            mm.setVisibility(JavaVisibility.PUBLIC);
            mm.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
            mm.setName(JavaBeansUtil.getGetterMethodName(property, fqjt) + "DispName");

            StringBuilder ss = new StringBuilder();
            ss.append("return ");
            ss.append(JavaBeansUtil.getGetterMethodName(property, fqjt));
            ss.append("() == null ? null : ");
            ss.append(JavaBeansUtil.getGetterMethodName(property, fqjt));
            ss.append("().getDispName();");
            mm.addBodyLine(ss.toString());
            topLevelClass.addMethod(mm);
        }

        return true;
    }

    public Method getJavaBeansGetter(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);
        method.setName(JavaBeansUtil.getGetterMethodName(property, fqjt));

        StringBuilder sb = new StringBuilder();
        sb.append("return "); //$NON-NLS-1$
        sb.append(property);
        sb.append(';');
        method.addBodyLine(sb.toString());

        return method;
    }

}
