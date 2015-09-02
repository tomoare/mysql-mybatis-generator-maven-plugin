package com.tomoare.mybatis.generator.plugins;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 *
 * @author tomoare
 */
public class ToStringPlugin extends PluginAdapter {

    private final FullyQualifiedJavaType toStringType;
    private final FullyQualifiedJavaType toStringStyleType;
    private final FullyQualifiedJavaType returnType;

    public ToStringPlugin() {
        super();
        toStringType = new FullyQualifiedJavaType(ToStringBuilder.class.getName());
        toStringStyleType = new FullyQualifiedJavaType(ToStringStyle.class.getName());
        returnType = new FullyQualifiedJavaType("java.lang.String");
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        makeToString(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        makeToString(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeToString(topLevelClass, introspectedTable);
        return true;
    }

    protected void makeToString(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(toStringType);
        topLevelClass.addImportedType(toStringStyleType);

        Method method = new Method();
        method.setConstructor(false);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setName("toString");
        method.setReturnType(returnType);
        method.addBodyLine("return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);");
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        topLevelClass.addMethod(method);
    }
}
