package cn.edu.gdut.zaoying.excel.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ContextMap {

    private Map<Key<?>, Object> keyObjectMap;

    private ContextMap(Map<Key<?>, Object> keyObjectMap) {
        this.keyObjectMap = keyObjectMap;
    }

    public static ContextMap normal(){
        return new ContextMap(new HashMap<>());
    }

    public static ContextMap order(){
        return new ContextMap(new LinkedHashMap<>());
    }

    public static ContextMap concurrent(){
        return new ContextMap(new ConcurrentHashMap<>());
    }

    public static ContextMap customize(Map<Key<?>, Object> keyObjectMap){
        return new ContextMap(keyObjectMap);
    }

    public interface Key<T>{

        static <T> Key<T> getKey(String identifier, Class<T> type){
            return new KeyImpl<>(identifier, type);
        }
        static <T> Key<T> getKey(String identifier, T value){
            Class<T> type = ClassUtil.getActualType(value);
            return new KeyImpl<>(identifier, type);
        }

        T cast(Object object);
        String getIdentifier();
        Class<T> getType();
    }

    public static class KeyImpl<T> implements Key<T>{
        private final String identifier;
        private final Class<T> type;

        public KeyImpl(String identifier, Class<T> type) {
            this.identifier = identifier;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key<?> key = (Key<?>) o;
            return Objects.equals(identifier, key.getIdentifier()) &&
                    Objects.equals(type, key.getType());
        }

        @Override
        public int hashCode() {
            return Objects.hash(identifier, type);
        }

        @Override
        public T cast(Object o){
            return type.cast(o);
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public Class<T> getType() {
            return type;
        }
    }

    public void merge(ContextMap contextMap){
        keyObjectMap.putAll(contextMap.getKeyObjectMap());
    }

    public <T> void put(Key<T> key, T value){
        keyObjectMap.put(key, value);
    }

    public <T> Key<T> put(String identifier,T value){
        Key<T> key = Key.getKey(identifier,value);
        keyObjectMap.put(key,key.cast(value));
        return key;
    }

    public <T> T get(Key<T> key) throws ClassCastException{
        return key.cast(keyObjectMap.get(key));
    }

    public Map<Key<?>, Object> getKeyObjectMap() {
        return keyObjectMap;
    }
}
