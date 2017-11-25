package in.mobiant.medical.views;

import android.os.Bundle;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.adapters.MediExpiringAdapter;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.utility.Variables;
import java.util.List;

public class StockStatusFragment extends Fragment {
    Button buttonPrint;
    List<MedicinesItem> expiredMedicines;
    RecyclerView expiredRecyclerView;
    List<MedicinesItem> expiringMedicines;
    RecyclerView expiringRecyclerView;
    MediDBHelper mediDBHelper;
    MediExpiringAdapter mediExpiredAdapter;
    MediExpiringAdapter mediExpiringAdapter;
    OnClickListener onClickListener = new C03701();

    class C03701 implements OnClickListener {
        C03701() {
        }

        public void onClick(View view) {
            if (view.getId() == StockStatusFragment.this.buttonPrint.getId()) {
                final WebView webView = new WebView(StockStatusFragment.this.getContext());
                webView.loadData(StockStatusFragment.this.createPrintTemplate().toString(), "text/html", "UTF-8");
                webView.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        StockStatusFragment.this.createWebPrintJob(webView);
                    }
                });
            }
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Stocks");
        this.mediDBHelper = new MediDBHelper(getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_stock_status, container, false);
        setHasOptionsMenu(true);
        this.expiringRecyclerView = (RecyclerView) view.findViewById(C0341R.id.expiringRecyclerView);
        this.expiredRecyclerView = (RecyclerView) view.findViewById(C0341R.id.expiredRecyclerView);
        this.expiringMedicines = this.mediDBHelper.getMedicinesExpiring(false);
        if (this.expiringMedicines.size() <= 0) {
            view.findViewById(C0341R.id.noItems1).setVisibility(0);
        } else {
            this.mediExpiringAdapter = new MediExpiringAdapter(getContext(), this.expiringMedicines);
            this.expiringRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            this.expiringRecyclerView.setAdapter(this.mediExpiringAdapter);
        }
        this.expiredMedicines = this.mediDBHelper.getMedicinesExpiring(true);
        if (this.expiringMedicines.size() <= 0) {
            view.findViewById(C0341R.id.noItems2).setVisibility(0);
        } else {
            this.mediExpiredAdapter = new MediExpiringAdapter(getContext(), this.expiredMedicines);
            this.expiredRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            this.expiredRecyclerView.setAdapter(this.mediExpiredAdapter);
        }
        Log.d(Variables.TAG, "expiring medicines : " + this.expiringMedicines.size());
        this.buttonPrint = (Button) view.findViewById(C0341R.id.buttonPrint);
        this.buttonPrint.setOnClickListener(this.onClickListener);
        return view;
    }

    public void onResume() {
        super.onResume();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Stocks");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    private StringBuffer createPrintTemplate() {
        StringBuffer htmlContent = new StringBuffer();
        htmlContent.append("<!DOCTYPE html>\n<html>\n<head><style type=\"text/css\"></style></head><body>\n<table style=\"width:100%\">\n<tr><td><table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">\n  <tr >\n    <th style=\"border: 1px solid black;padding:3px;\">Expiring<small>(15 Days)</small></th>\n  </tr>\n");
        for (MedicinesItem item : this.expiringMedicines) {
            htmlContent.append("  <tr >\n    <td style=\"border: 1px solid black;padding:3px;\"><small><b>" + item.getId() + "</b>" + String.format("  | %s%s  ", new Object[]{getString(C0341R.string.stockQty), Integer.valueOf(item.getStockQty())}) + String.format("%s%s", new Object[]{getString(C0341R.string.type), item.getDrugType()}) + "</small></td>\n</tr>\n");
        }
        htmlContent.append("</table></td><td>");
        htmlContent.append("<table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">\n  <tr >\n    <th style=\"border: 1px solid black;padding:3px;\">Expired</th>\n  </tr>\n");
        for (MedicinesItem item2 : this.expiredMedicines) {
            htmlContent.append("  <tr >\n    <td style=\"border: 1px solid black;padding:3px;\"><small><b>" + item2.getId() + "</b>" + String.format("  | %s%s  ", new Object[]{getString(C0341R.string.stockQty), Integer.valueOf(item2.getStockQty())}) + String.format("%s%s", new Object[]{getString(C0341R.string.type), item2.getDrugType()}) + "</small></td>\n</tr>\n");
        }
        htmlContent.append("</table></td></tr></table></body>\n</html>\n");
        return htmlContent;
    }

    private void createWebPrintJob(WebView webView) {
        PrintJob printJob = ((PrintManager) getActivity().getSystemService("print")).print(getString(C0341R.string.app_name) + " Bill/Invoice", webView.createPrintDocumentAdapter(), new Builder().setMediaSize(MediaSize.ISO_A5).build());
    }
}
