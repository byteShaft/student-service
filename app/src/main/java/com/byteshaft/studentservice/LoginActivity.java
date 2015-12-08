package com.byteshaft.studentservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private String username;
    private String password;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextUsername = (EditText) findViewById(R.id.et_username_login);
        editTextPassword = (EditText) findViewById(R.id.et_password_login);
        buttonLogin = (Button) findViewById(R.id.btn_login);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        forgetPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        buttonLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement authentication here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.forget_password:
                Intent intent = new Intent(AppGlobals.getContext(), ForgetPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;

        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();

        if (username.trim().isEmpty() || username.length() < 4) {
            editTextUsername.setError("at least 4 characters");
            valid = false;
        } else {
            editTextUsername.setError(null);
        }

        if (password.trim().isEmpty() || password.length() < 4) {
            editTextPassword.setError("at least 4 characters");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {
        AppGlobals.setVirgin(false);
        AppGlobals.putUsername(username);
        buttonLogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        buttonLogin.setEnabled(true);
    }
}
