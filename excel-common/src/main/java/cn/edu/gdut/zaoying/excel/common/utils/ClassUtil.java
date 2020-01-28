package cn.edu.gdut.zaoying.excel.common.utils;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassUtil {

    private final static Map<Class,FieldAccess> fieldAccessMap = new ConcurrentHashMap<>();

    private final static Map<Class, MethodAccess> methodAccessMap = new ConcurrentHashMap<>();

    public static <T> Class<T> getActualType(T instance) throws ClassCastException{
        return (Class<T>) instance.getClass();
    }

    public static <T> Type[] getActualTypeArguments(T instance, Class<T> actualInterface){
        Class clazz = instance.getClass();
        Type genericType = null;
        if(actualInterface != null && actualInterface.isInterface()){
            Type[] types = clazz.getGenericInterfaces();
            for (Type type : types) {
                if (type == actualInterface){
                    genericType = type;
                }
            }
        }
        else {
            genericType = clazz.getGenericSuperclass();
        }

        if(genericType instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{};
    }

    public static <T> FieldAccess getFieldAccessFromCache(Class<T> clazz){

        FieldAccess fieldAccess = fieldAccessMap.get(clazz);
        if(fieldAccess == null){
            fieldAccess = FieldAccess.get(clazz);
            fieldAccessMap.put(clazz, fieldAccess);
        }

        return fieldAccess;
    }

    public static <T> FieldAccess getFieldAccessFromCache(T instance){
        Class<T> clazz = getActualType(instance);
        return getFieldAccessFromCache(clazz);
    }

    public static <T> MethodAccess getMethodAccessFromCache(Class<T> clazz){

        MethodAccess methodAccess = methodAccessMap.get(clazz);
        if(methodAccess == null){
            methodAccess = MethodAccess.get(clazz);
            methodAccessMap.put(clazz, methodAccess);
        }

        return methodAccess;
    }

    public static <T> MethodAccess getMethodAccessFromCache(T instance){
        Class<T> clazz = getActualType(instance);
        return getMethodAccessFromCache(clazz);
    }

    public static <T> void setFields(T instance, Map<String,Object> stringObjectMap){

        FieldAccess fieldAccess = getFieldAccessFromCache(instance);

        stringObjectMap.forEach((key,value) -> {
            int fieldIndex = fieldAccess.getIndex(key);
            fieldAccess.set(instance, fieldIndex, value);
        });
    }

    public static <T> void setField(T instance, String fieldName, Object value){

        FieldAccess fieldAccess = getFieldAccessFromCache(instance);

        int fieldIndex = fieldAccess.getIndex(fieldName);

        fieldAccess.set(instance, fieldIndex, value);
    }

    public static <T> Object getField(T instance, String fieldName){

        FieldAccess fieldAccess = getFieldAccessFromCache(instance);

        int fieldIndex = fieldAccess.getIndex(fieldName);

        return fieldAccess.get(instance, fieldIndex);
    }

    public static <T> Object callMethod(T instance, String methodName, Object... args){

        MethodAccess methodAccess = getMethodAccessFromCache(instance);

        int methodIndex = methodAccess.getIndex(methodName);

        return methodAccess.invoke(instance, methodIndex, args.length == 1 ? args[0] : args);
    }
}
