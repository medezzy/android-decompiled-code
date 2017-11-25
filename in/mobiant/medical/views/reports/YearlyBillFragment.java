package in.mobiant.medical.views.reports;

import android.os.Bundle;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.adapters.PatientNamesAdapter;
import in.mobiant.medical.adapters.YearlyBillAdapter;
import in.mobiant.medical.db.BillDBHelper;
import in.mobiant.medical.interfaces.OnYearlyBillDataListener;
import in.mobiant.medical.models.BillInfo;
import in.mobiant.medical.utility.Variables;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class YearlyBillFragment extends Fragment {
    AutoCompleteTextView autoTextSearch;
    BillDBHelper billDBHelper;
    List<BillInfo> billInfoList;
    Button buttonPrint;
    TextView noItems1;
    OnClickListener onClickListener = new C03761();
    OnItemClickListener onItemClickListener = new C03772();
    OnYearlyBillDataListener onYearlyBillDataListener = new C05584();
    PatientNamesAdapter patientNamesAdapter;
    TextWatcher textWatcher = new C03783();
    YearlyBillAdapter yearlyBillAdapter;

    class C03761 implements OnClickListener {
        C03761() {
        }

        public void onClick(View view) {
            if (view.getId() == YearlyBillFragment.this.buttonPrint.getId()) {
                final WebView webView = new WebView(YearlyBillFragment.this.getContext());
                webView.loadData(YearlyBillFragment.this.createPrintTemplate().toString(), "text/html", "UTF-8");
                webView.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        YearlyBillFragment.this.createWebPrintJob(webView);
                    }
                });
            }
        }
    }

    class C03772 implements OnItemClickListener {
        C03772() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d(Variables.TAG, "patientNamesAdapter.count : " + YearlyBillFragment.this.patientNamesAdapter.getCount() + " pos :" + position);
            BillInfo billInfo = YearlyBillFragment.this.patientNamesAdapter.getItem(position);
            Log.v(Variables.TAG, "Seletected : " + billInfo.getPatientInfo().getFullName());
            YearlyBillFragment.this.autoTextSearch.setText("");
            YearlyBillFragment.this.billInfoList.clear();
            YearlyBillFragment.this.patientNamesAdapter.notifyDataSetChanged();
            YearlyBillFragment.this.billDBHelper.getYearlyBill(false, billInfo.getPatientInfo().getFullName(), YearlyBillFragment.this.onYearlyBillDataListener);
        }
    }

    class C03783 implements TextWatcher {
        C03783() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            Log.d(Variables.TAG, " start :  before : " + before + "  count : " + count);
            if (charSequence.length() >= 2 && !YearlyBillFragment.this.autoTextSearch.isPerformingCompletion()) {
                YearlyBillFragment.this.billDBHelper.getYearlyBill(true, charSequence.toString(), YearlyBillFragment.this.onYearlyBillDataListener);
            }
        }

        public void afterTextChanged(Editable editable) {
            Log.d(Variables.TAG, "Aftertext changed : " + editable.toString());
        }
    }

    class C05584 implements OnYearlyBillDataListener {
        C05584() {
        }

        public void onDataFetched(List<BillInfo> items) {
            if (items.size() <= 0) {
                YearlyBillFragment.this.noItems1.setVisibility(0);
            } else {
                YearlyBillFragment.this.billInfoList.clear();
                YearlyBillFragment.this.billInfoList.addAll(items);
            }
            YearlyBillFragment.this.yearlyBillAdapter.notifyDataSetChanged();
        }

        public void onNameChanged(List<BillInfo> items) {
            if (YearlyBillFragment.this.patientNamesAdapter == null) {
                YearlyBillFragment.this.patientNamesAdapter = new PatientNamesAdapter(YearlyBillFragment.this.getContext(), 17367057, YearlyBillFragment.this.billInfoList);
                YearlyBillFragment.this.autoTextSearch.setThreshold(2);
                YearlyBillFragment.this.autoTextSearch.setAdapter(YearlyBillFragment.this.patientNamesAdapter);
                YearlyBillFragment.this.autoTextSearch.setTextColor(YearlyBillFragment.this.getContext().getResources().getColor(C0341R.color.colorPrimaryDark));
            } else if (items.size() > 0) {
                YearlyBillFragment.this.billInfoList.clear();
                YearlyBillFragment.this.billInfoList.addAll(items);
                YearlyBillFragment.this.patientNamesAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.billDBHelper = new BillDBHelper(getContext());
        this.billInfoList = new ArrayList();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_yearly_bill, container, false);
        this.autoTextSearch = (AutoCompleteTextView) view.findViewById(C0341R.id.autoTextSearch);
        this.autoTextSearch.addTextChangedListener(this.textWatcher);
        this.autoTextSearch.setOnItemClickListener(this.onItemClickListener);
        RecyclerView yearlyBillRecyclerView = (RecyclerView) view.findViewById(C0341R.id.yearlyBillRecyclerView);
        this.noItems1 = (TextView) view.findViewById(C0341R.id.noItems1);
        this.yearlyBillAdapter = new YearlyBillAdapter(getContext(), this.billInfoList);
        yearlyBillRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        yearlyBillRecyclerView.setAdapter(this.yearlyBillAdapter);
        this.buttonPrint = (Button) view.findViewById(C0341R.id.buttonPrint);
        this.buttonPrint.setOnClickListener(this.onClickListener);
        return view;
    }

    private StringBuffer createPrintTemplate() {
        StringBuffer htmlContent = new StringBuffer();
        htmlContent.append("<!DOCTYPE html>\n<html>\n<head><style type=\"text/css\"></style></head><body>\n<table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">\n  <tr >\n    <th style=\"border: 1px solid black;padding:3px;\">List of Bills</small></th>\n  </tr>\n");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        for (BillInfo item : this.billInfoList) {
            calendar.setTimeInMillis(item.getDate());
            htmlContent.append("  <tr >\n    <td style=\"border: 1px solid black;padding:3px;\"><small><b> Bill no :" + item.getBillNo() + "</b> | Date : " + dateFormat.format(calendar.getTime()) + "  | Amount  : " + getString(C0341R.string.rupees) + item.getAmount() + "</small></td>\n</tr>\n");
        }
        htmlContent.append("</table>");
        htmlContent.append("</body>\n</html>\n");
        return htmlContent;
    }

    private void createWebPrintJob(WebView webView) {
        PrintJob printJob = ((PrintManager) getActivity().getSystemService("print")).print(getString(C0341R.string.app_name) + " Yearly Bill", webView.createPrintDocumentAdapter(), new Builder().setMediaSize(MediaSize.ISO_A5).build());
    }
}
