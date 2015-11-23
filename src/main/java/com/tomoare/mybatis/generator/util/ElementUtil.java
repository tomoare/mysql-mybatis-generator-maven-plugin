package com.tomoare.mybatis.generator.util;

import java.util.List;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 *
 * @author tomoare
 */
public class ElementUtil {

    private ElementUtil() {
    }

    public static void replaceId(XmlElement answer, String name) {

        for (int i = 0; i < answer.getAttributes().size(); i++) {
            Attribute attr = answer.getAttributes().get(i);
            if ("id".equals(attr.getName())) {
//                answer.getAttributes().remove(attr);
                Attribute replaceAttr = new Attribute("id", name);
                answer.getAttributes().set(i, replaceAttr);
                break;
            }
        }
    }

    public static void removeElement(XmlElement answer, String name) {
        List<Element> elements = answer.getElements();
        for (Element element : elements) {
            if (element instanceof XmlElement) {
                XmlElement xmlElement = (XmlElement) element;
                if (name.equals(xmlElement.getName())) {
                    elements.remove(xmlElement);
                    break;
                }
            }
        }
    }
}
