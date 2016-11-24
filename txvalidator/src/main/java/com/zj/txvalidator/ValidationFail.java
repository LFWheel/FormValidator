package com.zj.txvalidator;

import android.view.View;

/**
 * Created by ASUS on 2016/11/23.
 */

public class ValidationFail {
    public final View view;
    public final String message;
    final int order;

    public ValidationFail(View view, String message, int order) {
        this.view = view;
        this.message = message;
        this.order = order;
    }

    public View getView() {
        return view;
    }

    public String getMessage() {
        return message;
    }

    public int getOrder() {
        return order;
    }
}
