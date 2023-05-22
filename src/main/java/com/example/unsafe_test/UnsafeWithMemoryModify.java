package com.example.unsafe_test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static sun.misc.Unsafe.getUnsafe;

/**
 * @className: UnsafeWithMemoryModify
 * @author: czh
 * @date: 2023/5/10
 * @Description: 内存修改在c语言中是比较常见的，在Java中，可以用它绕过安全检查器。通过计算内存偏移，并使用putInt()方法，类的ACCESS_ALLOWED被修改
 * @version: 1.0.0
 **/
public class UnsafeWithMemoryModify {

    public static void mainWithException(String[] args) throws NoSuchFieldException {

        Guard guard = new Guard();
        guard.giveAccess(); // false, no access
        System.out.println( guard.giveAccess() );

        /*
            Exception in thread "main" java.lang.SecurityException: Unsafe
	                at sun.misc.Unsafe.getUnsafe(Unsafe.java:90)
	                at com.example.unsafe_test.UnsafeWithMemoryModify.main(UnsafeWithMemoryModify.java:25)
         */
        // bypass
        Unsafe unsafe = getUnsafe(); //非启动类会抛出异常
        Field f = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
        unsafe.putInt(guard, unsafe.objectFieldOffset(f), 42); // memory corruption
        guard.giveAccess(); // true, access granted
        System.out.println( guard.giveAccess() );
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {

        Guard guard = new Guard();
        guard.giveAccess(); // false, no access
        System.out.println( guard.giveAccess() );

        // bypass
        Field f = null;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        Field targetField = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
        //通过计算内存偏移，并使用putInt()方法，类的ACCESS_ALLOWED被修改 putInt(Object o, long offset, int x);
        //offset: 内存结构中针对整个对象的偏移量（比如某个属性）
        long field_offset = unsafe.objectFieldOffset(targetField);
        System.out.format("field_offset(ACCESS_ALLOWED):%d \n",field_offset);
        unsafe.putInt(guard, unsafe.objectFieldOffset(targetField), 42); // memory corruption
        guard.giveAccess(); // true, access granted
        System.out.println( guard.giveAccess() );
    }



}

class Guard {
    private int ACCESS_ALLOWED = 1;

    public boolean giveAccess() {
        return 42 == ACCESS_ALLOWED;
    }
}