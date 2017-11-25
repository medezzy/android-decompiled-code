package in.mobiant.medical.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton.OnValueChangeListener;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.utility.Variables;
import in.mobiant.medical.views.NewPrescriptionFragment.OnMedicineUpdateListener;
import java.util.List;

public class AddMedicineListAdapter extends Adapter<MedicineItemViewHolder> {
    private Context context;
    private List<MedicinesItem> medicinesList;
    private OnMedicineUpdateListener onMedicineUpdateListener;

    class MedicineItemViewHolder extends ViewHolder {
        ElegantNumberButton buttonQuantity;
        ImageView buttonSubstitute;
        EditText editTextTaxes;
        ImageView imageViewDelete;
        int packed_qty = 1;
        TextInputLayout textInputLayout4;
        TextView textView6;
        TextView textViewMediEachPrice;
        TextView textViewMediName;
        TextView textViewMediPrice;
        TextView textViewMediType;
        TextView textViewStock;

        MedicineItemViewHolder(View itemView) {
            super(itemView);
            this.textViewMediName = (TextView) itemView.findViewById(C0341R.id.textViewMediName);
            this.textViewMediType = (TextView) itemView.findViewById(C0341R.id.textViewMediType);
            this.textViewMediPrice = (TextView) itemView.findViewById(C0341R.id.textViewMediPrice);
            this.textViewMediEachPrice = (TextView) itemView.findViewById(C0341R.id.textViewMediEachPrice);
            this.textViewStock = (TextView) itemView.findViewById(C0341R.id.textViewStock);
            this.editTextTaxes = (EditText) itemView.findViewById(C0341R.id.editTextTaxes);
            this.buttonQuantity = (ElegantNumberButton) itemView.findViewById(C0341R.id.buttonQuantity);
            this.buttonSubstitute = (ImageView) itemView.findViewById(C0341R.id.buttonSubstitute);
            this.imageViewDelete = (ImageView) itemView.findViewById(C0341R.id.imageViewDelete);
            this.textView6 = (TextView) itemView.findViewById(C0341R.id.textView6);
            this.textInputLayout4 = (TextInputLayout) itemView.findViewById(C0341R.id.textInputLayout4);
            this.textViewMediPrice.setText(String.format("%s 0", new Object[]{this$0.context.getString(C0341R.string.rupees)}));
        }
    }

    public AddMedicineListAdapter(Context context, List<MedicinesItem> medicinesList, OnMedicineUpdateListener onMedicineUpdateListener) {
        this.context = context;
        this.medicinesList = medicinesList;
        this.onMedicineUpdateListener = onMedicineUpdateListener;
    }

    public MedicineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MedicineItemViewHolder(LayoutInflater.from(this.context).inflate(C0341R.layout.item_medicine_list, parent, false));
    }

    @SuppressLint({"DefaultLocale"})
    public void onBindViewHolder(final MedicineItemViewHolder holder, final int position) {
        final MedicinesItem item = (MedicinesItem) this.medicinesList.get(position);
        if (item.getDrugType().equals(Variables.DRUG_TYPE_STRIP)) {
            holder.packed_qty = Integer.parseInt(item.getPackedQty());
        }
        holder.textViewMediName.setText(String.format("%s", new Object[]{item.getId()}));
        holder.textViewMediType.setText(String.format("%s %s", new Object[]{this.context.getString(C0341R.string.type1), item.getDrugType()}));
        if (item.getStockQty() == 0) {
            holder.textViewStock.setTextColor(SupportMenu.CATEGORY_MASK);
        }
        holder.textViewStock.setText(String.format("%s%s", new Object[]{this.context.getString(C0341R.string.stockQty), Integer.valueOf(item.getStockQty())}));
        item.setTax(Float.parseFloat(holder.editTextTaxes.getText().toString()) / 100.0f);
        item.setEachPrice(item.getPackedPrice() / ((float) holder.packed_qty));
        item.setEachPriceWithTax(item.getEachPrice() + (item.getEachPrice() * item.getTax()));
        TextView textView = holder.textViewMediEachPrice;
        Object[] objArr = new Object[2];
        objArr[0] = this.context.getString(C0341R.string.rupees);
        objArr[1] = String.format("%.2f", new Object[]{Float.valueOf(item.getEachPriceWithTax())});
        textView.setText(String.format("%s%s", objArr));
        holder.buttonQuantity.setOnValueChangeListener(new OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Log.v(Variables.TAG, "old value : " + oldValue + " new value : " + newValue);
                item.setSelectedQty(newValue);
                TextView textView = holder.textViewMediPrice;
                Object[] objArr = new Object[2];
                objArr[0] = AddMedicineListAdapter.this.context.getString(C0341R.string.rupees);
                objArr[1] = String.format("%.2f", new Object[]{Float.valueOf(item.getEachPriceWithTax() * ((float) newValue))});
                textView.setText(String.format("%s%s", objArr));
                AddMedicineListAdapter.this.setTotalPrice();
            }
        });
        holder.buttonQuantity.setNumber(String.valueOf(item.getSelectedQty()), true);
        holder.editTextTaxes.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID != 6 || holder.editTextTaxes.getText().length() <= 0) {
                    return false;
                }
                item.setTax(Float.parseFloat(holder.editTextTaxes.getText().toString()) / 100.0f);
                item.setEachPriceWithTax(item.getEachPrice() + (item.getEachPrice() * item.getTax()));
                TextView textView2 = holder.textViewMediEachPrice;
                Object[] objArr = new Object[2];
                objArr[0] = AddMedicineListAdapter.this.context.getString(C0341R.string.rupees);
                objArr[1] = String.format("%.2f", new Object[]{Float.valueOf(item.getEachPriceWithTax())});
                textView2.setText(String.format("%s%s", objArr));
                textView2 = holder.textViewMediPrice;
                objArr = new Object[2];
                objArr[0] = AddMedicineListAdapter.this.context.getString(C0341R.string.rupees);
                objArr[1] = String.format("%.2f", new Object[]{Float.valueOf(item.getEachPriceWithTax() * ((float) holder.packed_qty))});
                textView2.setText(String.format("%s%s", objArr));
                AddMedicineListAdapter.this.setTotalPrice();
                return true;
            }
        });
        holder.buttonSubstitute.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddMedicineListAdapter.this.onMedicineUpdateListener.showMedicineSub(position, item.getReferencedGenericMedicine());
            }
        });
        holder.imageViewDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddMedicineListAdapter.this.medicinesList.remove(holder.getAdapterPosition());
                AddMedicineListAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                AddMedicineListAdapter.this.notifyDataSetChanged();
                AddMedicineListAdapter.this.setTotalPrice();
            }
        });
    }

    private void setTotalPrice() {
        float total_amout = 0.0f;
        for (MedicinesItem medicinesItem : this.medicinesList) {
            total_amout += medicinesItem.getEachPriceWithTax() * ((float) medicinesItem.getSelectedQty());
        }
        this.onMedicineUpdateListener.setTotalPrice(total_amout);
    }

    public void onViewAttachedToWindow(MedicineItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setTotalPrice();
    }

    public int getItemCount() {
        return this.medicinesList.size();
    }
}
