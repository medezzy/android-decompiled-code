package okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public interface Dns {
    public static final Dns SYSTEM = new C05621();

    class C05621 implements Dns {
        C05621() {
        }

        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            if (hostname != null) {
                return Arrays.asList(InetAddress.getAllByName(hostname));
            }
            throw new UnknownHostException("hostname == null");
        }
    }

    List<InetAddress> lookup(String str) throws UnknownHostException;
}
