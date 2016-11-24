package com.zj.txvalidator.inter;

import android.view.View;

import com.zj.txvalidator.ValidationFail;

import java.util.List;

/**
 * Created by ASUS on 2016/11/23.
 */

public interface ITxValidateCallback {
    void validateComplete(boolean result, List<ValidationFail> failedValidations, List<View> passedValidations);
}
