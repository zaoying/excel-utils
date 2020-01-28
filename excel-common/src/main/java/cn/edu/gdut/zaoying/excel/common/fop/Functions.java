package cn.edu.gdut.zaoying.excel.common.fop;

public interface Functions<A,B> {

    <C> Functions<A,C> next(Function<B, C> next);

    <C> Function<A,C> last(Function<B, C> next);

    static <A,B> Functions<A,B> first(Function<A, B> first){
        return new Functions<A,B>() {
            @Override
            public <C> Functions<A,C> next(Function<B,C> next) {
                Function<A,C> composition = compose(first, next);
                return first(composition);
            }

            @Override
            public <C> Function<A, C> last(Function<B, C> next) {
                return compose(first, next);
            }
        };
    }

    static <A,B,C> Function<A,C> compose(Function<A, B> first, Function<B, C> second){
        return input -> second.call(first.call(input));
    }
}
