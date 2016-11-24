package com.zj.txvalidator;

import java.util.List;

/**
 * Created by ASUS on 2016/11/23.
 */

public class FieldInfo {

    private final List<ValidationInfo> validationInfoList;

    public FieldInfo(List<ValidationInfo> validationInfoList) {
        this.validationInfoList = validationInfoList;
    }

    public List<ValidationInfo> getValidationInfoList() {
        return validationInfoList;
    }
}
