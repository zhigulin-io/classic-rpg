package ru.drifles.crpg.jmh;

/*@BenchmarkMode(Mode.AverageTime)
@Fork(value = 4, warmups = 2)
@Warmup(iterations = 2, batchSize = 1, time = 1)
@Measurement(iterations = 4, batchSize = 1, time = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)*/
public class MidLandBenchmark {
    /*private static final Land emptyLand;
    private static final Land fullLand;
    private static final Land semiLand;

    private static final Position source = new Position(0, 0);
    private static final Position destination = new Position(63, 63);

    static {
        try {
            emptyLand = new Land("/level_mid_1.world");
            fullLand = new Land("/level_mid_2.world");
            semiLand = new Land("/level_mid_3.world");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }



    @Benchmark
    public void emptyBFS() {
        emptyLand.routeBFS(source, destination);
    }

    @Benchmark
    public void semiBFS() {
        semiLand.routeBFS(source, destination);
    }

    @Benchmark
    public void fullBFS() {
        fullLand.routeBFS(source, destination);
    }



    @Benchmark
    public void emptyDFS() {
        emptyLand.routeDFS(source, destination);
    }

    @Benchmark
    public void semiDFS() {
        semiLand.routeDFS(source, destination);
    }

    @Benchmark
    public void fullDFS() {
        fullLand.routeDFS(source, destination);
    }



    @Benchmark
    public void emptyDijkstra() {
        emptyLand.routeDijkstra(source, destination);
    }

    @Benchmark
    public void semiDijkstra() {
        semiLand.routeDijkstra(source, destination);
    }

    @Benchmark
    public void fullDijkstra() {
        fullLand.routeDijkstra(source, destination);
    }



    @Benchmark
    public void emptyAStar() {
        emptyLand.routeAStar(source, destination);
    }

    @Benchmark
    public void semiAStar() {
        semiLand.routeAStar(source, destination);
    }

    @Benchmark
    public void fullAStar() {
        fullLand.routeAStar(source, destination);
    }



    @Benchmark
    public void emptyCustom() {
        emptyLand.routeCustom(source, destination);
    }

    @Benchmark
    public void semiCustom() {
        semiLand.routeCustom(source, destination);
    }

    @Benchmark
    public  void fullCustom() {
        fullLand.routeCustom(source, destination);
    }*/
}
