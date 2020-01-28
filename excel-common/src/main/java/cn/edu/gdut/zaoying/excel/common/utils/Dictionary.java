package cn.edu.gdut.zaoying.excel.common.utils;

import java.util.HashMap;
import java.util.Map;

public interface Dictionary<K,V extends Enum> {

    interface KeyGenerator<K,V>{
        K generateKey(V value);
    }

    interface DefaultResult<K,V>{
        V returnDefault(K key);
    }


    Dictionary<K,V> keyGenerator(KeyGenerator<K, V> kKeyGenerator);
    Dictionary<K,V> whenNotFound(DefaultResult<K, V> defaultResult);

    V translate(K key);

    class StringDictionary<V extends Enum> implements Dictionary<String,V>{
        private V[] values;
        private volatile Map<String,V> dictionary;
        private KeyGenerator<String,V> stringKeyGenerator = Enum::name;
        private DefaultResult<String,V> defaultResult = key -> null;

        StringDictionary(V[] values) {
            this.values = values;
        }

        protected synchronized void initDict(){
            dictionary = new HashMap<>();
            for (V value : values) {
                String key = stringKeyGenerator.generateKey(value);
                dictionary.put(key, value);
            }
        }

        @Override
        public Dictionary<String, V> keyGenerator(KeyGenerator<String,V> stringKeyGenerator) {
            this.stringKeyGenerator = stringKeyGenerator;
            return this;
        }

        @Override
        public Dictionary<String, V> whenNotFound(DefaultResult<String, V> defaultResult) {
            this.defaultResult = defaultResult;
            return this;
        }

        @Override
        public V translate(String key) {
            if(dictionary == null){
                initDict();
            }
            return dictionary.getOrDefault(key, defaultResult.returnDefault(key));
        }
    }

    static <V extends Enum> Dictionary<String,V> stringDict(Class<V> clazz){
        V[] values = clazz.getEnumConstants();
        return new StringDictionary<>(values);
    }
}
