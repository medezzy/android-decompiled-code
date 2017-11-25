package in.mobiant.medical.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog.Builder;

public class ShowDialogs {

    static class C03521 implements OnClickListener {
        C03521() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    public static void showAlertDialog(Context context, String msg) {
        new Builder(context).setMessage((CharSequence) msg).setPositiveButton((CharSequence) "OK", new C03521()).create().show();
    }
}
