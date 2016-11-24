package com.zj.txvalidator;

import com.zj.txvalidator.inter.IValidator;

import java.lang.annotation.Annotation;

/**
 * Created by ASUS on 2016/11/23.
 */

public class ValidationInfo {
    private final Annotation annotation;
    private final IValidator validator;
    final int order;

    ValidationInfo(Annotation annotation, IValidator validator) {
        this.annotation = annotation;
        this.validator = validator;
        this.order = validator.getOrder(annotation);
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public IValidator getValidator() {
        return validator;
    }

    public int getOrder() {
        return order;
    }
}
