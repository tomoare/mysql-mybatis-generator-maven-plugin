package com.tomoare.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByExampleWithoutBLOBsElementGenerator;

/**
 *
 * @author yuki.okazaki
 */
public class ExistByExampleElementGenerator extends SelectByExampleWithoutBLOBsElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        String fqjt = introspectedTable.getExampleType();

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                "existByExample"));
        answer.addAttribute(new Attribute(
                "resultType", "java.lang.Boolean")); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", fqjt)); //$NON-NLS-1$

        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select")); //$NON-NLS-1$
        answer.addElement(new TextElement("    1")); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement((new TextElement(sb.toString())));
        answer.addElement(getExampleIncludeElement());
        answer.addElement(new TextElement("limit 0 , 1"));

        parentElement.addElement(answer);
    }
}
