package com.tomoare.mybatis.generator.plugins;

import java.util.ArrayList;
import java.util.List;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 *
 * @author tomoare
 */
public class DirtyFlgPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {

        List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();

        List<Method> isMethod = new ArrayList<Method>();

        List<String> clearBody = new ArrayList<String>();
        
        for (IntrospectedColumn column : columns) {

            Field field = new Field();
            field.setType(new FullyQualifiedJavaType("boolean"));
            field.setInitializationString("false");
            field.setName(fieldName(column));
            field.setVisibility(JavaVisibility.PRIVATE);
            context.getCommentGenerator().addFieldComment(field, introspectedTable, column);

            topLevelClass.addField(field);

            Method m = new Method();
            m.setConstructor(false);
            m.setReturnType(new FullyQualifiedJavaType("boolean"));
            m.setVisibility(JavaVisibility.PUBLIC);
            m.setName(JavaBeansUtil.getGetterMethodName(fieldName(column), new FullyQualifiedJavaType("boolean")));
            m.addBodyLine("return this." + fieldName(column) + ";");
            isMethod.add(m);
            
            clearBody.add("this." + fieldName(column) + " = false;");
            
        }

        for (Method m : isMethod) {
            topLevelClass.addMethod(m);
        }
        
        Method clearMethod = new Method();
        clearMethod.setName("clearDirtyFlg");
        clearMethod.setVisibility(JavaVisibility.PUBLIC);
        for (String body : clearBody) {
            clearMethod.addBodyLine(body);
        }
        topLevelClass.addMethod(clearMethod);

        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {

        if (introspectedTable.getPrimaryKeyColumns().contains(introspectedColumn)) {
            return true;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("this.").append(fieldName(introspectedColumn)).append(" = true;");
        method.addBodyLine(sb.toString());
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {

        List<Element> elem = element.getElements();
        for (Element el : elem) {
            if (el instanceof XmlElement) {
                XmlElement dynamicElement = (XmlElement) el;
                if ("set".equals(dynamicElement.getName())) {
                    dynamicElement.getElements().clear();
                    StringBuilder sb = new StringBuilder();
                    for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
                        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
                        sb.setLength(0);
                        sb.append(fieldName(introspectedColumn));
                        isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
                        dynamicElement.addElement(isNotNullElement);

                        sb.setLength(0);
                        sb.append(MyBatis3FormattingUtilities
                                .getEscapedColumnName(introspectedColumn));
                        sb.append(" = "); //$NON-NLS-1$
                        sb.append(MyBatis3FormattingUtilities
                                .getParameterClause(introspectedColumn));
                        sb.append(',');

                        isNotNullElement.addElement(new TextElement(sb.toString()));
                    }
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elem = element.getElements();
        for (Element el : elem) {
            if (el instanceof XmlElement) {
                XmlElement dynamicElement = (XmlElement) el;
                if ("set".equals(dynamicElement.getName())) {
                    dynamicElement.getElements().clear();
                    StringBuilder sb = new StringBuilder();
                    for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
                        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
                        sb.setLength(0);
                        sb.append("record."); //$NON-NLS-1$
                        sb.append(fieldName(introspectedColumn));
                        isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
                        dynamicElement.addElement(isNotNullElement);

                        sb.setLength(0);
                        sb.append(MyBatis3FormattingUtilities
                                .getAliasedEscapedColumnName(introspectedColumn));
                        sb.append(" = "); //$NON-NLS-1$
                        sb.append(MyBatis3FormattingUtilities.getParameterClause(
                                introspectedColumn, "record.")); //$NON-NLS-1$
                        sb.append(',');

                        isNotNullElement.addElement(new TextElement(sb.toString()));
                    }
                    break;
                }
            }
        }

        return true;
    }

    private String fieldName(IntrospectedColumn column) {
        StringBuilder sb = new StringBuilder(column.getJavaProperty());
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "dirtyFlg");
        return sb.toString();
    }
}
