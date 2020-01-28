package cn.edu.gdut.zaoying.excel.common.utils;

import cn.edu.gdut.zaoying.excel.common.fop.Function;

import java.util.concurrent.atomic.AtomicReference;

public class ThreadUtil {
    public static Thread run(Runnable runnable){
        return new Thread(runnable);
    }

    public static void start(Runnable runnable){
        run(runnable).start();
    }

    public static <I,O> Function.VoidFunction<Function.VoidFunction<O>> start(Function<I,O> function, I input){

        return callback -> run(() ->callback.call(function.call(input))).start();
    }

    public static <I,O> O await(Function<I,O> function, I input){

        AtomicReference<O> output = new AtomicReference<>();

        join(() -> output.set(function.call(input)));

        return output.get();
    }

    public static void join(Runnable runnable){
        join(0).call(runnable);
    }

    public static Function.VoidFunction<Runnable> join(long millis){
        return runnable -> {
            Thread sub = run(runnable);
            sub.start();
            try {
                sub.join(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    public static void sleep(Runnable runnable){
        sleep(0).call(runnable);
    }

    public static Function.VoidFunction<Runnable> sleep(long millis){
        return runnable -> start(() ->{
            try {
                Thread.sleep(millis);
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
