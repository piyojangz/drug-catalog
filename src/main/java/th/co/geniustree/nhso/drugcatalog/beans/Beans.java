/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author moth
 */
public class Beans {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Beans.class);
    public static void copy(Object source, Object target) {
        Field[] declaredFields = source.getClass().getDeclaredFields();
        Map<Class<? extends Converter>, Converter> converters = new HashMap<>();
        for (Field sourceField : declaredFields) {
            sourceField.setAccessible(true);
            try {
                Field targetField = target.getClass().getDeclaredField(sourceField.getName());
                PropertyConverter annotation = sourceField.getAnnotation(PropertyConverter.class);
                if (annotation != null) {
                    if (!converters.containsKey(annotation.value())) {
                        Converter newInstance = annotation.value().newInstance();
                        converters.put(annotation.value(), newInstance);
                    }
                }
                char[] stringArray = sourceField.getName().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                String uperCaseName = new String(stringArray);
                Method declaredMethod = target.getClass().getMethod("set" + uperCaseName, targetField.getType());
                log.info("declearMethod target :"+declaredMethod);
                if (annotation != null) {
                    declaredMethod.invoke(target, converters.get(annotation.value()).convert(sourceField.get(source)));
                } else {
                    declaredMethod.invoke(target, sourceField.get(source));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }
}
