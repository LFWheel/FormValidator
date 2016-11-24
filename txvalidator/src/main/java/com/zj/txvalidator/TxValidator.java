package com.zj.txvalidator;

import android.content.Context;
import android.view.View;

import com.zj.txvalidator.inter.IFieldAdapter;
import com.zj.txvalidator.inter.ITxValidateCallback;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by ASUS on 2016/11/23.
 */

public class TxValidator {

    public static boolean validate(Context context,Object target,ITxValidateCallback callback){
        if (context == null) {
            throw new IllegalArgumentException("context cannot ben null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target cannot be null");
        }

        final List<ValidationFail> failedValidations = new ArrayList<>();
        final List<View> passedValidations = new ArrayList<>();
        boolean result = true;

        final Map<View, FieldInfo> infoMap = FieldFinder.getFieldsForTarget(target);
        for (Map.Entry<View, FieldInfo> entry : infoMap.entrySet()) {
            final FieldInfo fieldInfo = entry.getValue();
            final View view = entry.getKey();

            if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
                // don't run validation on views that are not visible
                continue;
            }

            ValidationFail fieldResult = performFieldValidations(context, fieldInfo, view);
            if (fieldResult != null) {
                failedValidations.add(fieldResult);
                result = false;
            } else {
                passedValidations.add(view);
            }

        }

        if (callback != null) {
            Collections.sort(failedValidations, new Comparator<ValidationFail>() {
                @Override
                public int compare(ValidationFail lhs, ValidationFail rhs) {
                    return lhs.order < rhs.order ? -1 : (lhs.order == rhs.order ? 0 : 1);
                }
            });
            callback.validateComplete(result, Collections.unmodifiableList(failedValidations), Collections.unmodifiableList(passedValidations));
        }
        return result;
    }

    private static ValidationFail performFieldValidations(Context context, FieldInfo fieldInfo, View view) {
        // field validations
        for (ValidationInfo valInfo : fieldInfo.getValidationInfoList()) {
            final Annotation annotation = valInfo.getAnnotation();
           /* if (fieldInfo.condition != null && fieldInfo.condition.validationAnnotation().equals(annotation.annotationType())) {
                boolean evaluation = evaluateCondition(view, fieldInfo.condition);

                if (! evaluation) {
                    // continue to next annotation
                    continue;
                }
            }*/
            final IFieldAdapter adapter = FieldAdapterFactory.getAdapterForField(view, annotation);
            if (adapter == null) {
              //  throw new NoFieldAdapterException(view, annotation);
            }

            final Object value = adapter.getFieldValue(annotation, view);
            final boolean isValid = valInfo.getValidator().validate(annotation, value);

            if (! isValid) {
                final String message = valInfo.getValidator().getMessage(context, annotation, value);
                final int order = valInfo.getValidator().getOrder(annotation);

                // no more validations on this field
                return new ValidationFail(view, message, order);
            }
        }
        return null;
    }
}
