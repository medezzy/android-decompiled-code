package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;

public abstract class RequestBody {

    class C05661 extends RequestBody {
        final /* synthetic */ ByteString val$content;
        final /* synthetic */ MediaType val$contentType;

        C05661(MediaType mediaType, ByteString byteString) {
            this.val$contentType = mediaType;
            this.val$content = byteString;
        }

        @Nullable
        public MediaType contentType() {
            return this.val$contentType;
        }

        public long contentLength() throws IOException {
            return (long) this.val$content.size();
        }

        public void writeTo(BufferedSink sink) throws IOException {
            sink.write(this.val$content);
        }
    }

    class C05672 extends RequestBody {
        final /* synthetic */ int val$byteCount;
        final /* synthetic */ byte[] val$content;
        final /* synthetic */ MediaType val$contentType;
        final /* synthetic */ int val$offset;

        C05672(MediaType mediaType, int i, byte[] bArr, int i2) {
            this.val$contentType = mediaType;
            this.val$byteCount = i;
            this.val$content = bArr;
            this.val$offset = i2;
        }

        @Nullable
        public MediaType contentType() {
            return this.val$contentType;
        }

        public long contentLength() {
            return (long) this.val$byteCount;
        }

        public void writeTo(BufferedSink sink) throws IOException {
            sink.write(this.val$content, this.val$offset, this.val$byteCount);
        }
    }

    class C05683 extends RequestBody {
        final /* synthetic */ MediaType val$contentType;
        final /* synthetic */ File val$file;

        C05683(MediaType mediaType, File file) {
            this.val$contentType = mediaType;
            this.val$file = file;
        }

        @Nullable
        public MediaType contentType() {
            return this.val$contentType;
        }

        public long contentLength() {
            return this.val$file.length();
        }

        public void writeTo(BufferedSink sink) throws IOException {
            Closeable source = null;
            try {
                source = Okio.source(this.val$file);
                sink.writeAll(source);
            } finally {
                Util.closeQuietly(source);
            }
        }
    }

    @Nullable
    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink bufferedSink) throws IOException;

    public long contentLength() throws IOException {
        return -1;
    }

    public static RequestBody create(@Nullable MediaType contentType, String content) {
        Charset charset = Util.UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = Util.UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        return create(contentType, content.getBytes(charset));
    }

    public static RequestBody create(@Nullable MediaType contentType, ByteString content) {
        return new C05661(contentType, content);
    }

    public static RequestBody create(@Nullable MediaType contentType, byte[] content) {
        return create(contentType, content, 0, content.length);
    }

    public static RequestBody create(@Nullable MediaType contentType, byte[] content, int offset, int byteCount) {
        if (content == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount((long) content.length, (long) offset, (long) byteCount);
        return new C05672(contentType, byteCount, content, offset);
    }

    public static RequestBody create(@Nullable MediaType contentType, File file) {
        if (file != null) {
            return new C05683(contentType, file);
        }
        throw new NullPointerException("content == null");
    }
}
