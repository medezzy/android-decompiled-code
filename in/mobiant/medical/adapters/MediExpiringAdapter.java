package in.mobiant.medical.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.models.MedicinesItem;
import java.util.List;

public class MediExpiringAdapter extends Adapter<ExpiringViewHolder> {
    Context context;
    List<MedicinesItem> medicinesItemList;

    class ExpiringViewHolder extends ViewHolder {
        TextView textViewName;
        TextView textViewType;

        public ExpiringViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(C0341R.id.textViewName);
            this.textViewType = (TextView) itemView.findViewById(C0341R.id.textViewType);
        }
    }

    public MediExpiringAdapter(Context context, List<MedicinesItem> medicinesItemList) {
        this.context = context;
        this.medicinesItemList = medicinesItemList;
    }

    public ExpiringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpiringViewHolder(LayoutInflater.from(this.context).inflate(C0341R.layout.item_stock_status_list, parent, false));
    }

    public void onBindViewHolder(ExpiringViewHolder holder, int position) {
        holder.textViewName.setText(((MedicinesItem) this.medicinesItemList.get(position)).getId());
        holder.textViewType.setText(String.format("%s%s  %s%s", new Object[]{this.context.getString(C0341R.string.stockQty), Integer.valueOf(item.getStockQty()), this.context.getString(C0341R.string.type), item.getDrugType()}));
    }

    public int getItemCount() {
        return this.medicinesItemList.size();
    }
}
