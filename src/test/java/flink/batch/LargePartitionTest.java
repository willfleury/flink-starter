package flink.batch;


import static java.util.stream.Collectors.joining;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.junit.Before;
import org.junit.Test;

public class LargePartitionTest extends BaseFlinkTest {

    private ParameterTool params;
    private String path;

    @Before
    public void preSubmit() throws Exception {
        path = getTempDirPath("job");

        String barePath = new URI(path).getPath();

        FileUtils.writeStringToFile(new File(barePath + "/input/file.txt"), "testinging hellooo\n");
        FileUtils.writeStringToFile(new File(barePath + "/test/out.txt"), "testinging hellooo\n");

        params = ParameterTool.fromMap(new ImmutableMap.Builder<String, String>()
                .put("input", path + "/input/file.txt")
                .put("output", path + "/output")
                .build());
    }

    @Test
    public void testProgram() throws Exception {
        new LargePartition(getEnv()).run(params);

        List<String> results = new ArrayList<>();
        readAllResultLines(results, path + "/test/out.txt");
        String expectedStr = results.stream().collect(joining("\n"));

        compareResultsByLinesInMemory(expectedStr, params.get("output"));
    }
}
