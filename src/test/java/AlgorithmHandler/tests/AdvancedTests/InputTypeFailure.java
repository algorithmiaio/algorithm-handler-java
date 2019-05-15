//package AlgorithmHandler.tests.AdvancedTests;
//
//import AlgorithmHandler.algorithms.LoadingAlgorithm;
//import AlgorithmHandler.algorithms.MatrixAlgorithm;
//import com.algorithmia.algorithm.Handler;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//public class InputTypeFailure extends HandlerTestBase {
//
//    private LoadingAlgorithm algo = new LoadingAlgorithm();
//    private Gson gson = new Gson();
//    private JsonObject request = GenerateInput();
//    private JsonObject expectedResponse = GenerateOutput();
//
//    public JsonObject GenerateInput() {
//        MatrixAlgorithm tmp = new MatrixAlgorithm();
//        MatrixAlgorithm.AlgoInput inputObj = tmp.new AlgoInput(new Float[]{0.25f, 0.15f}, new Float[]{0.12f, -0.15f});
//        JsonObject object = new JsonObject();
//        object.addProperty("content_type", "json");
//        object.add("data", gson.toJsonTree(inputObj));
//        return object;
//    }
//
//    public JsonObject GenerateOutput() {
//        JsonObject expectedResponse = new JsonObject();
//        expectedResponse.addProperty("message", "Missing required field in JSON input: name");
//        return expectedResponse;
//    }
//
//
//    @Test
//    public void RunAlgorithm() throws Exception {
//
//        Handler handler = new Handler<>(algo.getClass(), algo::Apply, algo::DownloadModel);
//        InputStream fakeIn = new ByteArrayInputStream(request.toString().getBytes());
//
//        System.setIn(fakeIn);
//        handler.serve();
//
//        byte[] fifoBytes = Files.readAllBytes(Paths.get(FIFOPIPE));
//        String rawData = new String(fifoBytes);
//        JsonObject actualResponse = parser.parse(rawData).getAsJsonObject();
//        Assert.assertEquals(expectedResponse.get("message"), actualResponse.get("message"));
//
//    }
//
//}
