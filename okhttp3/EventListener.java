package okhttp3;

import java.net.InetAddress;
import java.util.List;

abstract class EventListener {
    public static final EventListener NONE = new C05631();

    public interface Factory {
        EventListener create(Call call);
    }

    class C05631 extends EventListener {
        C05631() {
        }
    }

    class C05642 implements Factory {
        final /* synthetic */ EventListener val$listener;

        C05642(EventListener eventListener) {
            this.val$listener = eventListener;
        }

        public EventListener create(Call call) {
            return this.val$listener;
        }
    }

    EventListener() {
    }

    static Factory factory(EventListener listener) {
        return new C05642(listener);
    }

    public void fetchStart(Call call) {
    }

    public void dnsStart(Call call, String domainName) {
    }

    public void dnsEnd(Call call, String domainName, List<InetAddress> list, Throwable throwable) {
    }

    public void connectStart(Call call, InetAddress address, int port) {
    }

    public void secureConnectStart(Call call) {
    }

    public void secureConnectEnd(Call call, Handshake handshake, Throwable throwable) {
    }

    public void connectEnd(Call call, InetAddress address, int port, String protocol, Throwable throwable) {
    }

    public void requestHeadersStart(Call call) {
    }

    public void requestHeadersEnd(Call call, Throwable throwable) {
    }

    public void requestBodyStart(Call call) {
    }

    public void requestBodyEnd(Call call, Throwable throwable) {
    }

    public void responseHeadersStart(Call call) {
    }

    public void responseHeadersEnd(Call call, Throwable throwable) {
    }

    public void responseBodyStart(Call call) {
    }

    public void responseBodyEnd(Call call, Throwable throwable) {
    }

    public void fetchEnd(Call call, Throwable throwable) {
    }
}
