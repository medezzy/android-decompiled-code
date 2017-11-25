package in.mobiant.medical.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.TextView;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.models.BillInfo;
import java.util.ArrayList;
import java.util.List;

public class PatientNamesAdapter extends ArrayAdapter<BillInfo> {
    private final Context mContext;
    private List<BillInfo> namesItems;
    private List<BillInfo> namesItemsSugg;
    private List<BillInfo> namesItems_All;

    class C03471 extends Filter {
        C03471() {
        }

        public String convertResultToString(Object resultValue) {
            return ((BillInfo) resultValue).getPatientInfo().getFullName();
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null) {
                return new FilterResults();
            }
            PatientNamesAdapter.this.namesItemsSugg.clear();
            for (BillInfo billInfo : PatientNamesAdapter.this.namesItems) {
                if (billInfo.getPatientInfo().getFullName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                    PatientNamesAdapter.this.namesItemsSugg.add(billInfo);
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = PatientNamesAdapter.this.namesItemsSugg;
            filterResults.count = PatientNamesAdapter.this.namesItemsSugg.size();
            return filterResults;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            PatientNamesAdapter.this.namesItems.clear();
            if (results != null && results.count > 0) {
                for (Object object : results.values) {
                    if (object instanceof BillInfo) {
                        PatientNamesAdapter.this.namesItems.add((BillInfo) object);
                    }
                }
            } else if (constraint == null) {
                PatientNamesAdapter.this.namesItems.addAll(PatientNamesAdapter.this.namesItems_All);
            }
            PatientNamesAdapter.this.notifyDataSetChanged();
        }
    }

    public PatientNamesAdapter(@NonNull Context context, int resource, @NonNull List<BillInfo> namesItems) {
        super(context, resource, namesItems);
        this.mContext = context;
        this.namesItems = namesItems;
        this.namesItems_All = new ArrayList(namesItems);
        this.namesItemsSugg = new ArrayList(namesItems);
    }

    public int getCount() {
        return this.namesItems.size();
    }

    public BillInfo getItem(int position) {
        return (BillInfo) this.namesItems.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            try {
                convertView = ((Activity) this.mContext).getLayoutInflater().inflate(C0341R.layout.item_medicine_names_drop_down, parent, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        convertView.findViewById(C0341R.id.textViewType).setVisibility(8);
        convertView.findViewById(C0341R.id.textViewPrice).setVisibility(8);
        ((TextView) convertView.findViewById(C0341R.id.textViewName)).setText(getItem(position).getPatientInfo().getFullName());
        return convertView;
    }

    @NonNull
    public Filter getFilter() {
        return new C03471();
    }
}
