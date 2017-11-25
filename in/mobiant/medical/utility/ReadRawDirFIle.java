package in.mobiant.medical.utility;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadRawDirFIle {
    private String readFileFromRawDirectory(Context context, int resourceId) {
        IOException e;
        InputStream iStream = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = null;
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            ByteArrayOutputStream byteStream2 = new ByteArrayOutputStream();
            try {
                byteStream2.write(buffer);
                byteStream2.close();
                iStream.close();
                byteStream = byteStream2;
            } catch (IOException e2) {
                e = e2;
                byteStream = byteStream2;
                e.printStackTrace();
                return byteStream.toString();
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return byteStream.toString();
        }
        return byteStream.toString();
    }
}
