/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.stratford.rbandlar8760.rssfeeds;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author RamyaNari
 */
public class WorkQueue {

    private final ExecutorService threadPool;

  
    public WorkQueue() {
        final int processors = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(processors);
       // threadPool = Executors.newFixedThreadPool(1);
     }

  
    public WorkQueue(final int nThreads) {
        threadPool = Executors.newFixedThreadPool(nThreads);
    }

  
    public WorkQueue(final float multiplier) {
        final int processors = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(new Double(Math.floor(processors * multiplier)).intValue());
    }

  
    public void shutdown() {
        threadPool.shutdown();
    }

  
    @SuppressWarnings("unchecked")
    public void execute(final Runnable r) {
        threadPool.execute(r);
    }
}
