package in.mobiant.medical.adapters;

import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.utility.Variables;
import in.mobiant.medical.views.DialogAddMedicineSub.OnMediSubClickListener;
import java.util.List;

public class MediSubsAdapter extends Adapter<MedSubViewHolder> {
    private Context context;
    List<MedicinesItem> medicineSubsItems;
    OnMediSubClickListener subClickListener;

    class MedSubViewHolder extends ViewHolder {
        View itemView;
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewStock;
        TextView textViewType;

        MedSubViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.textViewName = (TextView) itemView.findViewById(C0341R.id.textViewName);
            this.textViewType = (TextView) itemView.findViewById(C0341R.id.textViewType);
            this.textViewPrice = (TextView) itemView.findViewById(C0341R.id.textViewPrice);
            this.textViewStock = (TextView) itemView.findViewById(C0341R.id.textViewStock);
        }
    }

    public MediSubsAdapter(Context context, List<MedicinesItem> medicineSubsItems) {
        this.context = context;
        this.medicineSubsItems = medicineSubsItems;
    }

    public MedSubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MedSubViewHolder(LayoutInflater.from(this.context).inflate(C0341R.layout.item_medicine_substitute, parent, false));
    }

    public void onBindViewHolder(MedSubViewHolder holder, final int position) {
        MedicinesItem item = (MedicinesItem) this.medicineSubsItems.get(position);
        holder.textViewName.setText(String.format("%s", new Object[]{item.getId()}));
        holder.textViewType.setText(String.format("%s%s", new Object[]{this.context.getString(C0341R.string.type1), item.getDrugType()}));
        int packed_qty = 1;
        if (item.getDrugType().equals(Variables.DRUG_TYPE_STRIP)) {
            packed_qty = Integer.parseInt(item.getPackedQty());
        }
        item.setEachPrice(item.getPackedPrice() / ((float) packed_qty));
        item.setEachPriceWithTax(item.getEachPrice() + (item.getEachPrice() * item.getTax()));
        TextView textView = holder.textViewPrice;
        Object[] objArr = new Object[2];
        objArr[0] = this.context.getString(C0341R.string.rupees);
        objArr[1] = String.format("%.2f", new Object[]{Float.valueOf(item.getEachPriceWithTax())});
        textView.setText(String.format("%s%s", objArr));
        if (item.getStockQty() == 0) {
            holder.textViewStock.setTextColor(SupportMenu.CATEGORY_MASK);
        }
        holder.textViewStock.setText(String.format("%s%s", new Object[]{this.context.getString(C0341R.string.stockQty), Integer.valueOf(item.getStockQty())}));
        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MediSubsAdapter.this.subClickListener.onSubstituteClick(position);
            }
        });
    }

    public int getItemCount() {
        return this.medicineSubsItems.size();
    }

    public void setSubClickListener(OnMediSubClickListener subClickListener) {
        this.subClickListener = subClickListener;
    }
}
