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
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.utility.Variables;
import java.util.ArrayList;
import java.util.List;

public class MediNamesAdapter extends ArrayAdapter<MedicinesItem> {
    private final Context mContext;
    private List<MedicinesItem> medicinesItems;
    private List<MedicinesItem> medicinesItemsSugg;
    private List<MedicinesItem> medicinesItems_All;

    class C03451 extends Filter {
        C03451() {
        }

        public String convertResultToString(Object resultValue) {
            return ((MedicinesItem) resultValue).getId();
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null) {
                return new FilterResults();
            }
            MediNamesAdapter.this.medicinesItemsSugg.clear();
            for (MedicinesItem medicinesItem : MediNamesAdapter.this.medicinesItems) {
                if (medicinesItem.getId().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                    MediNamesAdapter.this.medicinesItemsSugg.add(medicinesItem);
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = MediNamesAdapter.this.medicinesItemsSugg;
            filterResults.count = MediNamesAdapter.this.medicinesItemsSugg.size();
            return filterResults;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            MediNamesAdapter.this.medicinesItems.clear();
            if (results != null && results.count > 0) {
                for (Object object : results.values) {
                    if (object instanceof MedicinesItem) {
                        MediNamesAdapter.this.medicinesItems.add((MedicinesItem) object);
                    }
                }
            } else if (constraint == null) {
                MediNamesAdapter.this.medicinesItems.addAll(MediNamesAdapter.this.medicinesItems_All);
            }
            MediNamesAdapter.this.notifyDataSetChanged();
        }
    }

    public MediNamesAdapter(@NonNull Context context, int resource, @NonNull List<MedicinesItem> medicinesItems) {
        super(context, resource, medicinesItems);
        this.mContext = context;
        this.medicinesItems = medicinesItems;
        this.medicinesItems_All = new ArrayList(medicinesItems);
        this.medicinesItemsSugg = new ArrayList(medicinesItems);
    }

    public int getCount() {
        return this.medicinesItems.size();
    }

    public MedicinesItem getItem(int position) {
        return (MedicinesItem) this.medicinesItems.get(position);
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
        MedicinesItem medicinesItem = getItem(position);
        ((TextView) convertView.findViewById(C0341R.id.textViewName)).setText(String.format("%s", new Object[]{medicinesItem.getId()}));
        ((TextView) convertView.findViewById(C0341R.id.textViewType)).setText(String.format(this.mContext.getString(C0341R.string.type) + " %s", new Object[]{medicinesItem.getDrugType()}));
        int packed_qty = 1;
        if (medicinesItem.getDrugType().equals(Variables.DRUG_TYPE_STRIP)) {
            packed_qty = Integer.parseInt(medicinesItem.getPackedQty());
        }
        medicinesItem.setEachPrice(medicinesItem.getPackedPrice() / ((float) packed_qty));
        medicinesItem.setEachPriceWithTax(medicinesItem.getEachPrice() + (medicinesItem.getEachPrice() * medicinesItem.getTax()));
        ((TextView) convertView.findViewById(C0341R.id.textViewPrice)).setText(String.format(this.mContext.getString(C0341R.string.rupees) + " %s", new Object[]{Float.valueOf(medicinesItem.getEachPriceWithTax())}));
        return convertView;
    }

    @NonNull
    public Filter getFilter() {
        return new C03451();
    }
}
