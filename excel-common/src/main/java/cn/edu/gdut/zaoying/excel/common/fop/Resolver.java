package cn.edu.gdut.zaoying.excel.common.fop;

public interface Resolver<O> {
    void resolve(O output);
    void reject(Throwable throwable);

    class ResolverImpl<O> implements Resolver<O>{

        private Resolvable<O> resolvable;

        public ResolverImpl(Resolvable<O> resolvable) {
            this.resolvable = resolvable;
        }

        @Override
        public void resolve(O output) {
            resolvable.fulfill(output);
        }

        @Override
        public void reject(Throwable throwable) {
            resolvable.reject(throwable);
        }
    }

    static <O> Resolver<O> build(Resolvable<O> resolvable){
        return new ResolverImpl<>(resolvable);
    }
}
