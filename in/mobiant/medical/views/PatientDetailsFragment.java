package in.mobiant.medical.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.models.PatientInfo;
import in.mobiant.medical.utility.Variables;

public class PatientDetailsFragment extends Fragment {
    Button buttonProceed;
    EditText editTextAddress;
    EditText editTextAge;
    EditText editTextDocAddress;
    EditText editTextDoctor;
    EditText editTextDoctorRegNo;
    EditText editTextEmail;
    EditText editTextFullName;
    EditText editTextMobile;
    String gender = "";
    OnCheckedChangeListener onCheckedChangeListener = new C03661();
    OnClickListener onClickListener = new C03672();
    RadioGroup radioGroupGender;

    class C03661 implements OnCheckedChangeListener {
        C03661() {
        }

        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            if (checkedId == C0341R.id.radioButtonMale) {
                PatientDetailsFragment.this.gender = "male";
            } else if (checkedId == C0341R.id.radioButtonFemale) {
                PatientDetailsFragment.this.gender = "female";
            } else if (checkedId == C0341R.id.radioButtonTrasg) {
                PatientDetailsFragment.this.gender = "transgender";
            }
        }
    }

    class C03672 implements OnClickListener {
        C03672() {
        }

        public void onClick(View view) {
            if (view.getId() == C0341R.id.buttonProceed) {
                PatientInfo patientInfo = new PatientInfo();
                patientInfo.setFullName(PatientDetailsFragment.this.editTextFullName.getText().toString());
                patientInfo.setAddress(PatientDetailsFragment.this.editTextAddress.getText().toString());
                patientInfo.setAge(PatientDetailsFragment.this.editTextAge.getText().toString());
                patientInfo.setDocAddress(PatientDetailsFragment.this.editTextDocAddress.getText().toString());
                patientInfo.setDoctor(PatientDetailsFragment.this.editTextDoctor.getText().toString());
                patientInfo.setDoctorRegNo(PatientDetailsFragment.this.editTextDoctorRegNo.getText().toString());
                patientInfo.setEmail(PatientDetailsFragment.this.editTextEmail.getText().toString());
                patientInfo.setGender(PatientDetailsFragment.this.gender);
                patientInfo.setMobile(PatientDetailsFragment.this.editTextMobile.getText().toString());
                FragmentTransaction fragmentTransaction = PatientDetailsFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(C0341R.anim.slide_in_right, C0341R.anim.slide_out_left, C0341R.anim.slide_in_left, C0341R.anim.slide_out_right);
                BillSummaryFragment billSummaryFragment = new BillSummaryFragment();
                Bundle bundle = PatientDetailsFragment.this.getArguments();
                bundle.putParcelable(Variables.PARAM_PATIENT_INFO, patientInfo);
                billSummaryFragment.setArguments(bundle);
                fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, billSummaryFragment, "BillSummaryFragment");
                fragmentTransaction.addToBackStack("BillSummaryFragment");
                fragmentTransaction.commit();
            }
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Patient Details");
    }

    public void onResume() {
        super.onResume();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Patient Details");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_patient_details, container, false);
        setHasOptionsMenu(true);
        this.radioGroupGender = (RadioGroup) view.findViewById(C0341R.id.radioGroupGender);
        this.radioGroupGender.setOnCheckedChangeListener(this.onCheckedChangeListener);
        this.editTextFullName = (EditText) view.findViewById(C0341R.id.editTextFullName);
        this.editTextAge = (EditText) view.findViewById(C0341R.id.editTextAge);
        this.editTextMobile = (EditText) view.findViewById(C0341R.id.editTextMobile);
        this.editTextEmail = (EditText) view.findViewById(C0341R.id.editTextEmail);
        this.editTextAddress = (EditText) view.findViewById(C0341R.id.editTextAddress);
        this.editTextDoctor = (EditText) view.findViewById(C0341R.id.editTextDoctor);
        this.editTextDoctorRegNo = (EditText) view.findViewById(C0341R.id.editTextDoctorRegNo);
        this.editTextDocAddress = (EditText) view.findViewById(C0341R.id.editTextDocAddress);
        this.buttonProceed = (Button) view.findViewById(C0341R.id.buttonProceed);
        this.buttonProceed.setOnClickListener(this.onClickListener);
        return view;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
