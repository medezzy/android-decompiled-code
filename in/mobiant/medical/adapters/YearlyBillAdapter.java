package in.mobiant.medical.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.models.BillInfo;
import java.util.List;

public class YearlyBillAdapter extends Adapter<YearlyBillItem> {
    private List<BillInfo> billInfoList;
    private Context context;

    class YearlyBillItem extends ViewHolder {
        TextView textViewAmount;
        TextView textViewBillNo;

        public YearlyBillItem(View itemView) {
            super(itemView);
            this.textViewBillNo = (TextView) itemView.findViewById(C0341R.id.textViewName);
            this.textViewAmount = (TextView) itemView.findViewById(C0341R.id.textViewType);
        }
    }

    public YearlyBillAdapter(Context context, List<BillInfo> billInfoList) {
        this.billInfoList = billInfoList;
        this.context = context;
    }

    public YearlyBillItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YearlyBillItem(LayoutInflater.from(this.context).inflate(C0341R.layout.item_dwm_reports, parent, false));
    }

    public void onBindViewHolder(YearlyBillItem holder, int position) {
        BillInfo billInfo = (BillInfo) this.billInfoList.get(position);
        holder.textViewBillNo.setText(String.format("Bill no : %s", new Object[]{billInfo.getBillNo()}));
        holder.textViewAmount.setText(Html.fromHtml("<font color='blue'> Date : " + billInfo.getDate() + "</font> Amount : " + billInfo.getAmount()), BufferType.SPANNABLE);
    }

    public int getItemCount() {
        return this.billInfoList.size();
    }
}
