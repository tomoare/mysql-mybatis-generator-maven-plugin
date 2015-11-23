package com.tomoare.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import com.tomoare.mybatis.generator.util.ElementUtil;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;

/**
 *
 * @author yuki.okazaki
 */
public class SelectByPrimaryKeyForUpdateElementGenerator extends SelectByPrimaryKeyElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {

        int presize = parentElement.getElements().size();

        super.addElements(parentElement);

        int postsize = parentElement.getElements().size();

        if (presize != postsize) {
            XmlElement answer = (XmlElement) parentElement.getElements().get(postsize - 1);
            answer.addElement(new TextElement("for update"));
            ElementUtil.replaceId(answer, introspectedTable.getSelectByPrimaryKeyStatementId() + "ForUpdate");
        }
    }
}
