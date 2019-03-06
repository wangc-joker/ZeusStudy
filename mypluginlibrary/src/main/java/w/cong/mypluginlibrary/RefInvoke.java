package w.cong.mypluginlibrary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RefInvoke {

    public static Object createObject(String className) {
        try {
            Class r = Class.forName(className);
            return createObject(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createObject(Class clazz) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};
        try {
            return createObject(clazz,pareTypes,pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createObject(String className, Class pareType, Object pareValue) {
        try {
            Class r = Class.forName(className);
            return createObject(r,pareType,pareValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createObject(Class clazz, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        try {
            return createObject(clazz,pareTypes,pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createObject(String className, Class[] pareTypes, Object[] pareValues) {
        try {
            Class r  = Class.forName(className);
            return createObject(r,pareTypes,pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createObject(Class clazz, Class[] pareTypes, Object[] pareValues) {
        try {
            Constructor ctor = clazz.getDeclaredConstructor(pareTypes);
            ctor.setAccessible(true);
            return ctor.newInstance(pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] paretypes
    , Object[] pareValues) {
        if (obj == null) {
            return null;
        }
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName,paretypes);
            method.setAccessible(true);
            return method.invoke(obj,pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(String className, String methodName
    , Class[] pareTypes, Object[] pareValues) {
        try {
            Class c = Class.forName(className);
            return invokeStaticMethod(c,methodName,pareTypes,pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(Class c, String methodName
            , Class[] pareTypes, Object[] pareValues) {
        try {
            Method method = c.getDeclaredMethod(methodName,pareTypes);
            method.setAccessible(true);
            return method.invoke(null,pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getStaticFieldObject(String className, String fieldName) {
        return getFieldObject(className,null,fieldName);
    }

    public static Object getStaticFieldObject(Class clazz, String fieldName) {
        return getFieldObject(clazz,null,fieldName);
    }

    public static void setStaticFieldObject(String className, String fieldName, Object fieldValue) {
        setFieldObject(className,null,fieldName,fieldValue);
    }

    public static void setStaticFieldObject(Class clazz, String fieldName, Object fieldValue) {
        setFieldObject(clazz,null,fieldName,fieldValue);
    }

    public static Object getFieldObject(String className, Object object, String fieldName) {
        try {
            Class r = Class.forName(className);
            return getFieldObject(r,object,fieldName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldObject(Class clazz, Object object, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldObject(String className, Object object, String fieldName, Object fieldValue) {
        try {
            Class r = Class.forName(className);
            setFieldObject(r,object,fieldName,fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFieldObject(Class clazz, Object object, String fieldName, Object fieldValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object,fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getFieldObject(Object obj, String fieldName) {
        return getFieldObject(obj.getClass(), obj, fieldName);
    }

    public static void setFieldObject(Object obj, String filedName, Object fieldValue) {
        setFieldObject(obj.getClass(),obj,filedName,fieldValue);
    }
}
