package Signing_Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.evineon.MainActivity;
import com.example.android.evineon.R;
import com.example.android.evineon.Splashscreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail,inputPassword, inputfname,inputlname,inputLoc,inputRePassword,inputphone,inputclass,inputorg;
    private FirebaseAuth auth;
    private Button btnSignUp, btnLogin;
    private RadioGroup rgusertype,rggender;
    private RadioButton rbusertypebt,rbgenderbt;
    private ProgressDialog PD;
    private String rbusertype_str,rbgender_str,strphone;

    String year_str,month_str,day_str,dateString;


    private Button mDisplaydate;
    private DatePickerDialog.OnDateSetListener mDate;
    int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //Get FireBase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        inputfname = (EditText) findViewById(R.id.input_firstname);
        inputlname = (EditText) findViewById(R.id.input_lastname);
        mDisplaydate = (Button) findViewById(R.id.input_dob);

        inputLoc = (EditText) findViewById(R.id.input_city);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputRePassword = (EditText)findViewById(R.id.input_re_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        inputphone=(EditText) findViewById(R.id.input_phone);
        inputorg=(EditText) findViewById(R.id.input_org);
        inputclass=(EditText) findViewById(R.id.input_class);

        rggender = (RadioGroup) findViewById(R.id.radio_gender);

        mDisplaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                        R.style.AppTheme_Dark_Dialog, mDate,
                        year, month, day);

                String color_string = "#303030";
                int myColor = Color.parseColor(color_string);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(myColor));
                dialog.show();
            }
        });


        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                dateString = dayOfMonth + "/" + month + "/" + year;
                mDisplaydate.setText(dateString);
            }
        };

        // Validating Credits entered by User with Firebase
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fname = inputfname.getText().toString();
                final String city = inputLoc.getText().toString();
                final String email = inputEmail.getText().toString();
                final String lname = inputlname.getText().toString();
                final String re_pass = inputRePassword.getText().toString();
                final String password = inputPassword.getText().toString();
                final String inputnum = inputphone.getText().toString();
                final String classi = inputclass.getText().toString();
                final String org = inputorg.getText().toString();

                final int rdgenderint = rggender.getCheckedRadioButtonId();
                rbgenderbt = (RadioButton) findViewById(rdgenderint);
                rbgender_str = rbgenderbt.getText().toString();

                if (!fname.equals("") && !lname.equals("") && !city.equals("") && !email.equals("") && !inputnum.equals("")&& !re_pass.equals("")&& !password.equals("") && !dateString.equals("") && !org.equals("") && !classi.equals("")) {


                    if (password.equals(re_pass)) {

                        try {
                            // Email ID must be valid
                            // Password strength is minimum 6 characters by default in firebase registration
                            // Minimum Password length throws Error as 'WEAK PASSWORD'
                            if (password.length() > 0 && email.length() > 0) {
                                PD.show();
                                //authenticate user
                                auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    try {
                                                        throw task.getException();
                                                    } catch (FirebaseAuthWeakPasswordException e) {
                                                        inputPassword.setError(getString(R.string.error_weak_password));
                                                        inputPassword.requestFocus();
                                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                                        inputEmail.setError(getString(R.string.error_invalid_email));
                                                        inputEmail.requestFocus();
                                                    } catch (FirebaseAuthUserCollisionException e) {
                                                        inputEmail.setError(getString(R.string.error_user_exists));
                                                        inputEmail.requestFocus();
                                                    } catch (Exception e) {
                                                        //Log.e(TAG, e.getMessage());
                                                    }
                                                } else {
                                                    new Addtodatabase_register(fname, lname, city, dateString, email, rbgender_str, rbusertype_str = null, inputnum,org,classi);
                                                    Intent intent = new Intent(RegisterActivity.this, Splashscreen.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                PD.dismiss();
                                            }
                                        });
                            } else {
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Fill All Fields",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        inputRePassword.setError("Password does not match");
                        inputRePassword.setText("");
                        inputPassword.setText("");
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                }
            }

        });


        // Link to Login Screen
        /*btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

    }
}
