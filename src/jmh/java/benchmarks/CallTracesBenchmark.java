package benchmarks;

import com.github.kornilova_l.flamegraph.plugin.server.trees.converters.FlamegraphToCallTracesConverter;
import com.github.kornilova_l.flamegraph.plugin.server.trees.converters.flamegraph_format_trees.TreesSetImpl;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class CallTracesBenchmark {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void buildTree() {
        new FlamegraphToCallTracesConverter().convert(new File("src/jmh/resources/idea.jfr"));
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new FlamegraphToCallTracesConverter().convert(new File("src/jmh/resources/idea.jfr"));
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) / 1000 + "s");
    }
}
