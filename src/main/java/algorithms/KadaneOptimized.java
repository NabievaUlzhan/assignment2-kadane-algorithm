package algorithms;

import metrics.PerformanceTracker;

public final class KadaneOptimized {
    private KadaneOptimized() {}

    public static final class ResultLong {
        public final long maxSum;
        public final int start, end;
        public ResultLong(long maxSum, int start, int end) {
            this.maxSum = maxSum; this.start = start; this.end = end;
        }
        public Kadane.Result toIntResultSaturated() {
            long s = maxSum;
            int si = (s > Integer.MAX_VALUE) ? Integer.MAX_VALUE
                    : (s < Integer.MIN_VALUE) ? Integer.MIN_VALUE
                    : (int) s;
            return new Kadane.Result(si, start, end);
        }
        @Override public String toString() { return "sum=" + maxSum + " [" + start + "," + end + "]"; }
    }

    public static ResultLong maxSubarrayLong(int[] a) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("array is empty");
        long cur = a[0], best = a[0];
        int curStart = 0, bestL = 0, bestR = 0;

        for (int i = 1, n = a.length; i < n; i++) {
            int v = a[i];
            long extend = cur + v;
            if (extend < v) {
                cur = v;
                curStart = i;
            } else {
                cur = extend;
            }
            if (cur > best) {
                best = cur;
                bestL = curStart;
                bestR = i;
            }
        }
        return new ResultLong(best, bestL, bestR);
    }

    public static ResultLong maxSubarrayLong(int[] a, PerformanceTracker p) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("array is empty");
        long cur = a[0], best = a[0];
        int curStart = 0, bestL = 0, bestR = 0;
        if (p != null) p.arrayAccesses++;

        for (int i = 1, n = a.length; i < n; i++) {
            int v = a[i];
            if (p != null) p.arrayAccesses++;
            long extend = cur + v;
            if (p != null) p.comparisons++;
            if (extend < v) { cur = v; curStart = i; }
            else { cur = extend; }
            if (cur > best) { best = cur; bestL = curStart; bestR = i; }
        }
        return new ResultLong(best, bestL, bestR);
    }

    public static Kadane.Result maxSubarrayInt(int[] a) {
        return maxSubarrayLong(a).toIntResultSaturated();
    }
}
