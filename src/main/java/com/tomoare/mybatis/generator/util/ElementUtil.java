package com.tomoare.mybatis.generator.util;

import org.mybatis.generator.api.dom.xml.Attribute;
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
}
