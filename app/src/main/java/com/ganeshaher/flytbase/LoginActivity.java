package com.ganeshaher.flytbase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import static com.ganeshaher.flytbase.AppConstant.MYPREFERENCES;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText username, password;
    SharedPreferences sharedPreferences;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (TextInputEditText) findViewById(R.id.userField);
        password = (TextInputEditText) findViewById(R.id.passField);
        login=findViewById(R.id.loginButton);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        String user=sharedPreferences.getString(AppConstant.NAME,"");

        if (!user.isEmpty())
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().isEmpty())
                {
                    username.setError("Enter Username");
                }
                else  if (password.getText().toString().isEmpty())
                {
                    password.setError("Enter password");
                }
                else
                {
                    if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin"))
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(AppConstant.NAME, username.getText().toString());
                        editor.putString(AppConstant.PASSWORD, password.getText().toString());
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });


    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle(getString(R.string.exit_app))
                .setMessage(getString(R.string.msg31))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}