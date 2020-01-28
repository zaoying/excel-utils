package cn.edu.gdut.zaoying.excel.common.utils;

import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public interface Configurable<T> {

    static <T> Configurable<T> from(Class<T> clazz){
        return new ConfigurableImpl<>(clazz);
    }

    static <T> Configurable<T> from(T instance){
        return new ConfigurableImpl<>(instance);
    }

    interface ContextMapBuilder {
        ContextMap build();
    }

    class ConfigurableImpl<T> implements Configurable<T>{

        private ContextMap contextMap;

        private Class<T> clazz;

        private T instance;

        private ContextMapBuilder contextMapBuilder = ContextMap::order;

        public ConfigurableImpl(T instance) {
            this.instance = instance;
        }

        ConfigurableImpl(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ContextMap getContextMap(){
            if(contextMap == null){
                contextMap = contextMapBuilder.build();
            }
            return contextMap;
        }

        @Override
        public Configurable<T> config(String property, Object value) {
            getContextMap().put(property, value);
            return this;
        }

        @Override
        public Configurable<T> config(Object value) {
            Class<?> clazz = value.getClass();
            String simpleName = clazz.getSimpleName();
            String fieldName = StringUtil.firstLetter2LowerCase(simpleName);
            getContextMap().put(fieldName, value);
            return this;
        }

        @Override
        public Configurable<T> callMethod(String methodName, Object... args) {
            getContextMap().put(methodName, args);
            return this;
        }

        @Override
        public Configurable<T> config(Acceptable<ContextMap> contextMapAcceptable) {
            contextMapAcceptable.accept(getContextMap());
            return this;
        }

        @Override
        public Configurable<T> configAll(Class<?> clazz) {
            MethodAccess methodAccess = ClassUtil.getMethodAccessFromCache(clazz);

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                int modifier = method.getModifiers();
                if (Modifier.isStatic(modifier)) {
                    int paramsCount = method.getParameterCount();
                    if(paramsCount == 0){
                        String methodName = method.getName();
                        int methodIndex = methodAccess.getIndex(methodName);
                        Object object = methodAccess.invoke(null, methodIndex);
                        getContextMap().put(methodName, object);
                    }
                }
            }
            return this;
        }

        @Override
        public Configurable<T> configAll(ContextMap contextMap) {
            getContextMap().merge(contextMap);
            return this;
        }

        @Override
        public Configurable<T> buildContextMap(ContextMapBuilder contextMapBuilder) {
            this.contextMapBuilder = contextMapBuilder;
            return this;
        }

        @Override
        public T build() throws IllegalAccessException, InstantiationException {
            if(instance == null){
                instance = clazz.newInstance();
            }
            getContextMap().getKeyObjectMap().forEach((key,value) -> {
                if(value != null){
                    if(value.getClass().isArray()){
                        Object[] args = (Object[]) value;
                        String methodName = key.getIdentifier();
                        ClassUtil.callMethod(instance, methodName, args.length == 1 ? args[0] : args);
                    }
                    else {
                        String fieldName = key.getIdentifier();
                        ClassUtil.setField(instance, fieldName, value);
                    }
                }
            });
            return instance;
        }
    }

    Configurable<T> config(String property, Object value);

    Configurable<T> config(Object value);

    Configurable<T> callMethod(String methodName, Object... args);

    Configurable<T> config(Acceptable<ContextMap> contextMapAcceptable);

    Configurable<T> configAll(ContextMap contextMap);

    Configurable<T> configAll(Class<?> clazz);

    Configurable<T> buildContextMap(ContextMapBuilder contextMapBuilder);

    T build() throws IllegalAccessException, InstantiationException;
}
