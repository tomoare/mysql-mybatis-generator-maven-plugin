package com.tomoare.mybatis.generator.plugins;

import com.tomoare.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyForUpdateElementGenerator;
import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 *
 * @author yuki.okazaki
 */
public class SelectForUpdatePlugin extends PluginAdapter {

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        AbstractXmlElementGenerator elementGenerator = new SelectByPrimaryKeyForUpdateElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, document.getRootElement(), introspectedTable);
        return true;
    }
    
    protected void initializeAndExecuteGenerator(
            AbstractXmlElementGenerator elementGenerator,
            XmlElement parentElement,
            IntrospectedTable introspectedTable) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(null);
        elementGenerator.setWarnings(null);
        elementGenerator.addElements(parentElement);
    }    
}
