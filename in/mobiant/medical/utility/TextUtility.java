package in.mobiant.medical.utility;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class TextUtility {
    public static boolean isEmpty(EditText editText) {
        return editText.length() < 2;
    }

    public static String getText(EditText editText) {
        return editText.getText().toString();
    }

    public static void setText(EditText editText, String str) {
        editText.setText(str);
    }

    public static void setError(EditText editText, String str) {
        editText.setError(str);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidMobile(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() == 10;
    }
}
