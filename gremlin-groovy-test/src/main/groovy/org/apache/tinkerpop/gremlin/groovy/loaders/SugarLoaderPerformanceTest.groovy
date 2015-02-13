package org.apache.tinkerpop.gremlin.groovy.loaders

import com.carrotsearch.junitbenchmarks.BenchmarkOptions
import com.carrotsearch.junitbenchmarks.BenchmarkRule
import com.carrotsearch.junitbenchmarks.annotation.AxisRange
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart
import com.carrotsearch.junitbenchmarks.annotation.LabelType
import org.apache.tinkerpop.gremlin.AbstractGremlinTest
import org.apache.tinkerpop.gremlin.LoadGraphWith
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runners.MethodSorters

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
@AxisRange(min = 0d, max = 1d)
@BenchmarkMethodChart(filePrefix = "sugar")
@BenchmarkHistoryChart(labelWith = LabelType.CUSTOM_KEY, maxRuns = 20, filePrefix = "hx-sugar")
@FixMethodOrder(MethodSorters.JVM)
class SugarLoaderPerformanceTest extends AbstractGremlinTest {
    @Rule
    public TestRule benchmarkRun = new BenchmarkRule()

    public final static int DEFAULT_BENCHMARK_ROUNDS = 1000
    public final static int DEFAULT_WARMUP_ROUNDS = 50

    static {
        SugarLoader.load()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void java_g_V() throws Exception {
        g.V().iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void groovy_g_V() throws Exception {
        g.V.iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void java_g_V_outE_inV() throws Exception {
        g.V().outE().inV().iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void groovy_g_V_outE_inV() throws Exception {
        g.V.outE.inV.iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void java_g_V_outE_inV_outE_inV() throws Exception {
        g.V().outE().inV().outE().inV().iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void groovy_g_V_outE_inV_outE_inV() throws Exception {
        g.V.outE.inV.outE.inV.iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void java_g_V_name() throws Exception {
        g.V().values("name").iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void groovy_g_V_name() throws Exception {
        g.V.name.iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void java_g_VX1X_name() throws Exception {
        g.V(1).values("name").iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void groovy_g_VX1X_name() throws Exception {
        g.V(1).name.iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void java_g_VX1X_outE() throws Exception {
        g.V(1).outE().iterate()
    }

    @BenchmarkOptions(benchmarkRounds = SugarLoaderPerformanceTest.DEFAULT_BENCHMARK_ROUNDS, warmupRounds = SugarLoaderPerformanceTest.DEFAULT_WARMUP_ROUNDS, concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
    @LoadGraphWith(LoadGraphWith.GraphData.MODERN)
    @Test
    public void groovy_g_VX1X_outE() throws Exception {
        g.V(1).outE.iterate()
    }
}
