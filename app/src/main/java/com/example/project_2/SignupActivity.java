package com.example.project_2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, ageEditText, addressEditText, usernameEditText, passwordEditText;
    private Spinner genderSpinner;
    private Button submitButton;

    private DBClass databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();

        Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        List<String> options = new ArrayList<>();
        options.add("Select Gender"); // This is the hint
        options.add("Male");
        options.add("Female");
        options.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) { // Hint's position
                    textView.setText(getItem(position));
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setSelection(0); // Set the hint as the default selection
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedGender = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(SignupActivity.this, "Please select a gender.", Toast.LENGTH_LONG).show();
                    }
                });

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String age = ageEditText.getText().toString();
                String gender = genderSpinner.getSelectedItem().toString();

                // Username validation
                if (username.length() < 5) {
                    Toast.makeText(SignupActivity.this, "Username must be at least 5 characters long.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!username.matches("^[a-z0-9]+$")) {
                    Toast.makeText(SignupActivity.this, "Username must be alphanumeric and all lowercase with no spaces.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Password validation
                if (password.length() < 8) {
                    Toast.makeText(SignupActivity.this, "Password must be at least 8 characters long.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Character.isUpperCase(password.charAt(0))) {
                    Toast.makeText(SignupActivity.this, "Password must start with an uppercase letter.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.matches(".*[0-9].*")) {
                    Toast.makeText(SignupActivity.this, "Password must contain at least one number.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.contains(" ")) {
                    Toast.makeText(SignupActivity.this, "Password cannot contain spaces.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(SignupActivity.this, "Username already exists. Try another.", Toast.LENGTH_LONG).show();

                // Hash the password before storing it
                String hashedPassword = hashPassword(password);

                // Save the user information in the database
                databaseHelper.addInfo(name, age, gender, username, hashedPassword);
            }
        });
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.editTextName);
        genderSpinner = findViewById(R.id.genderSpinner);
        ageEditText = findViewById(R.id.editTextAge);
        usernameEditText = findViewById(R.id.editTextUsernameSignup);
        passwordEditText = findViewById(R.id.editTextPasswordSignup);
        submitButton = findViewById(R.id.buttonSubmitSignup);
    }

    public byte[] messageDigest(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(s.getBytes(StandardCharsets.UTF_8));
    }
    public String hashPassword(String password) {
        String hashPwd = "";
        try {
            byte [] hashedPwd = messageDigest(password);
            hashPwd = Base64.encodeToString(hashedPwd, 0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashPwd;
    }


}
