package in.mobiant.drindian;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import in.mobiant.medical.MedicalLoginActivity;

public class MainActivity extends AppCompatActivity {
    OnClickListener altertOkListener = new C03371();
    AlertDialog dialog = null;

    class C03371 implements OnClickListener {
        C03371() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            MainActivity.this.dismissAlertDialog();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        intent.setClass(this, MedicalLoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case C0338R.id.buttonDoctors:
                showAlertDialog("Doctors feature coming soon...");
                break;
            case C0338R.id.buttonFrontDesk:
                showAlertDialog("Front Desk feature coming soon...");
                break;
            case C0338R.id.buttonLabs:
                showAlertDialog("Labs feature coming soon...");
                break;
            case C0338R.id.buttonMedicals:
                intent = new Intent();
                intent.setClass(this, MedicalLoginActivity.class);
                break;
            case C0338R.id.buttonPatients:
                showAlertDialog("Patients feature coming soon...");
                break;
        }
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

    private void showAlertDialog(String msg) {
        if (this.dialog == null) {
            this.dialog = new Builder(this).setMessage((CharSequence) msg).setNeutralButton((CharSequence) "OK", this.altertOkListener).create();
            this.dialog.show();
            return;
        }
        this.dialog.setMessage(msg);
    }

    private void dismissAlertDialog() {
        if (this.dialog != null) {
            this.dialog.dismiss();
            this.dialog = null;
        }
    }
}
