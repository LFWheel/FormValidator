package com.zj.txvalidator.callback;

import android.content.Context;
import android.view.View;

import com.zj.txvalidator.ValidationFail;
import com.zj.txvalidator.inter.ITxValidateCallback;

import java.util.Collection;
import java.util.List;

/**
 * @author Tomas Vondracek
 */
public abstract class SimpleCallback implements ITxValidateCallback {

	protected final Context mContext;
	protected final boolean mFocusFirstFail;

	public SimpleCallback(Context context, boolean focusFirstFail) {
		mFocusFirstFail = focusFirstFail;
		mContext = context;
	}

    @Override
    public void validateComplete(boolean result, List<ValidationFail> failedValidations, List<View> passedValidations) {
		if (! failedValidations.isEmpty()) {
			ValidationFail firstFail = failedValidations.get(0);
			if (mFocusFirstFail) {
				firstFail.view.requestFocus();
			}
			showValidationMessage(firstFail);
		}

	    if (! passedValidations.isEmpty()) {
			showViewIsValid(passedValidations);
	    }
	}

	/**
	 * present validation message to the user
	 */
	protected abstract void showValidationMessage(ValidationFail firstFail);

	/**
	 * present to user that passed views are valid
	 * @param passedValidations all views that passed the validation
	 */
	protected void showViewIsValid(Collection<View> passedValidations) {
		// nothing
	}
}
