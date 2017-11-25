package in.mobiant.medical.utility;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;
import in.mobiant.medical.C0341R;

public class ProgressDialog extends Dialog {
    private String msg;

    public ProgressDialog(@NonNull Context context, String msg) {
        super(context, 16973939);
        this.msg = msg;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0341R.layout.dialog_progress);
        ((TextView) findViewById(C0341R.id.textViewMsg)).setText(this.msg);
    }
}
