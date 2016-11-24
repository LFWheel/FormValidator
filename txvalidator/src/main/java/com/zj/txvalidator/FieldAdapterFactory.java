package com.zj.txvalidator;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.zj.txvalidator.adapters.TextViewAdapter;
import com.zj.txvalidator.inter.IFieldAdapter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created adapters for views with validation.
 *
 * @author Tomas Vondracek
 */
public class FieldAdapterFactory {

	private static TextViewAdapter sTextViewAdapter;


	private static Map<Class<? extends View>, IFieldAdapter<? extends View,?>> sExternalAdapters;

	static void registerAdapter(Class<? extends View> viewType, Class<? extends IFieldAdapter<? extends View,?>> adapterClazz) throws IllegalAccessException, InstantiationException {
		if (sExternalAdapters == null) {
			sExternalAdapters = new HashMap<>();
		}
		sExternalAdapters.put(viewType, adapterClazz.newInstance());
	}


	public static IFieldAdapter getAdapterForField(View view) {
		return getAdapterForField(view, null);
	}

	public static IFieldAdapter<? extends View,?> getAdapterForField(View view, Annotation annotation) {
		final IFieldAdapter<? extends View,?> adapter;
		if (sExternalAdapters != null && sExternalAdapters.containsKey(view.getClass())) {
			adapter = sExternalAdapters.get(view.getClass());
        } else if (view instanceof TextView) {
			if (sTextViewAdapter == null) {
				sTextViewAdapter = new TextViewAdapter();
			}
			adapter = sTextViewAdapter;
		} else {
			adapter = null;
		}
		return adapter;
	}

	static void clear() {
		if (sExternalAdapters != null) {
			sExternalAdapters.clear();
		}
		sTextViewAdapter = null;
	}
}
