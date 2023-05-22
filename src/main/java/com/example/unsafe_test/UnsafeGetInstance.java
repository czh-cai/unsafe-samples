package com.example.unsafe_test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @className: UnsafeGetInstance
 * @author: czh
 * @date: 2023/5/13
 * @Description: 获取Unsafe 对象
 * @version: 1.0.0
 **/
public class UnsafeGetInstance {

    /**************************************Unsafe 获取实例部分代码 **************************************
     // 构造器被私有化
     private Unsafe() { }

     private static final Unsafe theUnsafe = new Unsafe();

     @CallerSensitive //jvm 校验标记 必须由 bootclass 类加载器进行加载
     public static Unsafe getUnsafe() {
            Class<?> caller = Reflection.getCallerClass();
            if (!VM.isSystemDomainLoader(caller.getClassLoader()))
            throw new SecurityException("Unsafe");
        return theUnsafe;
     }
     ****************************************************************************************************/
    /***************************************@CallerSensitive 方法,调用加载说明******************************
     vmOptions: -Xbootclasspath/a: xxx\xxx\target\classes
     执行前jvm 添加参数 -Xbootclasspath/a:D:\workspace\demo\target\classes

    -Xbootclasspath:bootclasspath ：让jvm从指定的路径中加载bootclass，用来替换jdk的rt.jar。一般不会用到。
                -Xbootclasspath/a: path ： 被指定的文件追加到默认的bootstrap路径中。
                -Xbootclasspath/p: path ： 让jvm优先于默认的bootstrap去加载path中指定的class。
    /****************************************************************************************************/


    public static void main_1(String[] args) {
        Unsafe unsafe = Unsafe.getUnsafe();
    }

    public static void main(String[] args) {
        //Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");//也可以这样,作用相同
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        // 将字段的访问权限设置为true
        unsafeField.setAccessible(true);
        //因为theUnsafe字段在Unsafe类中是一个静态字段，所以通过Field.get()获取字段值时，可以传null获取
        try {
            Unsafe unsafe = (Unsafe) unsafeField.get(null);
            System.out.println(unsafe);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
