package com.tomoare.mybatis.generator.plugins;

import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author tomaore
 */
public class PaginatorPlugin extends PluginAdapter {

    private final FullyQualifiedJavaType pageable;

    public PaginatorPlugin() {
        super();
        pageable = new FullyQualifiedJavaType(Pageable.class.getName());
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        topLevelClass.addImportedType(pageable);

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setName("pageable");
        field.setType(pageable);

        topLevelClass.addField(field);

        context.getCommentGenerator().addFieldComment(field, introspectedTable);

        // getter setter
        Method getter = new Method();
        getter.setVisibility(JavaVisibility.PUBLIC);
        getter.setReturnType(pageable);
        getter.setName("getPageable");
        getter.addBodyLine("return pageable;");
        topLevelClass.addMethod(getter);
        context.getCommentGenerator().addGeneralMethodComment(getter, introspectedTable);

        // getter setter
        Method setter = new Method();
        setter.setVisibility(JavaVisibility.PUBLIC);
        setter.addParameter(new Parameter(pageable, "pageable"));
        setter.setName("setPageable");
        setter.addBodyLine("this.pageable = pageable;");
        topLevelClass.addMethod(setter);
        context.getCommentGenerator().addGeneralMethodComment(setter, introspectedTable);

        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element,
            IntrospectedTable introspectedTable) {

        XmlElement isNotNullElement = new XmlElement("if");
        isNotNullElement.addAttribute(new Attribute("test", "pageable != null"));
        isNotNullElement.addElement(new TextElement("limit #{pageable.offset}, #{pageable.size}"));
        element.getElements().add(isNotNullElement);
        return true;
    }
}
