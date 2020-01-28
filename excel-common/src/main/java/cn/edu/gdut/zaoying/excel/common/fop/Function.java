package cn.edu.gdut.zaoying.excel.common.fop;

public interface Function<I,O> {
    /**
     * 函数 function
     * @param input 输入参数 function' input parameter
     * @return O 返回值的类型 returned value's type
     */
    O call(I input);

    /**
     * 返回值为void的函数
     * function which returns void
     * @param <I> 函数的参数类型 function's parameter type
     */
    interface VoidFunction<I>{
        void call(I input);
    }

    /**
     * 将返回结果为void的函数转换为返回结果为Void的函数
     * turn the function which returns a void into a function returns Void
     * @param  voidFunction 返回值为void的函数 function which returns void
     * @param <I> 函数的参数 function's parameter type
     * @return 返回值为Void的函数 function which returns Void
     */
    static <I> Function<I,Void> avoid(VoidFunction<I> voidFunction){
        return input -> {
            voidFunction.call(input);
            return null;
        };
    }

    /**
     * 用Thenable把一个函数包装成异步函数，use thenable to wrap a async function
     * @param function 被包装的函数 wrapped function
     * @param <I> 输入参数 input
     * @param <O> 返回参数 output
     * @return Thenable
     */
    static <I,O> Thenable<I,O> async(Function<I, O> function){
        return function::call;
    }

    /**
     * 将错误输入到标准输出，例如：命令控制台
     * prints error to standard output, like console etc.
     * @return 没有返回结果 nothing
     */
    static VoidFunction<Throwable> printError(){
        return Throwable::printStackTrace;
    }
}
