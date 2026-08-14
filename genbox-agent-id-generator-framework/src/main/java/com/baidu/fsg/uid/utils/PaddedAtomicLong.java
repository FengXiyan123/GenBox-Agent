package com.baidu.fsg.uid.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 工具类。
 */
public class PaddedAtomicLong extends AtomicLong {
    private static final long serialVersionUID = -3415778863941386253L;

    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public PaddedAtomicLong() {
        super();
    }

    public PaddedAtomicLong(long initialValue) {
        super(initialValue);
    }

    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }

}
