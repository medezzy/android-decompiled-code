package in.mobiant.medical.views;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import in.mobiant.medical.MedicalDashboardActivity;

public class SetActionBar {
    public static void setup(Activity activity, int icon, String title) {
        ActionBar actionBar = ((MedicalDashboardActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(icon);
            actionBar.setTitle((CharSequence) title);
        }
    }
}
