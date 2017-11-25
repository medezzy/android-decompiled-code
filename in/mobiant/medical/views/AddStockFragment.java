package in.mobiant.medical.views;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.MedicalDashboardActivity;
import in.mobiant.medical.adapters.MediNamesAdapter;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.models.StockAddResponse;
import in.mobiant.medical.models.StockMedicineInfo;
import in.mobiant.medical.models.StockMedicineInfo.Details;
import in.mobiant.medical.models.UserInfoItem;
import in.mobiant.medical.net.ApiCallInterface;
import in.mobiant.medical.utility.ShowDialogs;
import in.mobiant.medical.utility.TextUtility;
import in.mobiant.medical.utility.Utils;
import in.mobiant.medical.utility.Variables;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddStockFragment extends Fragment {
    ArrayList<MedicinesItem> allMedicines;
    Button buttonAdd;
    ImageButton buttonClose;
    Button buttonScan;
    MedicalDashboardActivity dashboardActivity;
    EditText editTextBatch;
    EditText editTextBill;
    EditText editTextDOM;
    EditText editTextExpiry;
    EditText editTextGeneric;
    EditText editTextPurchasedFrom;
    EditText editTextQty;
    AutoCompleteTextView editTextTrade;
    EditText editTextType;
    ImageView imageViewDOM;
    ImageView imageViewExpiry;
    boolean isScanning = false;
    private CodeScanner mCodeScanner;
    MediDBHelper mediDBHelper;
    MediNamesAdapter mediNamesAdapter;
    OnClickListener onClickListener = new C03584();
    OnItemClickListener onItemClickListener = new C03531();
    ProgressBar progressBar4;
    Callback<StockAddResponse> saveCallback = new C05505();
    CodeScannerView scannerView;
    StockMedicineInfo stockInfo;
    TextWatcher textWatcher = new C03542();

    class C03531 implements OnItemClickListener {
        static final /* synthetic */ boolean $assertionsDisabled = (!AddStockFragment.class.desiredAssertionStatus());

        C03531() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d(Variables.TAG, "mediNamesAdapter.count : " + AddStockFragment.this.mediNamesAdapter.getCount() + " pos :" + position);
            MedicinesItem medicinesItem = AddStockFragment.this.mediNamesAdapter.getItem(position);
            if ($assertionsDisabled || medicinesItem != null) {
                Log.v(Variables.TAG, "Seletected : " + medicinesItem.getId());
                AddStockFragment.this.setData(medicinesItem);
                AddStockFragment.this.isScanning = false;
                return;
            }
            throw new AssertionError();
        }
    }

    class C03542 implements TextWatcher {
        C03542() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() >= 2 && !AddStockFragment.this.editTextTrade.isPerformingCompletion()) {
                AddStockFragment.this.refreshData(charSequence.toString());
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }

    class C03584 implements OnClickListener {

        class C03562 implements OnDateSetListener {
            C03562() {
            }

            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                AddStockFragment.this.editTextExpiry.setText(String.format(Locale.getDefault(), "%d/%d/%d", new Object[]{Integer.valueOf(dayOfMonth), Integer.valueOf(month + 1), Integer.valueOf(year)}));
            }
        }

        class C03573 implements OnDateSetListener {
            C03573() {
            }

            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                AddStockFragment.this.editTextDOM.setText(String.format(Locale.getDefault(), "%d/%d/%d", new Object[]{Integer.valueOf(dayOfMonth), Integer.valueOf(month + 1), Integer.valueOf(year)}));
            }
        }

        class C05491 extends TypeToken<StockMedicineInfo> {
            C05491() {
            }
        }

        C03584() {
        }

        public void onClick(View view) {
            int id = view.getId();
            if (id == C0341R.id.buttonScan) {
                if (AddStockFragment.this.dashboardActivity.checkPermissions()) {
                    AddStockFragment.this.createScanner();
                    AddStockFragment.this.scannerView.setVisibility(0);
                    AddStockFragment.this.buttonClose.setVisibility(0);
                    AddStockFragment.this.isScanning = true;
                }
            } else if (id == C0341R.id.buttonAdd) {
                try {
                    AddStockFragment.this.progressBar4.setVisibility(0);
                    InputStream fileInputStream = new FileInputStream(new File(AddStockFragment.this.getContext().getFilesDir(), Variables.CUR_USER_OBJECT_FILE));
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    UserInfoItem loginUserInfoItem = (UserInfoItem) objectInputStream.readObject();
                    objectInputStream.close();
                    fileInputStream.close();
                    AddStockFragment.this.stockInfo = new StockMedicineInfo();
                    AddStockFragment.this.stockInfo.setId(AddStockFragment.this.editTextTrade.getText().toString());
                    StockMedicineInfo stockMedicineInfo = AddStockFragment.this.stockInfo;
                    stockMedicineInfo.getClass();
                    Details details = new Details();
                    details.setBatch(AddStockFragment.this.editTextBatch.getText().toString());
                    details.setBill(AddStockFragment.this.editTextBill.getText().toString());
                    details.setDom(Long.valueOf(AddStockFragment.this.getDOMInMillis()));
                    details.setExpiry(Long.valueOf(AddStockFragment.this.getExpiryInMillis()));
                    details.setPurchasedFrom(AddStockFragment.this.editTextPurchasedFrom.getText().toString());
                    details.setQuantity(Integer.valueOf(Integer.parseInt(AddStockFragment.this.editTextQty.getText().toString())));
                    AddStockFragment.this.stockInfo.setType(TextUtility.getText(AddStockFragment.this.editTextType));
                    List<Details> list = new ArrayList();
                    list.add(details);
                    AddStockFragment.this.stockInfo.setDetails(list);
                    Gson gson = new Gson();
                    Type type = new C05491().getType();
                    Call<StockAddResponse> call = ((ApiCallInterface) new Builder().baseUrl(Variables.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiCallInterface.class)).addStock(loginUserInfoItem.getUsername(), loginUserInfoItem.getMobile(), gson.toJson(AddStockFragment.this.stockInfo, type), loginUserInfoItem.getRole(), Utils.getDeviceID(AddStockFragment.this.getContext()));
                    Log.d(Variables.TAG, "info : " + gson.toJson(AddStockFragment.this.stockInfo, type));
                    call.enqueue(AddStockFragment.this.saveCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (id == C0341R.id.buttonClose) {
                AddStockFragment.this.scannerView.setVisibility(8);
                AddStockFragment.this.buttonClose.setVisibility(8);
            } else if (id == C0341R.id.imageViewExpiry) {
                calendar = Calendar.getInstance();
                new DatePickerDialog(AddStockFragment.this.getContext(), new C03562(), calendar.get(1), calendar.get(2), calendar.get(5)).show();
            } else if (id == C0341R.id.imageViewDOM) {
                calendar = Calendar.getInstance();
                new DatePickerDialog(AddStockFragment.this.getContext(), new C03573(), calendar.get(1), calendar.get(2), calendar.get(5)).show();
            }
        }
    }

    class C05483 implements DecodeCallback {
        C05483() {
        }

        public void onDecoded(@NonNull final Result result) {
            AddStockFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(AddStockFragment.this.getContext(), result.getText(), 1).show();
                    AddStockFragment.this.scannerView.setVisibility(8);
                    AddStockFragment.this.buttonClose.setVisibility(8);
                    AddStockFragment.this.mCodeScanner.releaseResources();
                    AddStockFragment.this.editTextTrade.setText(C0341R.string.abigel);
                }
            });
        }
    }

    class C05505 implements Callback<StockAddResponse> {
        C05505() {
        }

        public void onResponse(Call<StockAddResponse> call, Response<StockAddResponse> response) {
            AddStockFragment.this.progressBar4.setVisibility(8);
            Log.d(Variables.TAG, "res : " + response.toString());
            StockAddResponse stockAddResponse = (StockAddResponse) response.body();
            if (stockAddResponse.getResultCode().intValue() == Variables.RESULT_STOCKADDED) {
                AddStockFragment.this.stockInfo.setMinExpiry(stockAddResponse.getMinExpiry());
                AddStockFragment.this.stockInfo.setUpdatedStock(stockAddResponse.getUpdatedStock());
                AddStockFragment.this.stockInfo.setDetails(stockAddResponse.getDetails());
                ShowDialogs.showAlertDialog(AddStockFragment.this.getContext(), stockAddResponse.getMsg() + " \n Updated stock : " + stockAddResponse.getUpdatedStock());
                AddStockFragment.this.mediDBHelper.addStock(AddStockFragment.this.stockInfo);
                AddStockFragment.this.resetData();
                return;
            }
            ShowDialogs.showAlertDialog(AddStockFragment.this.getContext(), stockAddResponse.getMsg());
        }

        public void onFailure(Call<StockAddResponse> call, Throwable t) {
            AddStockFragment.this.progressBar4.setVisibility(8);
            Log.d(Variables.TAG, "req : " + call.request().url());
            ShowDialogs.showAlertDialog(AddStockFragment.this.getContext(), t.getMessage());
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dashboardActivity = (MedicalDashboardActivity) getActivity();
        this.mediDBHelper = new MediDBHelper(getContext());
        this.allMedicines = new ArrayList();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Add Stock");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0341R.layout.fragment_add_stock, container, false);
        setHasOptionsMenu(true);
        this.buttonScan = (Button) view.findViewById(C0341R.id.buttonScan);
        this.buttonAdd = (Button) view.findViewById(C0341R.id.buttonAdd);
        this.editTextGeneric = (EditText) view.findViewById(C0341R.id.editTextGeneric);
        this.editTextTrade = (AutoCompleteTextView) view.findViewById(C0341R.id.editTextTrade);
        this.editTextBatch = (EditText) view.findViewById(C0341R.id.editTextBatch);
        this.editTextDOM = (EditText) view.findViewById(C0341R.id.editTextDOM);
        this.editTextExpiry = (EditText) view.findViewById(C0341R.id.editTextExpiry);
        this.editTextQty = (EditText) view.findViewById(C0341R.id.editTextQty);
        this.editTextPurchasedFrom = (EditText) view.findViewById(C0341R.id.editTextPurchasedFrom);
        this.editTextBill = (EditText) view.findViewById(C0341R.id.editTextBill);
        this.editTextType = (EditText) view.findViewById(C0341R.id.editTextType);
        this.buttonClose = (ImageButton) view.findViewById(C0341R.id.buttonClose);
        this.progressBar4 = (ProgressBar) view.findViewById(C0341R.id.progressBar4);
        this.buttonScan.setOnClickListener(this.onClickListener);
        this.buttonAdd.setOnClickListener(this.onClickListener);
        this.buttonClose.setOnClickListener(this.onClickListener);
        this.editTextTrade.addTextChangedListener(this.textWatcher);
        this.editTextTrade.setOnItemClickListener(this.onItemClickListener);
        this.editTextTrade.setThreshold(2);
        this.mediNamesAdapter = new MediNamesAdapter(getContext(), 17367057, this.allMedicines);
        this.editTextTrade.setAdapter(this.mediNamesAdapter);
        this.scannerView = (CodeScannerView) view.findViewById(C0341R.id.scanner_view);
        this.imageViewDOM = (ImageView) view.findViewById(C0341R.id.imageViewDOM);
        this.imageViewExpiry = (ImageView) view.findViewById(C0341R.id.imageViewExpiry);
        this.imageViewDOM.setOnClickListener(this.onClickListener);
        this.imageViewExpiry.setOnClickListener(this.onClickListener);
        return view;
    }

    private void createScanner() {
        this.mCodeScanner = CodeScanner.builder().autoFocus(true).flash(false).onDecoded(new C05483()).build(getContext(), this.scannerView);
        this.mCodeScanner.startPreview();
    }

    public void onResume() {
        super.onResume();
        if (this.mCodeScanner != null) {
            this.mCodeScanner.startPreview();
        }
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Add Stock");
    }

    public void onPause() {
        super.onPause();
        if (this.mCodeScanner != null) {
            this.mCodeScanner.releaseResources();
        }
    }

    private long getDOMInMillis() {
        try {
            Date d = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this.editTextDOM.getText().toString());
            Log.d(Variables.TAG, "Date : " + d.toString() + " : " + d.getTime());
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long getExpiryInMillis() {
        try {
            Date d = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this.editTextExpiry.getText().toString());
            Log.d(Variables.TAG, "Date : " + d.toString() + " : " + d.getTime());
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void refreshData(String str) {
        List<MedicinesItem> items = this.mediDBHelper.getAllMedicines(str);
        if (items.size() > 0) {
            this.allMedicines.clear();
            this.allMedicines.addAll(items);
            this.mediNamesAdapter.notifyDataSetChanged();
        }
        Log.d(Variables.TAG, "refreshData");
        if (this.isScanning) {
            this.editTextTrade.showDropDown();
        }
    }

    private void setData(MedicinesItem item) {
        this.editTextTrade.setText(item.getId());
        this.editTextGeneric.setText(Arrays.toString(item.getReferencedGenericMedicine().toArray()));
        this.editTextType.setText(item.getDrugType());
    }

    private void resetData() {
        TextUtility.setText(this.editTextGeneric, "");
        TextUtility.setText(this.editTextBatch, "");
        TextUtility.setText(this.editTextType, "");
        TextUtility.setText(this.editTextDOM, "");
        TextUtility.setText(this.editTextExpiry, "");
        TextUtility.setText(this.editTextQty, "");
        TextUtility.setText(this.editTextPurchasedFrom, "");
        TextUtility.setText(this.editTextBill, "");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
