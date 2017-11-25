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
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.db.BillDBHelper;
import in.mobiant.medical.views.reports.DWMReportFragment;

public class ReportsFragment extends Fragment {
    BillDBHelper billDBHelper;
    Button buttonDailyReport;
    Button buttonMonthlyReport;
    Button buttonWeeklyReport;
    Button buttonYearlyBill;
    DWMReportFragment dwmReportFragment = null;
    OnClickListener onClickListener = new C03681();
    TextView textViewCashCollection;

    class C03681 implements OnClickListener {
        C03681() {
        }

        public void onClick(View view) {
            if (view.getId() != ReportsFragment.this.buttonYearlyBill.getId()) {
                Bundle bundle;
                if (view.getId() == ReportsFragment.this.buttonDailyReport.getId()) {
                    ReportsFragment.this.dwmReportFragment = new DWMReportFragment();
                    bundle = new Bundle();
                    bundle.putString("title", "Daily");
                    ReportsFragment.this.dwmReportFragment.setArguments(bundle);
                    ReportsFragment.this.goToFragment(ReportsFragment.this.dwmReportFragment, "DWMReportFragment");
                } else if (view.getId() == ReportsFragment.this.buttonWeeklyReport.getId()) {
                    ReportsFragment.this.dwmReportFragment = new DWMReportFragment();
                    bundle = new Bundle();
                    bundle.putString("title", "Weekly");
                    ReportsFragment.this.dwmReportFragment.setArguments(bundle);
                    ReportsFragment.this.goToFragment(ReportsFragment.this.dwmReportFragment, "DWMReportFragment");
                } else if (view.getId() == ReportsFragment.this.buttonMonthlyReport.getId()) {
                    ReportsFragment.this.dwmReportFragment = new DWMReportFragment();
                    bundle = new Bundle();
                    bundle.putString("title", "Monthly");
                    ReportsFragment.this.dwmReportFragment.setArguments(bundle);
                    ReportsFragment.this.goToFragment(ReportsFragment.this.dwmReportFragment, "DWMReportFragment");
                }
            }
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Reports");
        this.billDBHelper = new BillDBHelper(getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_reports, container, false);
        setHasOptionsMenu(true);
        this.buttonDailyReport = (Button) view.findViewById(C0341R.id.buttonDailyReport);
        this.buttonYearlyBill = (Button) view.findViewById(C0341R.id.buttonYearlyBill);
        this.buttonWeeklyReport = (Button) view.findViewById(C0341R.id.buttonWeeklyReport);
        this.buttonMonthlyReport = (Button) view.findViewById(C0341R.id.buttonMonthlyReport);
        this.buttonDailyReport.setOnClickListener(this.onClickListener);
        this.buttonYearlyBill.setOnClickListener(this.onClickListener);
        this.buttonWeeklyReport.setOnClickListener(this.onClickListener);
        this.buttonMonthlyReport.setOnClickListener(this.onClickListener);
        this.textViewCashCollection = (TextView) view.findViewById(C0341R.id.textViewCashCollection);
        this.textViewCashCollection.setText(String.format("%s %s %s", new Object[]{getString(C0341R.string.today_s_cash_collection), getString(C0341R.string.rupees), Float.valueOf(this.billDBHelper.getDailyCash())}));
        return view;
    }

    private void goToFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(C0341R.anim.slide_in_right, C0341R.anim.slide_out_left, C0341R.anim.slide_in_left, C0341R.anim.slide_out_right);
        fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public void onResume() {
        super.onResume();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Reports");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
