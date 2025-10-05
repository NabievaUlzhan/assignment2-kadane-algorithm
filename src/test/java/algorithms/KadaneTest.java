package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class KadaneTest {
    @Test
    void classicExample() {
        int[] a = {-2,1,-3,4,-1,2,1,-5,4};
        var r = Kadane.maxSubarray(a, new PerformanceTracker());
        assertEquals(6, r.maxSum);
        assertEquals(3, r.start);
        assertEquals(6, r.end);
    }

    @Test
    void allNegative() {
        int[] a = {-8,-3,-6,-2,-5,-4};
        var r = Kadane.maxSubarray(a, new PerformanceTracker());
        assertEquals(-2, r.maxSum);
        assertEquals(3, r.start);
        assertEquals(3, r.end);
    }

    @Test
    void emptyArrayThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> Kadane.maxSubarray(new int[]{}, new PerformanceTracker()));
    }

    @Test
    void singlePositiveElement() {
        var r = Kadane.maxSubarray(new int[]{7}, new PerformanceTracker());
        assertEquals(7, r.maxSum);
        assertEquals(0, r.start);
        assertEquals(0, r.end);
    }

    @Test
    void singleNegativeElement() {
        var r = Kadane.maxSubarray(new int[]{-9}, new PerformanceTracker());
        assertEquals(-9, r.maxSum);
        assertEquals(0, r.start);
        assertEquals(0, r.end);
    }

    @Test
    void duplicatesPresent() {
        int[] a = {1,1,1,-1,1,1};
        var r = Kadane.maxSubarray(a, new PerformanceTracker());
        assertEquals(4, r.maxSum);
        assertEquals(0, r.start);
        assertEquals(5, r.end);
    }

    @Test
    void sortedAscendingAllPositive() {
        int[] a = {1,2,3,4};
        var r = Kadane.maxSubarray(a, new PerformanceTracker());
        assertEquals(10, r.maxSum);
        assertEquals(0, r.start);
        assertEquals(3, r.end);
    }

    @Test
    void reverseSortedWithDrop() {
        int[] a = {5,4,3,2,1,-10};
        var r = Kadane.maxSubarray(a, new PerformanceTracker());
        assertEquals(15, r.maxSum);
        assertEquals(0, r.start);
        assertEquals(4, r.end);
    }

    @Test
    void optimizedMatchesBaselineOnRandom() {
        Random rnd = new Random(321);
        for (int t=0; t<50; t++) {
            int n = 800;
            int[] a = new int[n];
            for (int i=0;i<n;i++) a[i] = rnd.nextInt(20001) - 10000;
            var base = Kadane.maxSubarray(a, new PerformanceTracker());
            var opt  = Kadane.maxSubarrayOptimizedLong(a).toIntResultSaturated();
            assertEquals(base.maxSum, opt.maxSum);
            assertEquals(base.start,  opt.start);
            assertEquals(base.end,    opt.end);
        }
    }

    @Test
    void optimizedIsNotSlowerThan2xOnAverage() {
        Random rnd = new Random(7);
        long baseSum=0, optSum=0;
        for (int t=0; t<15; t++) {
            int n = 20_000;
            int[] a = new int[n];
            for (int i=0;i<n;i++) a[i] = rnd.nextInt(200_001) - 100_000;
            long t0 = System.nanoTime();
            Kadane.maxSubarray(a, new PerformanceTracker());
            long t1 = System.nanoTime();
            Kadane.maxSubarrayOptimizedLong(a);
            long t2 = System.nanoTime();
            baseSum += (t1 - t0);
            optSum  += (t2 - t1);
        }
        System.out.printf("avg base=%.3f ms, opt=%.3f ms%n",
                baseSum/15.0/1e6, optSum/15.0/1e6);
        assertTrue(optSum <= 2 * baseSum, "optimized unexpectedly much slower");
    }
}