package AlgorithmHandler;

import AlgorithmHandler.algorithms.BasicAlgorithmInteger;
import com.algorithmia.algorithmHandler.AlgorithmHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PrimitiveInputTest extends AlgorithmHandlerTestBase {
    /// TEXT hello world

    private BasicAlgorithmInteger algo = new BasicAlgorithmInteger();
    private Gson gson = new Gson();
    private JsonObject request = PrepareInput();
    private JsonObject expectedResponse = PrepareOutput();

    JsonObject PrepareInput(){
        Float inputObj = 32.5f;
        JsonObject object = new JsonObject();
        object.addProperty("content_type", "json");
        object.add("data", gson.toJsonTree(inputObj));
        return object;
    }

    JsonObject PrepareOutput(){
        JsonObject expectedResponse = new JsonObject();
        JsonObject metadata = new JsonObject();
        metadata.addProperty("content_type", "text");
        expectedResponse.add("metadata", metadata);
        expectedResponse.addProperty("result", "Hello, the number is 32.5");
        return expectedResponse;
    }


    @Test
    public void RunAlgorithm()throws Exception{

        AlgorithmHandler handler = new AlgorithmHandler<>(algo::Foo, Float.class);
        InputStream fakeIn = new ByteArrayInputStream(request.toString().getBytes());

        System.setIn(fakeIn);
        handler.run();

        byte[] fifoBytes = Files.readAllBytes(Paths.get(FIFOPIPE));
        String rawData = new String(fifoBytes);
        JsonObject actualResponse  = parser.parse(rawData).getAsJsonObject();
        Assert.assertEquals(expectedResponse, actualResponse);

    }
}