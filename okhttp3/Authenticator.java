package okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

public interface Authenticator {
    public static final Authenticator NONE = new C05591();

    class C05591 implements Authenticator {
        C05591() {
        }

        public Request authenticate(Route route, Response response) {
            return null;
        }
    }

    @Nullable
    Request authenticate(Route route, Response response) throws IOException;
}
