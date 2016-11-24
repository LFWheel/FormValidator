package com.zj.txvalidator.validators;

import android.support.v4.util.LruCache;

import com.zj.txvalidator.annotations.ValidatorFor;
import com.zj.txvalidator.inter.IValidator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 2016/11/23.
 */

public class ValidatorFactory {
    private static final int INSTANCE_CACHE_SIZE = 4;

    private static final LruCache<Class<? extends IValidator>, IValidator> sCachedValidatorInstances = new LruCache<>(INSTANCE_CACHE_SIZE);
    private static final Map<Class<? extends Annotation>, Class<? extends IValidator>> sValidators = new HashMap<>();

    static {
        // our default validators:

        //noinspection unchecked
        registerValidatorClasses(NotEmptyValidator.class);
    }

    public static void registerValidatorClasses(Class<? extends IValidator<?>>... classes) {
        if (classes == null || classes.length == 0) {
            return;
        }

        for (Class<? extends IValidator<?>> clazz : classes) {
            final Annotation[] annotations = clazz.getAnnotations();

            // search for @ValidatorFor annotation and read supported validations
            for (Annotation annotation : annotations) {
                if (annotation instanceof ValidatorFor) {
                    Class<? extends Annotation>[] validationAnnotations = ((ValidatorFor) annotation).value();
                    for (Class<? extends Annotation> validationAnnotation : validationAnnotations) {
                        sValidators.put(validationAnnotation, clazz);
                    }
                    break;
                }
            }
        }
    }
    public static IValidator createValidator(Annotation annotation)throws IllegalAccessException, InstantiationException {
        if (annotation == null) {
            return null;
        }

        final Class<? extends IValidator> clazz = sValidators.get(annotation.annotationType());

        IValidator validator = null;
        if (clazz != null) {
            validator = sCachedValidatorInstances.get(clazz);
            if (validator == null) {
                validator = clazz.newInstance();
                sCachedValidatorInstances.put(clazz, validator);
            }
        }
        return validator;
    }
}
