package in.mobiant.medical;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import in.mobiant.medical.models.UserInfoItem;
import in.mobiant.medical.utility.Utils;
import in.mobiant.medical.views.DashboardFragment;

public class MedicalDashboardActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    public ActionBarDrawerToggle drawerToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0341R.layout.app_bar_medical_dashboard);
        setSupportActionBar((Toolbar) findViewById(C0341R.id.toolbar));
        getSupportFragmentManager().beginTransaction().replace(C0341R.id.dashboard_frame_layout, new DashboardFragment(), "DashboardFragment").commit();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClick(View view) {
        if (view.getId() == C0341R.id.buttonSignOut) {
            Utils.saveLogin(this, false, new UserInfoItem());
            finish();
            startActivity(new Intent(this, MedicalLoginActivity.class));
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        ((DrawerLayout) findViewById(C0341R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    public boolean checkPermissions() {
        boolean isTrue = true;
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 0);
            isTrue = false;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return isTrue;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        return false;
    }
}
