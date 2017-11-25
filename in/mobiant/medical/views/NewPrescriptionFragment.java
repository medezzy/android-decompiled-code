package in.mobiant.medical.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.adapters.AddMedicineListAdapter;
import in.mobiant.medical.adapters.MediNamesAdapter;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.utility.Variables;
import java.util.ArrayList;
import java.util.List;

public class NewPrescriptionFragment extends Fragment {
    AddMedicineListAdapter addMedicineListAdapter;
    ArrayList<MedicinesItem> allMedicines;
    AutoCompleteTextView autoTextSearch;
    Button buttonProceed;
    ArrayList<MedicinesItem> cartMedicineList;
    MediDBHelper mediDbHelper;
    MediNamesAdapter mediNamesAdapter;
    OnClickListener onClickListener = new C03653();
    OnItemClickListener onItemClickListener = new C03631();
    OnMedicineUpdateListener onMedicineUpdateListener = new C05564();
    RecyclerView rvMedicinesList;
    TextView textViewTotal;
    TextWatcher textWatcher = new C03642();

    class C03631 implements OnItemClickListener {
        C03631() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d(Variables.TAG, "mediNamesAdapter.count : " + NewPrescriptionFragment.this.mediNamesAdapter.getCount() + " pos :" + position);
            MedicinesItem medicinesItem = NewPrescriptionFragment.this.mediNamesAdapter.getItem(position);
            Log.v(Variables.TAG, "Seletected : " + medicinesItem.getId());
            NewPrescriptionFragment.this.addMedicineToCart(medicinesItem);
            NewPrescriptionFragment.this.autoTextSearch.setText("");
            NewPrescriptionFragment.this.allMedicines.clear();
            NewPrescriptionFragment.this.mediNamesAdapter.notifyDataSetChanged();
            NewPrescriptionFragment.this.rvMedicinesList.scrollToPosition(NewPrescriptionFragment.this.cartMedicineList.size() - 1);
        }
    }

    class C03642 implements TextWatcher {
        C03642() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            Log.d(Variables.TAG, " start :  before : " + before + "  count : " + count);
            if (charSequence.length() >= 2 && !NewPrescriptionFragment.this.autoTextSearch.isPerformingCompletion()) {
                NewPrescriptionFragment.this.refreshData(charSequence.toString());
            }
        }

        public void afterTextChanged(Editable editable) {
            Log.d(Variables.TAG, "Aftertext changed : " + editable.toString());
        }
    }

    class C03653 implements OnClickListener {
        C03653() {
        }

        public void onClick(View view) {
            if (view.getId() == C0341R.id.buttonProceed) {
                FragmentTransaction fragmentTransaction = NewPrescriptionFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(C0341R.anim.slide_in_right, C0341R.anim.slide_out_left, C0341R.anim.slide_in_left, C0341R.anim.slide_out_right);
                PatientDetailsFragment patientDetailsFragment = new PatientDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Variables.PARAM_MEDICINE_LIST, NewPrescriptionFragment.this.cartMedicineList);
                patientDetailsFragment.setArguments(bundle);
                fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, patientDetailsFragment, "PatientDetailsFragment");
                fragmentTransaction.addToBackStack("PatientDetailsFragment");
                fragmentTransaction.commit();
            }
        }
    }

    public interface OnMedicineUpdateListener {
        void selectMedicineSub(int i, MedicinesItem medicinesItem);

        void setTotalPrice(float f);

        void showMedicineSub(int i, List<String> list);
    }

    class C05564 implements OnMedicineUpdateListener {
        C05564() {
        }

        @SuppressLint({"DefaultLocale"})
        public void setTotalPrice(float price) {
            Log.d(Variables.TAG, String.format("price %f ", new Object[]{Float.valueOf(price)}));
            TextView textView = NewPrescriptionFragment.this.textViewTotal;
            Object[] objArr = new Object[2];
            objArr[0] = NewPrescriptionFragment.this.getString(C0341R.string.rupees);
            objArr[1] = String.format("%.2f", new Object[]{Float.valueOf(price)});
            textView.setText(String.format("Total : %s%s", objArr));
        }

        public void showMedicineSub(int subForPosition, List<String> genericName) {
            NewPrescriptionFragment.this.showMedicineSub(subForPosition, genericName);
        }

        public void selectMedicineSub(int subForPosition, MedicinesItem item) {
            NewPrescriptionFragment.this.cartMedicineList.set(subForPosition, item);
            NewPrescriptionFragment.this.addMedicineListAdapter.notifyItemChanged(subForPosition);
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mediDbHelper = new MediDBHelper(getContext());
        this.allMedicines = new ArrayList();
        this.cartMedicineList = new ArrayList();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "New Prescription");
    }

    public void onResume() {
        super.onResume();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "New Prescription");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_new_prescription, container, false);
        setHasOptionsMenu(true);
        this.autoTextSearch = (AutoCompleteTextView) view.findViewById(C0341R.id.autoTextSearch);
        this.buttonProceed = (Button) view.findViewById(C0341R.id.buttonProceed);
        this.textViewTotal = (TextView) view.findViewById(C0341R.id.textViewTotal);
        this.rvMedicinesList = (RecyclerView) view.findViewById(C0341R.id.rvMedicinesList);
        this.textViewTotal.setText(String.format("%s 0", new Object[]{getString(C0341R.string.rupees)}));
        this.buttonProceed.setOnClickListener(this.onClickListener);
        this.addMedicineListAdapter = new AddMedicineListAdapter(getContext(), this.cartMedicineList, this.onMedicineUpdateListener);
        this.rvMedicinesList.setLayoutManager(new LinearLayoutManager(getContext(), 1, true));
        this.rvMedicinesList.setAdapter(this.addMedicineListAdapter);
        this.autoTextSearch.addTextChangedListener(this.textWatcher);
        this.autoTextSearch.setOnItemClickListener(this.onItemClickListener);
        getMedicinesData();
        return view;
    }

    private void refreshData(String str) {
        List<MedicinesItem> items = this.mediDbHelper.getAllMedicines(str);
        if (items.size() > 0) {
            this.allMedicines.clear();
            this.allMedicines.addAll(items);
            this.mediNamesAdapter.notifyDataSetChanged();
        }
        Log.d(Variables.TAG, "refreshData");
    }

    private void getMedicinesData() {
        this.mediNamesAdapter = new MediNamesAdapter(getContext(), 17367057, this.allMedicines);
        this.autoTextSearch.setThreshold(2);
        this.autoTextSearch.setAdapter(this.mediNamesAdapter);
        this.autoTextSearch.setTextColor(getContext().getResources().getColor(C0341R.color.colorPrimaryDark));
    }

    private void addMedicineToCart(MedicinesItem medicinesItem) {
        this.cartMedicineList.add(medicinesItem);
        this.addMedicineListAdapter.notifyItemInserted(this.cartMedicineList.size() - 1);
    }

    public void showMedicineSub(int position, List<String> genericName) {
        DialogAddMedicineSub dialogAddMedicineSub = new DialogAddMedicineSub(getContext(), genericName, this.mediDbHelper, position);
        dialogAddMedicineSub.setListener(this.onMedicineUpdateListener);
        dialogAddMedicineSub.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
