package com.zj.txvalidator;

import android.view.View;

import com.zj.txvalidator.exception.TxValidationException;
import com.zj.txvalidator.inter.IValidator;
import com.zj.txvalidator.validators.ValidatorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by ASUS on 2016/11/23.
 */

public class FieldFinder {
    private static final WeakHashMap<Object, Map<View, FieldInfo>> sCachedFieldsByTarget = new WeakHashMap<>();

    public static Map<View, FieldInfo> getFieldsForTarget(Object target) {
        Map<View, FieldInfo> infoMap = sCachedFieldsByTarget.get(target);
        if (infoMap != null) {
            for (View view : infoMap.keySet()) {
                // view has been removed from the window - we will need to scan fields of target again
                if (view.getWindowToken() == null) {
                    infoMap = null;

                    break;
                }
            }
        }

        if (infoMap == null) {
            infoMap = findFieldToValidate(target);
            sCachedFieldsByTarget.put(target, infoMap);
        }
        return infoMap;
    }

    private static Map<View, FieldInfo> findFieldToValidate(Object target) {
        Field fields[] = target.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return Collections.emptyMap();
        }
        WeakHashMap<View, FieldInfo> infoMap = new WeakHashMap<>(fields.length);

        for(Field field : fields){
           Annotation annotations[] = field.getDeclaredAnnotations();
            if(annotations.length>0){
                if(!View.class.isAssignableFrom(field.getType())){
                    //如果该域和View不是同类型的，则跳出本次循环，获取下一个域
                    continue;
                }
                final View view;
                try {
                    field.setAccessible(true);
                    view = (View) field.get(target);
                } catch (IllegalAccessException e) {
                    throw new TxValidationException(e);
                }
                if (view == null) {
                    continue;
                }
                List<ValidationInfo> infos = new ArrayList<>();
                for(Annotation annotation : annotations){
                    final IValidator validator;
                    try {
                        validator = ValidatorFactory.createValidator(annotation);
                    } catch (IllegalAccessException e) {
                        throw new TxValidationException(e);
                    } catch (InstantiationException e) {
                        throw new TxValidationException(e);
                    }
                    if (validator != null) {
                        ValidationInfo info = new ValidationInfo(annotation, validator);
                        infos.add(info);
                    }
                }

                if (infos.size() > 0) {
                    Collections.sort(infos, new Comparator<ValidationInfo>() {
                        @Override
                        public int compare(ValidationInfo lhs, ValidationInfo rhs) {
                            return lhs.order < rhs.order ? -1 : (lhs.order == rhs.order ? 0 : 1);
                        }
                    });
                }
                final FieldInfo fieldInfo = new FieldInfo(infos);
                infoMap.put(view, fieldInfo);
            }
        }
        return infoMap;
    }
}
