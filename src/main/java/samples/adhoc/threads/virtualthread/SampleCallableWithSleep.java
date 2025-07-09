package samples.adhoc.threads.virtualthread;

import java.util.concurrent.Callable;

public class SampleCallableWithSleep implements Callable<String> {

    private String name;

    public SampleCallableWithSleep(String name) {
        this.name = name;

    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1);
        return this.name + "_" + Math.random();
    }
}