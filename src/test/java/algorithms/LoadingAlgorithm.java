package algorithms;

import com.algorithmia.algorithm.Required;

import java.util.HashMap;


public class LoadingAlgorithm {

    public class AlgoInput {
        @Required
        String name;
        @Required
        Integer age;

        public AlgoInput(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    public String Apply(AlgoInput input, HashMap<String, String> context) throws RuntimeException {
        if (context != null && context.containsKey("local_file")) {
            return "Hello " + input.name + " you are " + input.age +
                    " years old, and your model file is downloaded here " + context.get("local_file");
        }
        throw new RuntimeException("We need to serve DownloadModel before Apply.");
    }

    public HashMap<String, String> DownloadModel() throws RuntimeException {
        HashMap<String, String> context = new HashMap<>();
        context.put("local_file", "/tmp/somefile");
        return context;
    }
}