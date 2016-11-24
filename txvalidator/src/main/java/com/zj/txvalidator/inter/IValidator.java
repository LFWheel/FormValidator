package com.zj.txvalidator.inter;

import android.content.Context;

import java.lang.annotation.Annotation;

/**
 * Created by ASUS on 2016/11/23.
 */

public interface IValidator<T> {
    boolean validate(Annotation annotation, T input);

    String getMessage(Context context, Annotation annotation, T input);

    int getOrder(Annotation annotation);
}
