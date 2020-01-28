package cn.edu.gdut.zaoying.excel.common.fop;

import cn.edu.gdut.zaoying.excel.common.utils.ThreadUtil;

public interface Promise<I,O> {

    <N> Promise<O,N> then(Function<O, N> onFulfilled);

    Promise<I,O> error(Function.VoidFunction<Throwable> throwable);

    enum Status {
        PENDING,
        FULFILLED,
        REJECTED
    }

    class PromiseImpl<I,O> implements Promise<I,O>,Resolvable<O>{

        private Function.VoidFunction<Throwable> onFailed;

        private Function<O,?> onFulfilled;

        private Resolver resolver;

        private Status status = Status.PENDING;

        private O output;

        private Throwable reason;

        PromiseImpl(Function.VoidFunction<Throwable> onFailed) {
            this.onFailed = onFailed;
        }

        void execute(O output){
            Runnable runnable = ()->{
                try {
                    Object next = onFulfilled.call(output);
                    resolver.resolve(next);
                }
                catch (Throwable throwable){
                    reject(throwable);
                }
            };

            //防止耗时操作阻塞当前线程
            //avoid the onFulfilled function do so much thing that makes current thread stuck
            if(onFulfilled instanceof Thenable){
                ThreadUtil.start(runnable);
            }
            else runnable.run();
        }

        @Override
        public void fulfill(O output) {
            this.status = Status.FULFILLED;
            this.output = output;
            if(onFulfilled != null){
                execute(output);
            }
        }

        @Override
        public void reject(Throwable reason) {
            this.status = Status.REJECTED;
            this.reason = reason;
            if ((onFailed != null)) {
                onFailed.call(reason);
            }
        }

        @Override
        public <N> Promise<O,N> then(Function<O,N> onFulfilled) {

            this.onFulfilled = onFulfilled;

            //share the onFailed callback to every promise
            PromiseImpl<O,N> next = new PromiseImpl<>(onFailed);

            resolver = Resolver.build(next);

            if(status == Status.FULFILLED){
                execute(output);
            }

            return next;
        }

        @Override
        public Promise<I,O> error(Function.VoidFunction<Throwable> onFailed) {

            this.onFailed = onFailed;

            if(status == Status.REJECTED){
                onFailed.call(reason);
            }

            return this;
        }
    }

    static <O> Promise<?,O> just(O output){
        return call(input -> input.resolve(output));
    }

    static <O> Promise<?,O> call(Function.VoidFunction<Resolver<O>> function){

        PromiseImpl<?,O> promise = new PromiseImpl<>(Function.printError());

        Resolver<O> resolver = Resolver.build(promise);

        function.call(resolver);

        return promise;
    }
}
