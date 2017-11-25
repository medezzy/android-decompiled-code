package in.mobiant.medical;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.MedicineItemNew;
import in.mobiant.medical.models.UserInfoItem;
import in.mobiant.medical.net.ApiCallInterface;
import in.mobiant.medical.net.OTPSendInterface;
import in.mobiant.medical.utility.HttpClient;
import in.mobiant.medical.utility.ProgressDialog;
import in.mobiant.medical.utility.TextUtility;
import in.mobiant.medical.utility.Utils;
import in.mobiant.medical.utility.Variables;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MedicalLoginActivity extends AppCompatActivity {
    ApiCallInterface apiCallInterface;
    Button buttonBack;
    Button buttonRegister;
    Button buttonVerify;
    CheckBox checkBoxRemember;
    private EditText editTextAddressCity;
    private EditText editTextAddressCountry;
    private EditText editTextAddressLine1;
    private EditText editTextAddressLine2;
    private EditText editTextAddressPin;
    private EditText editTextAddressState;
    private EditText editTextCode;
    private EditText editTextDrugLicenceNum;
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextGSTIN;
    private EditText editTextLastName;
    private EditText editTextMedicalName;
    private EditText editTextMob;
    private EditText editTextPwd;
    private EditText editTextUsername;
    private boolean isChecking = false;
    private boolean isVerified = false;
    Callback<String> otpCallback = new C05361();
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    RadioButton radioButtonAdmin;
    RadioButton radioButtonSubUser;
    RadioGroup radioGroup1;
    Retrofit retrofit;
    Callback<UserInfoItem> signInCallback = new C05372();
    View signUpLayout;

    class C03403 implements OnClickListener {
        C03403() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class C05361 implements Callback<String> {
        C05361() {
        }

        public void onResponse(Call<String> call, Response<String> response) {
            Log.d(Variables.TAG, "res : " + response.toString());
            if (response.code() == ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                MedicalLoginActivity.this.buttonVerify.setVisibility(0);
            }
        }

        public void onFailure(Call<String> call, Throwable t) {
            MedicalLoginActivity.this.buttonVerify.setText(C0341R.string.send_otp_again);
            MedicalLoginActivity.this.editTextMob.setInputType(2);
            t.printStackTrace();
        }
    }

    class C05372 implements Callback<UserInfoItem> {
        C05372() {
        }

        public void onResponse(Call<UserInfoItem> call, Response<UserInfoItem> response) {
            MedicalLoginActivity.this.isChecking = false;
            MedicalLoginActivity.this.progressBar.setVisibility(8);
            try {
                Log.d(Variables.TAG, "res " + response.raw().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserInfoItem userInfoItem = (UserInfoItem) response.body();
            if (userInfoItem.getResultCode().intValue() == Variables.RESULT_OK) {
                if (MedicalLoginActivity.this.checkBoxRemember.isChecked()) {
                    Utils.saveLogin(MedicalLoginActivity.this.getBaseContext(), true, userInfoItem);
                } else {
                    Utils.saveLogin(MedicalLoginActivity.this.getBaseContext(), false, userInfoItem);
                }
                if (Utils.isMedicinesDownloaded(MedicalLoginActivity.this.getBaseContext())) {
                    MedicalLoginActivity.this.gotoDashboard();
                    return;
                }
                MedicalLoginActivity.this.progressDialog = new ProgressDialog(MedicalLoginActivity.this, MedicalLoginActivity.this.getString(C0341R.string.downloading_msg));
                MedicalLoginActivity.this.progressDialog.setTitle(C0341R.string.app_name);
                MedicalLoginActivity.this.progressDialog.setCancelable(false);
                MedicalLoginActivity.this.progressDialog.show();
                MedicalLoginActivity.this.apiCallInterface.getMedicines(userInfoItem.getUsername(), userInfoItem.getMobile(), userInfoItem.getRole(), Utils.getDeviceID(MedicalLoginActivity.this.getBaseContext()), Integer.valueOf(0), Integer.valueOf(1000)).enqueue(new MedicinesDownloadCallback(MedicalLoginActivity.this.getBaseContext(), MedicalLoginActivity.this.apiCallInterface, userInfoItem, 0));
                return;
            }
            MedicalLoginActivity.this.showDialog(userInfoItem.getMsg());
        }

        public void onFailure(Call<UserInfoItem> call, Throwable t) {
            MedicalLoginActivity.this.isChecking = false;
            MedicalLoginActivity.this.progressBar.setVisibility(8);
            Log.d(Variables.TAG, "req : " + call.request().url());
            t.printStackTrace();
        }
    }

    public class MedicinesDownloadCallback implements Callback<List<MedicineItemNew>> {
        private ApiCallInterface apiCallInterface;
        private Context baseContext;
        final int limit = 1000;
        private MediDBHelper mediDBHelper;
        private int skip = 0;
        private UserInfoItem userInfoItem;

        public void setSkip(int skip) {
            this.skip = skip;
        }

        public MedicinesDownloadCallback(Context baseContext, ApiCallInterface apiCallInterface, UserInfoItem userInfoItem, int skip) {
            this.baseContext = baseContext;
            this.apiCallInterface = apiCallInterface;
            this.userInfoItem = userInfoItem;
            this.skip = skip;
            this.mediDBHelper = new MediDBHelper(baseContext);
        }

        public void onResponse(Call<List<MedicineItemNew>> call, Response<List<MedicineItemNew>> response) {
            Log.d(Variables.TAG, "res " + response.raw().toString());
            this.mediDBHelper.addMedicines(MedicalLoginActivity.this, MedicalLoginActivity.this.progressDialog, (List) response.body(), this.apiCallInterface, this, this.userInfoItem, this.skip, 1000);
        }

        public void onFailure(Call<List<MedicineItemNew>> call, Throwable t) {
            Log.d(Variables.TAG, "req : " + call.request().url());
            t.printStackTrace();
            if (MedicalLoginActivity.this.progressDialog != null) {
                MedicalLoginActivity.this.progressDialog.dismiss();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0341R.layout.activity_medical_login);
        this.retrofit = new Builder().baseUrl(Variables.SERVER_URL).client(HttpClient.getClient()).addConverterFactory(GsonConverterFactory.create()).build();
        this.apiCallInterface = (ApiCallInterface) this.retrofit.create(ApiCallInterface.class);
        this.editTextUsername = (EditText) findViewById(C0341R.id.editTextUsername);
        this.editTextPwd = (EditText) findViewById(C0341R.id.editTextPwd);
        this.editTextMob = (EditText) findViewById(C0341R.id.editTextMob);
        this.editTextCode = (EditText) findViewById(C0341R.id.editTextCode);
        this.editTextEmail = (EditText) findViewById(C0341R.id.editTextEmail);
        this.editTextFirstName = (EditText) findViewById(C0341R.id.editTextFirstName);
        this.editTextLastName = (EditText) findViewById(C0341R.id.editTextLastName);
        this.editTextMedicalName = (EditText) findViewById(C0341R.id.editTextMedicalName);
        this.editTextAddressLine1 = (EditText) findViewById(C0341R.id.editTextAddressLine1);
        this.editTextAddressLine2 = (EditText) findViewById(C0341R.id.editTextAddressLine2);
        this.editTextAddressCity = (EditText) findViewById(C0341R.id.editTextAddressCity);
        this.editTextAddressPin = (EditText) findViewById(C0341R.id.editTextAddressPin);
        this.editTextAddressState = (EditText) findViewById(C0341R.id.editTextAddressState);
        this.editTextAddressCountry = (EditText) findViewById(C0341R.id.editTextAddressCountry);
        this.editTextGSTIN = (EditText) findViewById(C0341R.id.editTextGSTIN);
        this.editTextDrugLicenceNum = (EditText) findViewById(C0341R.id.editTextDrugLicenceNum);
        this.signUpLayout = findViewById(C0341R.id.signUpLayout);
        this.buttonRegister = (Button) findViewById(C0341R.id.buttonRegister);
        this.buttonBack = (Button) findViewById(C0341R.id.buttonBack);
        this.buttonVerify = (Button) findViewById(C0341R.id.buttonVerify);
        this.checkBoxRemember = (CheckBox) findViewById(C0341R.id.checkBoxRemember);
        this.radioButtonAdmin = (RadioButton) findViewById(C0341R.id.radioButtonAdmin);
        this.radioButtonSubUser = (RadioButton) findViewById(C0341R.id.radioButtonSubUser);
        this.radioGroup1 = (RadioGroup) findViewById(C0341R.id.radioGroup1);
        this.editTextPwd.setInputType(129);
        this.progressBar = (ProgressBar) findViewById(C0341R.id.progressBar);
        if (Utils.isLogin(this)) {
            gotoDashboard();
        }
        Log.d(Variables.TAG, "" + Utils.getDeviceID(this));
    }

    public void onButtonClick(View view) {
        if (view.getId() != C0341R.id.buttonSignIn || this.isChecking) {
            if (view.getId() == C0341R.id.buttonSignUp) {
                visibleSignUpLayout(0);
            } else if (view.getId() == C0341R.id.buttonRegister) {
                if (!TextUtility.isValidEmail(this.editTextEmail.getText().toString())) {
                    this.editTextEmail.setError("Not valid email");
                } else if (!TextUtility.isValidMobile(this.editTextMob.getText().toString())) {
                    this.editTextMob.setError("Not valid mobile");
                } else if (!this.isVerified || TextUtility.isEmpty(this.editTextUsername) || TextUtility.isEmpty(this.editTextPwd) || TextUtility.isEmpty(this.editTextFirstName) || TextUtility.isEmpty(this.editTextLastName) || TextUtility.isEmpty(this.editTextMedicalName) || TextUtility.isEmpty(this.editTextAddressLine1) || TextUtility.isEmpty(this.editTextAddressLine2) || TextUtility.isEmpty(this.editTextAddressCity) || TextUtility.isEmpty(this.editTextAddressPin) || TextUtility.isEmpty(this.editTextAddressState) || TextUtility.isEmpty(this.editTextAddressCountry) || TextUtility.isEmpty(this.editTextGSTIN) || TextUtility.isEmpty(this.editTextDrugLicenceNum) || TextUtility.isEmpty(this.editTextMob) || TextUtility.isEmpty(this.editTextEmail)) {
                    this.editTextFirstName.setError("Please fill all details");
                } else {
                    this.progressBar.setVisibility(0);
                    JSONObject jO = new JSONObject();
                    try {
                        jO.put("username", TextUtility.getText(this.editTextUsername));
                        jO.put("password", TextUtility.getText(this.editTextPwd));
                        jO.put("medical_name", TextUtility.getText(this.editTextMedicalName));
                        JSONArray jDArr = new JSONArray();
                        jDArr.put(0, Utils.getDeviceID(this));
                        jO.put("devices_arr", jDArr);
                        jO.put("subUsers_arr", new JSONArray());
                        Calendar calendar = Calendar.getInstance();
                        jO.put("registrationDate", calendar.get(5) + "/" + (calendar.get(2) + 1) + "/" + calendar.get(1));
                        calendar.add(2, 3);
                        jO.put("expiry", calendar.getTimeInMillis());
                        JSONObject jOwner = new JSONObject();
                        jOwner.put("first", TextUtility.getText(this.editTextFirstName));
                        jOwner.put("last", TextUtility.getText(this.editTextLastName));
                        jO.put("Owner", jOwner);
                        JSONObject jAdd = new JSONObject();
                        jAdd.put("Line1", TextUtility.getText(this.editTextAddressLine1));
                        jAdd.put("Line2", TextUtility.getText(this.editTextAddressLine2));
                        jAdd.put("City", TextUtility.getText(this.editTextAddressCity));
                        jAdd.put("Pin", TextUtility.getText(this.editTextAddressPin));
                        jAdd.put("State", TextUtility.getText(this.editTextAddressState));
                        jAdd.put("Country", TextUtility.getText(this.editTextAddressCountry));
                        jO.put("Address", jAdd);
                        jO.put("Gstin", TextUtility.getText(this.editTextGSTIN));
                        jO.put("drugLicence", TextUtility.getText(this.editTextDrugLicenceNum));
                        jO.put("mobile", TextUtility.getText(this.editTextMob));
                        jO.put(NotificationCompat.CATEGORY_EMAIL, TextUtility.getText(this.editTextEmail));
                        jO.put("accType", "retailer");
                        jO.put("lastLogin", 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(Variables.TAG, jO.toString());
                    this.apiCallInterface.signUp(jO.toString()).enqueue(this.signInCallback);
                }
            } else if (view.getId() == C0341R.id.buttonBack) {
                visibleSignUpLayout(8);
            } else if (view.getId() == C0341R.id.buttonVerify) {
                sendOTPAndVerify(view);
            }
        } else if (this.editTextUsername.getText().length() > 0 && this.editTextPwd.getText().length() > 0) {
            this.isChecking = true;
            this.progressBar.setVisibility(0);
            String role = "admin";
            if (this.radioButtonSubUser.isChecked()) {
                role = "sub_user";
            }
            this.apiCallInterface.userCheck(this.editTextUsername.getText().toString(), this.editTextPwd.getText().toString(), Utils.getDeviceID(this), role).enqueue(this.signInCallback);
        } else if (this.editTextUsername.getText().length() > 0) {
            this.editTextUsername.setError("Enter valid username");
        } else if (this.editTextPwd.getText().length() > 0) {
            this.editTextPwd.setError("Enter valid password");
        }
    }

    public void visibleSignUpLayout(int visibility) {
        if (visibility != 0) {
            this.radioGroup1.setVisibility(0);
            this.checkBoxRemember.setVisibility(0);
        } else {
            this.radioGroup1.setVisibility(8);
            this.checkBoxRemember.setVisibility(8);
        }
        this.signUpLayout.setVisibility(visibility);
        this.buttonBack.setVisibility(visibility);
        this.buttonRegister.setVisibility(visibility);
        this.buttonVerify.setVisibility(visibility);
    }

    public void sendOTPAndVerify(View view) {
        if (this.buttonVerify.getText().equals(getString(C0341R.string.send_otp)) || this.buttonVerify.getText().equals(getString(C0341R.string.send_otp_again))) {
            if (TextUtility.isValidMobile(TextUtility.getText(this.editTextMob))) {
                this.buttonVerify.setVisibility(8);
                this.buttonVerify.setText(C0341R.string.verify);
                this.editTextMob.setInputType(0);
                findViewById(C0341R.id.textInputLayout4).setVisibility(0);
                SharedPreferences sharedPreferences = getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0);
                Log.d(Variables.TAG, " last OTP : " + sharedPreferences.getString(Variables.LASTOTP, ""));
                if (sharedPreferences.getString(Variables.LASTOTP, "").equals("")) {
                    int num = new SecureRandom().nextInt(100000);
                    String otp = String.format(Locale.getDefault(), "%05d", new Object[]{Integer.valueOf(num)});
                    sharedPreferences.edit().putString(Variables.LASTOTP, otp).apply();
                    ((OTPSendInterface) new Builder().baseUrl("http://smst.manifestsolution.com/").client(HttpClient.getClient()).addConverterFactory(ScalarsConverterFactory.create()).build().create(OTPSendInterface.class)).sendOTPMessage("DrIndian verification code is : " + otp, TextUtility.getText(this.editTextMob)).enqueue(this.otpCallback);
                    return;
                }
                return;
            }
            TextUtility.setError(this.editTextMob, "Enter valid mobile number");
        } else if (getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0).getString(Variables.LASTOTP, "").equals(TextUtility.getText(this.editTextCode))) {
            this.isVerified = true;
            Toast.makeText(getBaseContext(), "OTP verified", 0).show();
            this.editTextCode.setInputType(0);
            this.buttonVerify.setText("Verified");
        } else {
            this.isVerified = false;
            Toast.makeText(getBaseContext(), "OTP does not match", 0).show();
        }
    }

    private void showDialog(String msg) {
        new AlertDialog.Builder(this).setMessage((CharSequence) msg).setPositiveButton((CharSequence) "OK", new C03403()).create().show();
    }

    public void gotoDashboard() {
        startActivity(new Intent(this, MedicalDashboardActivity.class));
        finish();
    }
}
