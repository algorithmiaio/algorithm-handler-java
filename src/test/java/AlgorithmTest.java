import com.algorithmia.Algorithmia;
import com.algorithmia.algo.*;
import com.algorithmia.TypeToken;
import com.google.gson.JsonElement;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assume;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class AlgorithmTest {

    private String key;

    @Before
    public void setup() {
        key = System.getenv("ALGORITHMIA_API_KEY");
        Assume.assumeNotNull(key);
    }

    @Test
    public void algorithmPipeJson() throws Exception {
        AlgoResponse res = Algorithmia.client(key).algo("docs/JavaAddOne").pipe(41);
        Assert.assertEquals("42", res.as(new TypeToken<JsonElement>(){}).toString());
        int result = res.as(new TypeToken<Integer>(){});
        Assert.assertEquals(42, result);
        Assert.assertEquals(ContentType.Json, res.getMetadata().getContentType());
    }

    @Test
    public void algorithmPipeText() throws Exception {
        AlgoResponse res = Algorithmia.client(key).algo("demo/Hello").pipe("foo");
        Assert.assertEquals("\"Hello foo\"", res.as(new TypeToken<JsonElement>(){}).toString());
        Assert.assertEquals("\"Hello foo\"", res.asJsonString());
        Assert.assertEquals("Hello foo", res.as(new TypeToken<String>(){}));
        Assert.assertEquals("Hello foo", res.asString());
        Assert.assertEquals(ContentType.Text, res.getMetadata().getContentType());
    }

    @Test
    public void algorithmPipeBinary() throws Exception {
        byte[] input = new byte[10];
        AlgoResponse res = Algorithmia.client(key).algo("docs/JavaBinaryInAndOut").pipe(input);
        byte[] output = res.as(new TypeToken<byte[]>(){});
        Assert.assertEquals(Base64.encodeBase64String(input),Base64.encodeBase64String(output));
        Assert.assertEquals(ContentType.Binary, res.getMetadata().getContentType());
    }

    @Test
    public void algorithmRawOutput() throws Exception {
        AlgoResponse res = Algorithmia.client(key).algo("demo/Hello")
                .setOutputType(Algorithm.AlgorithmOutputType.RAW).pipe("foo");
        Assert.assertEquals("Hello foo", res.getRawOutput());
        Assert.assertEquals(null, res.getMetadata());
    }

    @Test
    public void algorithmVoidOutput() throws Exception {
        AlgoAsyncResponse res = Algorithmia.client(key).algo("demo/Hello")
                .setOutputType(Algorithm.AlgorithmOutputType.VOID).pipe("foo")
                .getAsyncResponse();
        Assert.assertEquals("void", res.getAsyncProtocol());
        Assert.assertTrue(res.getRequestId() != null);  // request is unpredictable, but should be *something*
    }

    @Test
    public void algorithmSetOption() throws Exception {
        AlgoResponse res = Algorithmia.client(key).algo("demo/Hello")
                .setOption("output", "raw").pipe("foo");

        Assert.assertEquals("Hello foo", res.getRawOutput());
    }

    @Test
    public void algorithmSetOptions() throws Exception {
        Map<String, String> options = new HashMap<String, String>();
        options.put("output", "raw");

        AlgoResponse res = Algorithmia.client(key).algo("demo/Hello")
                .setOptions(options).pipe("foo");

        Assert.assertEquals("Hello foo", res.getRawOutput());

    }

    @Test
    public void algorithmCheckTimeout() throws Exception {
        Algorithm algo = Algorithmia.client(key).algo("docs/JavaAddOne");

        // Check default timeout - just for fun. This doesn't have to be specified at all time
        // but I wanted to make sure this method never throws an exception when the key in the options
        // is null.
        Assert.assertEquals((long)300, (long)algo.getTimeout());

        // Check Minute conversion
        algo = algo.setTimeout(20L, TimeUnit.MINUTES);
        Assert.assertEquals((long)20 * 60, (long)algo.getTimeout());

        // And seconds just in case
        algo = algo.setTimeout(30L, TimeUnit.SECONDS);
        Assert.assertEquals((long)30, (long)algo.getTimeout());

        // And milliseconds
        algo = algo.setTimeout(5000L, TimeUnit.MILLISECONDS);
        Assert.assertEquals((long)5, (long)algo.getTimeout());
    }
}
