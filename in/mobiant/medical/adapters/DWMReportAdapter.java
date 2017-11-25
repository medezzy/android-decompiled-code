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

public class DWMReportAdapter extends Adapter<ExpiringViewHolder> {
    Context context;
    List<MedicinesItem> medicinesItemList;
    private int reportType = 0;

    class ExpiringViewHolder extends ViewHolder {
        TextView textViewName;
        TextView textViewType;

        public ExpiringViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(C0341R.id.textViewName);
            this.textViewType = (TextView) itemView.findViewById(C0341R.id.textViewType);
        }
    }

    public DWMReportAdapter(Context context, List<MedicinesItem> medicinesItemList, int reportType) {
        this.context = context;
        this.medicinesItemList = medicinesItemList;
        this.reportType = reportType;
    }

    public ExpiringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpiringViewHolder(LayoutInflater.from(this.context).inflate(C0341R.layout.item_dwm_reports, parent, false));
    }

    public void onBindViewHolder(ExpiringViewHolder holder, int position) {
        holder.textViewName.setText(((MedicinesItem) this.medicinesItemList.get(position)).getId());
        switch (this.reportType) {
            case 1:
                holder.textViewType.setText(String.format("%s%s  %s%s", new Object[]{this.context.getString(C0341R.string.type), item.getDrugType(), " | Sell Qty : ", Integer.valueOf(item.getD_sell())}));
                return;
            case 2:
                holder.textViewType.setText(String.format("%s%s  %s%s", new Object[]{this.context.getString(C0341R.string.type), item.getDrugType(), " | Sell Qty : ", Integer.valueOf(item.getW_sell())}));
                return;
            case 3:
                holder.textViewType.setText(String.format("%s%s  %s%s", new Object[]{this.context.getString(C0341R.string.type), item.getDrugType(), " | Sell Qty : ", Integer.valueOf(item.getM_sell())}));
                return;
            case 4:
                holder.textViewType.setText(String.format("%s%s  %s%s", new Object[]{this.context.getString(C0341R.string.type), item.getDrugType(), " | Buy Qty : ", Integer.valueOf(item.getD_buy())}));
                return;
            case 5:
                holder.textViewType.setText(String.format("%s%s  %s%s", new Object[]{this.context.getString(C0341R.string.type), item.getDrugType(), " | Buy Qty : ", Integer.valueOf(item.getW_buy())}));
                return;
            case 6:
                holder.textViewType.setText(String.format("%s%s  %s%s", new Object[]{this.context.getString(C0341R.string.type), item.getDrugType(), " | Buy Qty : ", Integer.valueOf(item.getM_buy())}));
                return;
            default:
                return;
        }
    }

    public int getItemCount() {
        return this.medicinesItemList.size();
    }

    public void setReportType(int type) {
        this.reportType = type;
    }
}
