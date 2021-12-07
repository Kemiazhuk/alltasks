package com.epam.rd.autotasks;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadUnionIml implements ThreadUnion {
    private final String name;
    private final CopyOnWriteArrayList<Thread> allStartingThreads = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<FinishedThreadResult> threadResults = new CopyOnWriteArrayList<>();
    private boolean flaForShutdown = false;

    public ThreadUnionIml(String name) {
        this.name = name;
    }

    @Override
    public int totalSize() {
        return allStartingThreads.size();
    }

    @Override
    public int activeSize() {
        int counterActiveThreads = 0;
        for (Thread thread : allStartingThreads) {
            if (thread.isAlive()) {
                counterActiveThreads++;
            }
        }
        return counterActiveThreads;
    }

    @Override
    public void shutdown() {
        for (Thread thread : allStartingThreads) {
            thread.interrupt();
        }
        flaForShutdown = true;
    }

    @Override
    public boolean isShutdown() {
        return flaForShutdown;
    }

    @Override
    public void awaitTermination() {
        while (activeSize() != 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return activeSize() == 0 && isShutdown();
    }

    @Override
    public List<FinishedThreadResult> results() {
        return threadResults;
    }

    @Override
    public synchronized Thread newThread(Runnable r) {
        if (flaForShutdown) {
            throw new IllegalStateException();
        }
        Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                threadResults.add(new FinishedThreadResult(t.getName(), e));
            }
        };
        Thread thread = new Thread(r) {
            public synchronized void start() {
                super.start();
            }

            public void run() {
                super.run();
                threadResults.add(new FinishedThreadResult(this.getName()));

            }
        };
        thread.setUncaughtExceptionHandler(h);
        thread.setName(name + "-worker-" + allStartingThreads.size());
        allStartingThreads.add(thread);
        return thread;
    }
}
