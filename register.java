
package com.example.abhinay.loginapp;

import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

import java.util.HashMap;
        import java.util.Map;

public class register extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://172.20.180.95/uploading/volleylogin.php";

    public static final String KEY_USERNAME = "user";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "emailid";
    public static final String KEY_AGE = "age";
    public static final String KEY_GEN = "gender";
    public static final String KEY_CPASSWORD = "confirmpassword";
    public static final String KEY_PRODUCT = "product";

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextGEN;
    private EditText editTextAGE;
    private EditText editTextProduct;
    private Button buttonRegister;

//    private Button buttonlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextAGE= (EditText) findViewById(R.id.editTextAge);
        editTextGEN= (EditText) findViewById(R.id.editTextGEN);
        editTextProduct= (EditText) findViewById(R.id.editTextProduct);
        buttonRegister = (Button) findViewById(R.id.buttonregister);


        buttonRegister.setOnClickListener(this);
    }

    private void registerUser(){
        final String user = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String confirmpassword = editTextConfirmPassword.getText().toString().trim();
        final String emailid = editTextEmail.getText().toString().trim();
        final String age = editTextAGE.getText().toString().trim();
        final String gender = editTextGEN.getText().toString().trim();
        final String product = editTextProduct.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(register.this,response,Toast.LENGTH_LONG).show();

                        if(response.trim().equals("successfully registered")) {
                            openLogin();
                        }

                        else {
                            buttonRegister.setEnabled(true);
                        }

                    }

                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(register.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,user);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_CPASSWORD,confirmpassword);
                params.put(KEY_EMAIL, emailid);
                params.put(KEY_AGE,age);
                params.put(KEY_GEN,gender);
                params.put(KEY_PRODUCT,product);
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

   private void openLogin(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        buttonRegister.setEnabled(false);
           if(validate())
              registerUser();
        buttonRegister.setEnabled(true);

    }
    public boolean validate() {
        boolean valid = true;

        String name = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String cpassword = editTextConfirmPassword.getText().toString();
        String gen=editTextGEN.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            editTextUsername.setError("at least 3 characters");
            valid = false;
        } else {
            editTextUsername.setError(null);
        }
        if (cpassword.isEmpty() || !cpassword.equals(password)) {
            editTextConfirmPassword.setError("Password do not match");
            valid = false;
        } else {
            editTextConfirmPassword.setError(null);
        }
        if (gen.isEmpty() || (!gen.equals("Male") && !gen.equals("Female") && !gen.equals("male") && !gen.equals("female"))) {
            editTextGEN.setError("Enter male or female");
            valid = false;
        } else {
            editTextGEN.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("enter a valid email address");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            editTextPassword.setError("between 4 and 15 alphanumeric characters");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }
}

