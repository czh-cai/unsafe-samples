package com.example.unsafe_test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @className: UnsafeWithPrivateConstructt
 * @author: czh
 * @date: 2023/5/9
 * @Description: Unsafe 使用例子:当你想要绕过对象构造方法、安全检查器或者没有public的构造方法时，allocateInstance()方法变得非常有用。
 * @version: 1.0.0
 **/
public class UnsafeWithPrivateConstructt {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        A o1 = new A(); // constructor
        System.out.println(o1.a()); //1

        A o2 = A.class.newInstance(); // reflection  通过反射引用
        System.out.println(o2.a()); //1

        Field f = null;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        A o3 = (A) unsafe.allocateInstance(A.class); // unsafe  allocateInstance()根本没有进入构造方法，在单例模式时，我们似乎看到了危机。
        System.out.println(o3.a()); //0  没有走构造方法所以为0，直接构建堆内存对象

    }

}

class A {
    private long a; // not initialized value
    public A() {
        this.a = 1; // initialization
    }
    public long a() { return this.a; }
}