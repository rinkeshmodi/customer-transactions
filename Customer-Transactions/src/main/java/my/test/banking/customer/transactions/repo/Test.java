package my.test.banking.customer.transactions.repo;

import java.util.concurrent.atomic.AtomicLong;

public class Test {

    public static void main(String[] args) {
        AtomicLong atomicLong = new AtomicLong(10);
        long expect = atomicLong.longValue();
        System.out.println(atomicLong.compareAndSet(expect, expect + 1));

    }
}
