package in.mobiant.medical.views;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.adapters.MediSubsAdapter;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.utility.Variables;
import in.mobiant.medical.views.NewPrescriptionFragment.OnMedicineUpdateListener;
import java.util.ArrayList;
import java.util.List;

public class DialogAddMedicineSub extends Dialog {
    Context context;
    List<String> genericName;
    OnMedicineUpdateListener listener;
    MediDBHelper mediDBHelper;
    MediSubsAdapter mediSubsAdapter;
    List<MedicinesItem> medicineSubsItems;
    OnMediSubClickListener onMediSubClickListener = new C05551();
    private RecyclerView recyclerSubMeds;
    int subForPosition;

    private class GetSubsTask extends AsyncTask {
        private GetSubsTask() {
        }

        protected Object doInBackground(Object[] objects) {
            for (String s : DialogAddMedicineSub.this.genericName) {
                List<MedicinesItem> items = DialogAddMedicineSub.this.mediDBHelper.getMedicinesSubs(s);
                Log.d(Variables.TAG, "size : " + items.size());
                if (items.size() > 0) {
                    DialogAddMedicineSub.this.medicineSubsItems.addAll(items);
                }
            }
            publishProgress(new Object[0]);
            return null;
        }

        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            DialogAddMedicineSub.this.mediSubsAdapter.notifyItemRangeInserted(0, DialogAddMedicineSub.this.medicineSubsItems.size());
        }
    }

    public interface OnMediSubClickListener {
        void onSubstituteClick(int i);
    }

    class C05551 implements OnMediSubClickListener {
        C05551() {
        }

        public void onSubstituteClick(int pos) {
            DialogAddMedicineSub.this.listener.selectMedicineSub(DialogAddMedicineSub.this.subForPosition, (MedicinesItem) DialogAddMedicineSub.this.medicineSubsItems.get(pos));
            DialogAddMedicineSub.this.dismiss();
        }
    }

    public DialogAddMedicineSub(@NonNull Context context, List<String> genericName, MediDBHelper mediDBHelper, int subForPosition) {
        super(context);
        this.context = context;
        this.genericName = genericName;
        this.mediDBHelper = mediDBHelper;
        this.subForPosition = subForPosition;
    }

    public void setListener(OnMedicineUpdateListener listener) {
        this.listener = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0341R.layout.dialog_add_medicine_sub);
        this.medicineSubsItems = new ArrayList();
        this.recyclerSubMeds = (RecyclerView) findViewById(C0341R.id.recyclerSubMeds);
        this.recyclerSubMeds.setLayoutManager(new LinearLayoutManager(this.context, 1, false));
        this.mediSubsAdapter = new MediSubsAdapter(this.context, this.medicineSubsItems);
        this.mediSubsAdapter.setSubClickListener(this.onMediSubClickListener);
        this.recyclerSubMeds.setAdapter(this.mediSubsAdapter);
        new GetSubsTask().execute(new Object[0]);
    }
}
