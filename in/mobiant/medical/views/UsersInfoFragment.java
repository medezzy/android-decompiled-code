package in.mobiant.medical.views;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.models.SubUserInfoResponse;
import in.mobiant.medical.models.SubUsersArr;
import in.mobiant.medical.models.UserInfoItem;
import in.mobiant.medical.net.ApiCallInterface;
import in.mobiant.medical.utility.Utils;
import in.mobiant.medical.utility.Variables;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersInfoFragment extends Fragment {
    Button buttonAddUser;
    Button buttonUpdate;
    EditText editTextAddressCity;
    EditText editTextAddressCountry;
    EditText editTextAddressLine1;
    EditText editTextAddressLine2;
    EditText editTextAddressPin;
    EditText editTextAddressState;
    EditText editTextDrugLicenceNum;
    EditText editTextEmail;
    EditText editTextFirstName;
    EditText editTextGSTIN;
    EditText editTextLastName;
    EditText editTextMedicalName;
    EditText editTextMob;
    TextInputEditText etSubPassword;
    TextInputEditText etSubUsername;
    boolean isAddUser = false;
    LinearLayout layoutSubUsers;
    UserInfoItem loginUserInfoItem;
    OnClickListener onClickListener = new C03711();
    ProgressBar progressBar2;
    TextInputLayout textInputLayout1;
    TextInputLayout textInputLayout2;
    TextInputEditText tietSubscription;
    TextInputEditText tietUsername;
    Callback<SubUserInfoResponse> userInfoItemCallback = new C05572();

    class C03711 implements OnClickListener {
        C03711() {
        }

        public void onClick(View view) {
            if (view.getId() == C0341R.id.buttonAddUser) {
                UsersInfoFragment.this.isAddUser = true;
                UsersInfoFragment.this.visibleAddSubUserUI(0);
            } else if (view.getId() == C0341R.id.buttonUpdate) {
                UsersInfoFragment.this.progressBar2.setVisibility(0);
                UsersInfoFragment.this.updateUsers();
                UsersInfoFragment.this.isAddUser = false;
            }
        }
    }

    class C05572 implements Callback<SubUserInfoResponse> {

        class C03721 implements DialogInterface.OnClickListener {
            C03721() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }

        C05572() {
        }

        public void onResponse(Call<SubUserInfoResponse> call, Response<SubUserInfoResponse> response) {
            UsersInfoFragment.this.progressBar2.setVisibility(8);
            try {
                SubUserInfoResponse res = (SubUserInfoResponse) response.body();
                if (res.getResultCode().intValue() == Variables.RESULT_USERADDED) {
                    UsersInfoFragment.this.loginUserInfoItem.setSubUsersArr(res.getSubUsersArr());
                    Utils.saveData(UsersInfoFragment.this.getContext(), UsersInfoFragment.this.loginUserInfoItem);
                    UsersInfoFragment.this.refreshSubUsersUI();
                }
                new Builder(UsersInfoFragment.this.getContext()).setMessage(res.getMsg()).setPositiveButton((CharSequence) "OK", new C03721()).create().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onFailure(Call<SubUserInfoResponse> call, Throwable t) {
            Log.d(Variables.TAG, " at onFailure " + call.request().url().toString());
            UsersInfoFragment.this.progressBar2.setVisibility(8);
            t.printStackTrace();
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "User Info");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_users_info, container, false);
        setHasOptionsMenu(true);
        view.findViewById(C0341R.id.buttonVerify).setVisibility(8);
        this.tietUsername = (TextInputEditText) view.findViewById(C0341R.id.tietUsername);
        this.tietSubscription = (TextInputEditText) view.findViewById(C0341R.id.tietSubscription);
        this.etSubUsername = (TextInputEditText) view.findViewById(C0341R.id.etSubUsername);
        this.etSubPassword = (TextInputEditText) view.findViewById(C0341R.id.etSubPassword);
        this.editTextFirstName = (EditText) view.findViewById(C0341R.id.editTextFirstName);
        this.editTextLastName = (EditText) view.findViewById(C0341R.id.editTextLastName);
        this.editTextMedicalName = (EditText) view.findViewById(C0341R.id.editTextMedicalName);
        this.editTextAddressLine1 = (EditText) view.findViewById(C0341R.id.editTextAddressLine1);
        this.editTextAddressLine2 = (EditText) view.findViewById(C0341R.id.editTextAddressLine2);
        this.editTextAddressCity = (EditText) view.findViewById(C0341R.id.editTextAddressCity);
        this.editTextAddressPin = (EditText) view.findViewById(C0341R.id.editTextAddressPin);
        this.editTextAddressState = (EditText) view.findViewById(C0341R.id.editTextAddressState);
        this.editTextAddressCountry = (EditText) view.findViewById(C0341R.id.editTextAddressCountry);
        this.editTextGSTIN = (EditText) view.findViewById(C0341R.id.editTextGSTIN);
        this.editTextDrugLicenceNum = (EditText) view.findViewById(C0341R.id.editTextDrugLicenceNum);
        this.editTextMob = (EditText) view.findViewById(C0341R.id.editTextMob);
        this.editTextEmail = (EditText) view.findViewById(C0341R.id.editTextEmail);
        this.textInputLayout1 = (TextInputLayout) view.findViewById(C0341R.id.textInputLayout1);
        this.textInputLayout2 = (TextInputLayout) view.findViewById(C0341R.id.textInputLayout2);
        this.buttonAddUser = (Button) view.findViewById(C0341R.id.buttonAddUser);
        this.buttonUpdate = (Button) view.findViewById(C0341R.id.buttonUpdate);
        this.layoutSubUsers = (LinearLayout) view.findViewById(C0341R.id.layoutSubUsers);
        this.progressBar2 = (ProgressBar) view.findViewById(C0341R.id.progressBar2);
        this.buttonAddUser.setOnClickListener(this.onClickListener);
        this.buttonUpdate.setOnClickListener(this.onClickListener);
        readUserInfo();
        return view;
    }

    private void readUserInfo() {
        Exception e;
        try {
            FileInputStream inputStream = new FileInputStream(new File(getContext().getFilesDir(), Variables.CUR_USER_OBJECT_FILE));
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            this.loginUserInfoItem = (UserInfoItem) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            this.tietUsername.setText(this.loginUserInfoItem.getUsername());
            this.editTextFirstName.setText(this.loginUserInfoItem.getOwner().getFirst());
            this.editTextLastName.setText(this.loginUserInfoItem.getOwner().getLast());
            this.editTextMedicalName.setText(this.loginUserInfoItem.getMedical_name());
            this.editTextAddressLine1.setText(this.loginUserInfoItem.getAddress().getLine1());
            this.editTextAddressLine2.setText(this.loginUserInfoItem.getAddress().getLine2());
            this.editTextAddressCity.setText(this.loginUserInfoItem.getAddress().getCity());
            this.editTextAddressPin.setText(this.loginUserInfoItem.getAddress().getPin());
            this.editTextAddressState.setText(this.loginUserInfoItem.getAddress().getState());
            this.editTextAddressCountry.setText(this.loginUserInfoItem.getAddress().getCountry());
            this.editTextGSTIN.setText(this.loginUserInfoItem.getGstin());
            this.editTextDrugLicenceNum.setText(this.loginUserInfoItem.getDrugLicence());
            this.editTextMob.setText(this.loginUserInfoItem.getMobile());
            this.editTextEmail.setText(this.loginUserInfoItem.getEmail());
            long time = this.loginUserInfoItem.getExpiry().longValue();
            Log.d(Variables.TAG, "time : " + time);
            this.tietSubscription.setText(DateFormat.getDateInstance(1).format(new Date(time)));
            if (this.loginUserInfoItem.getRole().equals(Variables.ROLE_SUB_USER)) {
                this.buttonAddUser.setVisibility(8);
            }
            refreshSubUsersUI();
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
        } catch (ClassNotFoundException e3) {
            e = e3;
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    public void visibleAddSubUserUI(int visibility) {
        this.textInputLayout1.setVisibility(visibility);
        this.textInputLayout2.setVisibility(visibility);
        this.buttonUpdate.setVisibility(visibility);
        this.etSubUsername.setText("");
        this.etSubPassword.setText("");
    }

    public void updateUsers() {
        try {
            JSONObject obj;
            ApiCallInterface apiCallInterface = (ApiCallInterface) new Retrofit.Builder().baseUrl(Variables.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiCallInterface.class);
            List<SubUsersArr> list = this.loginUserInfoItem.getSubUsersArr();
            JSONArray jsArray = new JSONArray();
            for (SubUsersArr item : list) {
                obj = new JSONObject();
                obj.put(Variables.USERNAME, item.getUsername());
                obj.put(Variables.PASSWORD, item.getPassword());
                obj.put(Variables.DEVICE_ID, item.getDeviceId());
                jsArray.put(obj);
            }
            if (this.isAddUser) {
                if (this.etSubUsername.length() <= 0 || this.etSubPassword.length() <= 0) {
                    this.etSubPassword.setError("Please input valid details.");
                    return;
                }
                obj = new JSONObject();
                obj.put(Variables.USERNAME, this.etSubUsername.getText().toString());
                obj.put(Variables.PASSWORD, this.etSubPassword.getText().toString());
                obj.put(Variables.DEVICE_ID, "");
                jsArray.put(obj);
            }
            Log.d(Variables.TAG, " jsArray : " + jsArray.toString());
            apiCallInterface.addSubUser(this.loginUserInfoItem.getUsername(), this.loginUserInfoItem.getMobile(), this.etSubUsername.getText().toString(), this.etSubPassword.getText().toString(), jsArray.toString()).enqueue(this.userInfoItemCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        visibleAddSubUserUI(8);
    }

    private void refreshSubUsersUI() {
        this.layoutSubUsers.removeAllViews();
        this.layoutSubUsers.setVisibility(0);
        TextView textView = new TextView(getContext());
        textView.setText(getString(C0341R.string.sub_users));
        textView.setTypeface(Typeface.defaultFromStyle(1));
        this.layoutSubUsers.addView(textView);
        for (final SubUsersArr item : this.loginUserInfoItem.getSubUsersArr()) {
            CardView cardView = new CardView(getContext());
            LayoutParams layoutParams = new LayoutParams(-1, -2);
            layoutParams.setMargins(5, 5, 5, 5);
            cardView.setLayoutParams(layoutParams);
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams1.setMargins(15, 5, 5, 5);
            relativeLayout.setLayoutParams(layoutParams1);
            TextView tv = new TextView(getContext());
            tv.setText(new StringBuffer().append("\n").append(getString(C0341R.string.username_sub_user)).append(File.pathSeparatorChar).append(item.getUsername()).append("\n \n").append(getString(C0341R.string.password_sub_user)).append(File.pathSeparatorChar).append(item.getPassword()).append("\n").toString());
            relativeLayout.addView(tv);
            if (this.loginUserInfoItem.getRole().equals(Variables.ROLE_ADMIN)) {
                ImageButton imageButton = new ImageButton(getContext());
                imageButton.setImageResource(17301564);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.addRule(21);
                imageButton.setLayoutParams(layoutParams2);
                relativeLayout.addView(imageButton);
                imageButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        UsersInfoFragment.this.loginUserInfoItem.getSubUsersArr().remove(item);
                        UsersInfoFragment.this.refreshSubUsersUI();
                        UsersInfoFragment.this.buttonUpdate.setVisibility(0);
                    }
                });
            }
            cardView.addView(relativeLayout);
            this.layoutSubUsers.addView(cardView);
        }
    }
}
