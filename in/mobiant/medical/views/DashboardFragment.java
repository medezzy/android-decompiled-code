package in.mobiant.medical.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.zxing.Result;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.MedicalDashboardActivity;
import in.mobiant.medical.utility.Utils;
import in.mobiant.medical.utility.Variables;

public class DashboardFragment extends Fragment {
    Button buttonAddStock;
    Button buttonAlerts;
    ImageButton buttonClose;
    Button buttonNewPresc;
    Button buttonReports;
    Button buttonSearch;
    Button buttonStockStat;
    MedicalDashboardActivity dashboardActivity;
    EditText editTextPrescID;
    ImageButton imageScan;
    private CodeScanner mCodeScanner;
    OnClickListener onClickListener = new C03623();
    CodeScannerView scannerView;

    class C03623 implements OnClickListener {
        C03623() {
        }

        public void onClick(View v) {
            int id = v.getId();
            if (id == C0341R.id.imageScan) {
                if (DashboardFragment.this.dashboardActivity.checkPermissions()) {
                    DashboardFragment.this.createScanner();
                    DashboardFragment.this.scannerView.setVisibility(0);
                    DashboardFragment.this.buttonClose.setVisibility(0);
                }
            } else if (id == C0341R.id.buttonClose) {
                DashboardFragment.this.scannerView.setVisibility(8);
                DashboardFragment.this.buttonClose.setVisibility(8);
            } else {
                FragmentTransaction fragmentTransaction = DashboardFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(C0341R.anim.slide_in_right, C0341R.anim.slide_out_left, C0341R.anim.slide_in_left, C0341R.anim.slide_out_right);
                if (id != C0341R.id.buttonSearch) {
                    if (id == C0341R.id.buttonNewPresc) {
                        fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, new NewPrescriptionFragment(), "NewPrescriptionFragment");
                        fragmentTransaction.addToBackStack("NewPrescriptionFragment");
                    } else if (id == C0341R.id.buttonStockStat) {
                        fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, new StockStatusFragment(), "StockStatusFragment");
                        fragmentTransaction.addToBackStack("StockStatusFragment");
                    } else if (id == C0341R.id.buttonAddStock) {
                        fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, new AddStockFragment(), "AddStockFragment");
                        fragmentTransaction.addToBackStack("AddStockFragment");
                    } else if (id == C0341R.id.buttonReports) {
                        fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, new ReportsFragment(), "ReportsFragment");
                        fragmentTransaction.addToBackStack("ReportsFragment");
                    } else if (id == C0341R.id.buttonAlerts) {
                        fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, new AlertsFragment(), "AlertsFragment");
                        fragmentTransaction.addToBackStack("AlertsFragment");
                    }
                }
                fragmentTransaction.commit();
            }
        }
    }

    class C05531 implements ErrorCallback {
        C05531() {
        }

        public void onError(@NonNull Exception error) {
            error.printStackTrace();
        }
    }

    class C05542 implements DecodeCallback {
        C05542() {
        }

        public void onDecoded(@NonNull final Result result) {
            Log.d(Variables.TAG, "result : " + result.getText());
            DashboardFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(DashboardFragment.this.getContext(), result.getText(), 1).show();
                    DashboardFragment.this.scannerView.setVisibility(8);
                    DashboardFragment.this.buttonClose.setVisibility(8);
                    DashboardFragment.this.mCodeScanner.releaseResources();
                    DashboardFragment.this.editTextPrescID.setText(result.getText());
                }
            });
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dashboardActivity = (MedicalDashboardActivity) getActivity();
        SetActionBar.setup(getActivity(), C0341R.mipmap.ic_launcher, "Dashboard");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_dashboard, container, false);
        setHasOptionsMenu(true);
        this.imageScan = (ImageButton) view.findViewById(C0341R.id.imageScan);
        this.buttonSearch = (Button) view.findViewById(C0341R.id.buttonSearch);
        this.buttonNewPresc = (Button) view.findViewById(C0341R.id.buttonNewPresc);
        this.buttonStockStat = (Button) view.findViewById(C0341R.id.buttonStockStat);
        this.buttonAddStock = (Button) view.findViewById(C0341R.id.buttonAddStock);
        this.buttonReports = (Button) view.findViewById(C0341R.id.buttonReports);
        this.buttonAlerts = (Button) view.findViewById(C0341R.id.buttonAlerts);
        this.editTextPrescID = (EditText) view.findViewById(C0341R.id.editTextPrescID);
        this.buttonClose = (ImageButton) view.findViewById(C0341R.id.buttonClose);
        this.imageScan.setOnClickListener(this.onClickListener);
        this.buttonSearch.setOnClickListener(this.onClickListener);
        this.buttonNewPresc.setOnClickListener(this.onClickListener);
        this.buttonStockStat.setOnClickListener(this.onClickListener);
        this.buttonAddStock.setOnClickListener(this.onClickListener);
        this.buttonReports.setOnClickListener(this.onClickListener);
        this.buttonAlerts.setOnClickListener(this.onClickListener);
        this.buttonClose.setOnClickListener(this.onClickListener);
        this.scannerView = (CodeScannerView) view.findViewById(C0341R.id.scanner_view);
        this.dashboardActivity.checkPermissions();
        return view;
    }

    private void createScanner() {
        this.mCodeScanner = CodeScanner.builder().autoFocus(true).flash(false).onDecoded(new C05542()).onError(new C05531()).build(getContext(), this.scannerView);
        this.mCodeScanner.startPreview();
    }

    public void onResume() {
        super.onResume();
        if (this.dashboardActivity.checkPermissions() && this.mCodeScanner != null) {
            this.mCodeScanner.startPreview();
        }
        SetActionBar.setup(getActivity(), C0341R.mipmap.ic_launcher, "Dashboard");
        System.gc();
        Utils.resetReports(getContext());
    }

    public void onPause() {
        super.onPause();
        if (this.dashboardActivity.checkPermissions() && this.mCodeScanner != null) {
            this.mCodeScanner.releaseResources();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(C0341R.anim.slide_in_right, C0341R.anim.slide_out_left, C0341R.anim.slide_in_left, C0341R.anim.slide_out_right);
        fragmentTransaction.replace(C0341R.id.dashboard_frame_layout, new UsersInfoFragment(), "UsersInfoFragment");
        fragmentTransaction.addToBackStack("UsersInfoFragment");
        fragmentTransaction.commit();
        return true;
    }
}
