package in.mobiant.medical.views.reports;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.adapters.DWMReportAdapter;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.views.SetActionBar;
import java.util.ArrayList;
import java.util.List;

public class DWMReportFragment extends Fragment {
    int DWM = 0;
    OnCheckedChangeListener checkedChangeListener = new C03741();
    DWMReportAdapter dwmReportAdapter;
    private MediDBHelper mediDBHelper;
    TextView noItems1;
    RadioButton radioButtonBUY;
    RadioButton radioButtonSELL;
    List<MedicinesItem> reportsMedicines;
    private String title = "";

    class C03741 implements OnCheckedChangeListener {
        C03741() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (compoundButton.getId() == DWMReportFragment.this.radioButtonBUY.getId() && isChecked) {
                DWMReportFragment.this.setDWM(false);
                DWMReportFragment.this.dwmReportAdapter.setReportType(DWMReportFragment.this.DWM);
                DWMReportFragment.this.setNoItemVisible();
            } else if (compoundButton.getId() == DWMReportFragment.this.radioButtonSELL.getId() && isChecked) {
                DWMReportFragment.this.setDWM(true);
                DWMReportFragment.this.dwmReportAdapter.setReportType(DWMReportFragment.this.DWM);
                DWMReportFragment.this.setNoItemVisible();
            }
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getArguments().getString("title", "");
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, this.title + " Reports");
        this.mediDBHelper = new MediDBHelper(getContext());
        this.reportsMedicines = new ArrayList();
        setDWM(true);
    }

    public void setDWM(boolean isSell) {
        if (this.title.equals("Daily")) {
            this.DWM = isSell ? 1 : 4;
        } else if (this.title.equals("Weekly")) {
            this.DWM = isSell ? 2 : 5;
        } else if (this.title.equals("Monthly")) {
            this.DWM = isSell ? 3 : 6;
        }
        this.reportsMedicines.clear();
        this.reportsMedicines.addAll(this.mediDBHelper.getBuySell(this.DWM));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_dwm_report, container, false);
        setHasOptionsMenu(true);
        this.radioButtonSELL = (RadioButton) view.findViewById(C0341R.id.radioButtonSELL);
        this.radioButtonBUY = (RadioButton) view.findViewById(C0341R.id.radioButtonBUY);
        RecyclerView reportRecyclerView = (RecyclerView) view.findViewById(C0341R.id.reportRecyclerView);
        this.noItems1 = (TextView) view.findViewById(C0341R.id.noItems1);
        if (this.reportsMedicines.size() <= 0) {
            this.noItems1.setVisibility(0);
        }
        this.dwmReportAdapter = new DWMReportAdapter(getContext(), this.reportsMedicines, this.DWM);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        reportRecyclerView.setAdapter(this.dwmReportAdapter);
        this.radioButtonBUY.setOnCheckedChangeListener(this.checkedChangeListener);
        this.radioButtonSELL.setOnCheckedChangeListener(this.checkedChangeListener);
        return view;
    }

    public void setNoItemVisible() {
        if (this.reportsMedicines.size() <= 0) {
            this.noItems1.setVisibility(0);
        } else {
            this.noItems1.setVisibility(8);
        }
        this.dwmReportAdapter.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, this.title + " Reports");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
