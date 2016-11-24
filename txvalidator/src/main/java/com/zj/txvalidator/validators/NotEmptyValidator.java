package com.zj.txvalidator.validators;

import android.content.Context;
import android.text.TextUtils;

import com.zj.txvalidator.annotations.NotEmpty;
import com.zj.txvalidator.annotations.ValidatorFor;
import com.zj.txvalidator.inter.IValidator;

import java.lang.annotation.Annotation;

/**
 * Created by ASUS on 2016/11/23.
 */
@ValidatorFor(NotEmpty.class)
public class NotEmptyValidator implements IValidator<CharSequence>{
    @Override
    public boolean validate(Annotation annotation, CharSequence input) {
        if(((NotEmpty)annotation).trim()){
            return !TextUtils.isEmpty(input.toString().trim());
        }
        return false;
    }

    @Override
    public String getMessage(Context context, Annotation annotation, CharSequence input) {
        final int messageId = ((NotEmpty)annotation).messageId();
        String message = null;
        if(messageId>0){
            message = context.getString(messageId);
        }
        return message;
    }

    @Override
    public int getOrder(Annotation annotation) {
        return ((NotEmpty)annotation).order();
    }
}
