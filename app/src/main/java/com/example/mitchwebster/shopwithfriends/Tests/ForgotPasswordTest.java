package com.example.mitchwebster.shopwithfriends.Tests;

/**
 * Created by Mitch Webster on 3/27/2015.
 * File for all server connections
 * Specific details for mongo server connection
 */
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.mitchwebster.shopwithfriends.Activities.Forgot_Password;
import com.example.mitchwebster.shopwithfriends.Activities.Login;
import com.example.mitchwebster.shopwithfriends.R;

import junit.framework.Assert;

public class ForgotPasswordTest extends ActivityInstrumentationTestCase2<Login> {

    public ForgotPasswordTest() {
        super(Login.class);
    }

    public void testFakeEmail() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Forgot_Password.class.getName(), null, false);
                Login myLogin = getActivity();
                final Button forgotPassButton = (Button) myLogin.findViewById(R.id.fp);
                EditText email = (EditText) myLogin.findViewById(R.id.emailLogin);
                email.setText("F@KEUSER");
                Assert.assertEquals("F@KEUSER",email.getText().toString());
                forgotPassButton.performClick();
                Forgot_Password nextActivity = (Forgot_Password) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
                Assert.assertNull(nextActivity);
            }
        });
    }

    public void testValidEmail() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Forgot_Password.class.getName(), null, false);
                Login myLogin = getActivity();
                final Button forgotPassButton = (Button) myLogin.findViewById(R.id.fp);
                EditText email = (EditText) myLogin.findViewById(R.id.emailLogin);
                email.setText("m@gmail.com");
                Assert.assertEquals("m@gmail.com",email.getText().toString());
                forgotPassButton.performClick();
                Forgot_Password nextActivity = (Forgot_Password) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
                Assert.assertNotNull(nextActivity);
                nextActivity.finish();
            }
        });
    }

    public void testEmptyEmail() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Forgot_Password.class.getName(), null, false);
                Login myLogin = getActivity();
                Assert.assertEquals(true,((EditText) myLogin.findViewById(R.id.emailLogin)).getText().toString().isEmpty());
                final Button forgotPassButton = (Button) myLogin.findViewById(R.id.fp);
                forgotPassButton.performClick();
                Forgot_Password nextActivity = (Forgot_Password) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
                Assert.assertNull(nextActivity);
            }
        });
    }

}
