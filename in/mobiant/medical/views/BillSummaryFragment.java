package in.mobiant.medical.views;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.db.BillDBHelper;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.BillInfo;
import in.mobiant.medical.models.BillMedicineItem;
import in.mobiant.medical.models.BillResponse;
import in.mobiant.medical.models.MedicalInfo;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.models.PatientInfo;
import in.mobiant.medical.models.UserInfoItem;
import in.mobiant.medical.net.ApiCallInterface;
import in.mobiant.medical.utility.ShowDialogs;
import in.mobiant.medical.utility.Utils;
import in.mobiant.medical.utility.Variables;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillSummaryFragment extends Fragment {
    private BillDBHelper billDBHelper;
    private BillInfo billInfo;
    private List<BillMedicineItem> billMedicineItems;
    private ImageView buttonDateChange;
    private ImageView buttonDone;
    private ImageView buttonHome;
    private ImageView buttonPrint;
    private ImageView buttonShare;
    private List<MedicinesItem> cartMedicinesList;
    private String curBillImagepath = "";
    private UserInfoItem loginUserInfoItem;
    private MediDBHelper mediDBHelper;
    private MedicalInfo medicalInfo;
    OnClickListener onClickListener = new C03601();
    private ProgressBar progressBar3;
    Callback<BillResponse> saveCallback = new C05522();
    private TextView textViewTotal;
    private WebView webViewBillSummary;

    class C03601 implements OnClickListener {

        class C05512 extends TypeToken<BillInfo> {
            C05512() {
            }
        }

        C03601() {
        }

        public void onClick(View view) {
            if (view.getId() == BillSummaryFragment.this.buttonPrint.getId()) {
                BillSummaryFragment.this.createWebPrintJob(BillSummaryFragment.this.webViewBillSummary);
            } else if (view.getId() == BillSummaryFragment.this.buttonShare.getId()) {
                String path = BillSummaryFragment.this.getBillAsImage();
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/png");
                intent.putExtra("android.intent.extra.STREAM", Uri.parse(path));
                BillSummaryFragment.this.startActivity(Intent.createChooser(intent, "Share Bill"));
            } else if (view.getId() == BillSummaryFragment.this.buttonDateChange.getId()) {
                final Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(BillSummaryFragment.this.getContext(), new OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        BillSummaryFragment.this.billInfo.setDate(calendar.getTimeInMillis());
                        BillSummaryFragment.this.webViewBillSummary.loadData(BillSummaryFragment.this.createBillTemplate().toString(), "text/html", "UTF-8");
                    }
                }, calendar.get(1), calendar.get(2), calendar.get(5)).show();
            } else if (view.getId() == BillSummaryFragment.this.buttonDone.getId()) {
                try {
                    BillSummaryFragment.this.progressBar3.setVisibility(0);
                    FileInputStream inputStream = new FileInputStream(new File(BillSummaryFragment.this.getContext().getFilesDir(), Variables.CUR_USER_OBJECT_FILE));
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    BillSummaryFragment.this.loginUserInfoItem = (UserInfoItem) objectInputStream.readObject();
                    objectInputStream.close();
                    inputStream.close();
                    Gson gson = new Gson();
                    Type type = new C05512().getType();
                    Call<BillResponse> call = ((ApiCallInterface) new Builder().baseUrl(Variables.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiCallInterface.class)).addBillRecord(BillSummaryFragment.this.loginUserInfoItem.getUsername(), BillSummaryFragment.this.loginUserInfoItem.getMobile(), gson.toJson(BillSummaryFragment.this.billInfo, type), BillSummaryFragment.this.loginUserInfoItem.getRole(), Utils.getDeviceID(BillSummaryFragment.this.getContext()));
                    Log.d(Variables.TAG, "info : " + gson.toJson(BillSummaryFragment.this.billInfo, type));
                    call.enqueue(BillSummaryFragment.this.saveCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (view.getId() == BillSummaryFragment.this.buttonHome.getId()) {
                FragmentManager fm = BillSummaryFragment.this.getFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    Log.d(Variables.TAG, fm.getBackStackEntryAt(i).getName());
                    String tag = fm.getBackStackEntryAt(i).getName();
                    fm.popBackStack();
                    fm.beginTransaction().remove(fm.findFragmentByTag(tag)).commit();
                }
            }
        }
    }

    class C05522 implements Callback<BillResponse> {
        C05522() {
        }

        public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
            BillSummaryFragment.this.progressBar3.setVisibility(8);
            Log.d(Variables.TAG, "req : " + response.toString());
            BillResponse billResponse = (BillResponse) response.body();
            if (billResponse.getResultCode().intValue() == Variables.RESULT_BILLADDED) {
                BillSummaryFragment.this.billInfo.setBillNo(billResponse.getBillNo());
                ShowDialogs.showAlertDialog(BillSummaryFragment.this.getContext(), billResponse.getMsg() + " \n Bill no : " + billResponse.getBillNo());
                BillSummaryFragment.this.webViewBillSummary.loadData(BillSummaryFragment.this.createBillTemplate().toString(), "text/html", "UTF-8");
                BillSummaryFragment.this.buttonDone.setVisibility(8);
                BillSummaryFragment.this.billDBHelper.addBillRecord(BillSummaryFragment.this.billInfo);
                BillSummaryFragment.this.mediDBHelper.updateStockAfterBill(billResponse.getMediStocks());
                return;
            }
            ShowDialogs.showAlertDialog(BillSummaryFragment.this.getContext(), billResponse.getMsg());
        }

        public void onFailure(Call<BillResponse> call, Throwable t) {
            BillSummaryFragment.this.progressBar3.setVisibility(8);
            Log.d(Variables.TAG, "req : " + call.request().url());
            ShowDialogs.showAlertDialog(BillSummaryFragment.this.getContext(), t.getMessage());
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cartMedicinesList = getArguments().getParcelableArrayList(Variables.PARAM_MEDICINE_LIST);
        PatientInfo patientInfo = (PatientInfo) getArguments().getParcelable(Variables.PARAM_PATIENT_INFO);
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Bill Summary");
        this.medicalInfo = new MedicalInfo();
        this.billInfo = new BillInfo();
        this.billInfo.setPatientInfo(patientInfo);
        this.billInfo.setDate(Calendar.getInstance().getTimeInMillis());
        this.billMedicineItems = new ArrayList();
        this.billDBHelper = new BillDBHelper(getContext());
        this.mediDBHelper = new MediDBHelper(getContext());
    }

    public void onResume() {
        super.onResume();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Bill Summary");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_bill_summary, container, false);
        setHasOptionsMenu(true);
        this.webViewBillSummary = (WebView) view.findViewById(C0341R.id.webViewBillSummary);
        this.textViewTotal = (TextView) view.findViewById(C0341R.id.textViewTotal);
        this.buttonShare = (ImageView) view.findViewById(C0341R.id.buttonShare);
        this.buttonPrint = (ImageView) view.findViewById(C0341R.id.buttonPrint);
        this.buttonDone = (ImageView) view.findViewById(C0341R.id.buttonDone);
        this.buttonDateChange = (ImageView) view.findViewById(C0341R.id.buttonDateChange);
        this.buttonHome = (ImageView) view.findViewById(C0341R.id.buttonHome);
        this.progressBar3 = (ProgressBar) view.findViewById(C0341R.id.progressBar3);
        this.buttonPrint.setOnClickListener(this.onClickListener);
        this.buttonShare.setOnClickListener(this.onClickListener);
        this.buttonDateChange.setOnClickListener(this.onClickListener);
        this.buttonDone.setOnClickListener(this.onClickListener);
        this.buttonHome.setOnClickListener(this.onClickListener);
        this.webViewBillSummary.loadData(createBillTemplate().toString(), "text/html", "UTF-8");
        return view;
    }

    public StringBuffer createBillTemplate() {
        StringBuffer htmlContent = new StringBuffer();
        htmlContent.append("<!DOCTYPE html>\n<html>\n<head><style type=\"text/css\"></style></head><body>\n\n<h2 style=\"text-align:center\">Receipt</h2><table style=\"width:100%\">\n<tr><td style=\"width:40%;vertical-align:top\"><p style=\"text-align:left\">Bill no : " + this.billInfo.getBillNo() + "<br>Date : " + this.billInfo.getDate() + "<br>GSTIN : " + this.medicalInfo.getGstin() + "</p></td><td style=\"width:60%;vertical-align:top\"><p style=\"text-align:left\"><b>" + this.medicalInfo.getName() + "</b><br>" + this.medicalInfo.getAddress() + "</b><br>Ph." + this.medicalInfo.getPhone() + "</b></p></td></tr></table><hr><table style=\"width:100%;border\">\n<tr>\n\t<td><b>Patient Details:</b><br>Name : " + this.billInfo.getPatientInfo().getFullName() + "<br>Age : " + this.billInfo.getPatientInfo().getAge() + "<br>Gender:" + this.billInfo.getPatientInfo().getGender() + "<br>Phone : " + this.billInfo.getPatientInfo().getMobile() + "<br>Email : " + this.billInfo.getPatientInfo().getEmail() + "</td>\n<td valign=\" top \"><b>Doctor Details:</b><br>Name : " + this.billInfo.getPatientInfo().getDoctor() + "<br>Reg.no : " + this.billInfo.getPatientInfo().getDoctorRegNo() + "<br>Address : " + this.billInfo.getPatientInfo().getDocAddress() + "</td>\n</tr>\n</table>    \n\n<hr><h5>Bill Summary</h5><table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">\n  <tr >\n    <th style=\"border: 1px solid black;padding:3px;\">Name</th>\n    <th style=\"border: 1px solid black;padding:3px;\">Qty</th> \n    <th style=\"border: 1px solid black;padding:3px;\">Price</th>\n  </tr>\n");
        this.billMedicineItems.clear();
        float total = 0.0f;
        for (MedicinesItem medicinesItem : this.cartMedicinesList) {
            float price = ((float) medicinesItem.getSelectedQty()) * medicinesItem.getEachPriceWithTax();
            price = Float.parseFloat(String.format("%.2f", new Object[]{Float.valueOf(price)}));
            BillMedicineItem billItem = new BillMedicineItem();
            billItem.setId(medicinesItem.getId());
            billItem.setExpiry(medicinesItem.getExpiry());
            billItem.setQuantity(medicinesItem.getSelectedQty());
            billItem.setTotal(price);
            billItem.setType(medicinesItem.getDrugType());
            this.billMedicineItems.add(billItem);
            htmlContent.append("  <tr>\n    <td style=\"text-align:center;border: 1px solid black;padding:0px;\">" + billItem.getId() + " (" + billItem.getType() + ")</td>\n    <td style=\"text-align:center;border: 1px solid black;padding:0px;\">" + billItem.getQuantity() + "</td>\n    <td style=\"text-align:center;border: 1px solid black;padding:0px;\"> ₹ " + billItem.getTotal() + "</td>\n  </tr>\n");
            total += price;
        }
        this.billInfo.setBillMedicinesList(this.billMedicineItems);
        this.billInfo.setAmount(total);
        htmlContent.append("<tr>\n  \t<th style=\"border: 1px solid black;padding:0px;\" colspan=\"2\">Total</th>\n  \t<th style=\"border: 1px solid black;padding:0px;\"> ₹ " + this.billInfo.getAmount() + "</th>\n  </tr></table>\n\n</body>\n</html>\n");
        this.textViewTotal.setText(String.format("Total : %s %s", new Object[]{getString(C0341R.string.rupees), Float.valueOf(this.billInfo.getAmount())}));
        return htmlContent;
    }

    private void createWebPrintJob(WebView webView) {
        PrintJob printJob = ((PrintManager) getActivity().getSystemService("print")).print(getString(C0341R.string.app_name) + " Bill/Invoice", webView.createPrintDocumentAdapter(), new PrintAttributes.Builder().setMediaSize(MediaSize.ISO_A5).build());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    public String getBillAsImage() {
        this.webViewBillSummary.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        this.webViewBillSummary.layout(0, 0, this.webViewBillSummary.getMeasuredWidth(), this.webViewBillSummary.getMeasuredHeight());
        this.webViewBillSummary.setDrawingCacheEnabled(true);
        this.webViewBillSummary.buildDrawingCache();
        Bitmap bm = Bitmap.createBitmap(this.webViewBillSummary.getMeasuredWidth(), this.webViewBillSummary.getMeasuredHeight(), Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bm);
        bigcanvas.drawBitmap(bm, 0.0f, (float) bm.getHeight(), new Paint());
        this.webViewBillSummary.draw(bigcanvas);
        Log.d(Variables.TAG, "1111111111111111111111=" + bigcanvas.getWidth());
        Log.d(Variables.TAG, "22222222222222222222222=" + bigcanvas.getHeight());
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString(), "/currentBill.png");
            OutputStream fOut = new FileOutputStream(file);
            bm.compress(CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            bm.recycle();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void onStop() {
        super.onStop();
        if (this.curBillImagepath.equals("")) {
            File file = new File(this.curBillImagepath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
