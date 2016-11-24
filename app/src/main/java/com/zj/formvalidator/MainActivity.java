package com.zj.formvalidator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zj.txvalidator.TxValidator;
import com.zj.txvalidator.ValidationFail;
import com.zj.txvalidator.annotations.NotEmpty;
import com.zj.txvalidator.callback.SimpleToastCallback;
import com.zj.txvalidator.inter.ITxValidateCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @NotEmpty(messageId = R.string.notempty,trim = true)
    EditText editText;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TxValidator.validate(MainActivity.this, MainActivity.this,new SimpleToastCallback(MainActivity.this));
            }
        });
    }
}
