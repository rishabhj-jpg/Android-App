package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etName, etPhone;
    private Button btnLogin, btnRegister;
    private CleverTapAPI cleverTapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cleverTapAPI = CleverTapAPI.getDefaultInstance(this);

        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Sample credentials for demo
        etEmail.setText("fan@example.com");
        etName.setText("City Fan");
        etPhone.setText("+1234567890");

        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (email.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please enter email and name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set user profile on CleverTap
        HashMap<String, Object> profileUpdate = new HashMap<>();
        profileUpdate.put("Email", email);
        profileUpdate.put("Name", name);
        profileUpdate.put("Phone", phone);
        profileUpdate.put("Identity", email); // Use email as unique identifier
        profileUpdate.put("Favorite Team", "Manchester City");
        profileUpdate.put("User Type", "Premium Fan"); // Example property

        if (cleverTapAPI != null) {
            cleverTapAPI.onUserLogin(profileUpdate);

            // Record login event with properties
            Map<String, Object> loginProps = new HashMap<>();
            loginProps.put("Login Method", "Email");
            loginProps.put("App Version", "1.0");
            cleverTapAPI.pushEvent("User Logged In", loginProps);

            Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show();

            // Navigate to main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void handleRegister() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (email.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please enter email and name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set user profile for registration
        HashMap<String, Object> profileUpdate = new HashMap<>();
        profileUpdate.put("Email", email);
        profileUpdate.put("Name", name);
        profileUpdate.put("Phone", phone);
        profileUpdate.put("Identity", email);
        profileUpdate.put("Favorite Team", "Manchester City");
        profileUpdate.put("User Type", "New Fan");
        profileUpdate.put("Sign Up Date", new java.util.Date());

        if (cleverTapAPI != null) {
            cleverTapAPI.onUserLogin(profileUpdate);

            // Record registration event
            Map<String, Object> registerProps = new HashMap<>();
            registerProps.put("Registration Method", "Email");
            registerProps.put("Team Selected", "Manchester City");
            cleverTapAPI.pushEvent("User Registered", registerProps);

            Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();

            // Navigate to main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}