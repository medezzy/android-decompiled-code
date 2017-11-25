package in.mobiant.medical.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import in.mobiant.medical.C0341R;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.AlertInfoItem;

public class AlertsFragment extends Fragment {
    MediDBHelper mediDBHelper;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Alerts");
        this.mediDBHelper = new MediDBHelper(getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(C0341R.layout.fragment_alerts, container, false);
        setHasOptionsMenu(true);
        for (AlertInfoItem item : this.mediDBHelper.getAlertInfo()) {
            View view1 = inflater.inflate(C0341R.layout.item_alert_info, container, false);
            ((TextView) view1.findViewById(C0341R.id.textViewInfo)).setText(Html.fromHtml("<font color='red'>" + item.getCount() + "</font> Medicines going to expire in next <font color='red'>" + item.getDays() + "</font> days."), BufferType.SPANNABLE);
            linearLayout.addView(view1);
        }
        return linearLayout;
    }

    public void onResume() {
        super.onResume();
        SetActionBar.setup(getActivity(), C0341R.mipmap.arrow_back, "Alerts");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
