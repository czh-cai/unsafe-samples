package com.example.unsafe_test;

import lombok.*;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

/**
 * @className: UnsafeTest

 * @author: czh
 * @date: 2023/5/13
 * @Description: unsafe 使用场景测试
 * @version: 1.0.0
 **/
public class UnsafeMainTest {

    public static void main(String[] args) throws NoSuchFieldException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        //get Unsafe Instance
        Unsafe unsafe = getUnsafeInstance();
//        //1.Object field operate
//        ObjectFieldOperate( unsafe );
//        //2.byte operate
//        byteOperate( unsafe );
//        //3.Unsafe address physical memory operate
//        addressOperate( unsafe );
//        //4.Unsafe physical memory operate
//        memoryOperate( unsafe );
//        //5.Unsafe static field operate
//        staticFieldOperate( unsafe );
//        //6.Unsafe class operate
//        classOperate( unsafe );
        //7.Unsafe thread operate
        //threadOperate( unsafe );
        //8.Unsafe cpu system loadavg monitor
        //cpuSystemLoadavgMonitor( unsafe );
        //9.Unsafe synchronize monitor
        //synchronizeMonitorOperate( unsafe );
        //10.Unsafe cas (compareAndSwap)
        //compareAndSwapOperate( unsafe );
        //11.Unsafe volatile get put operate
        //volatileOperate( unsafe );
        //12.Unsafe fence operate
        //fenceOperate( unsafe );
        //13.Unsafe memory barrier operate
        //memoryBarrierOperate( unsafe );
        //14.Unsafe array operate
        arrayOperate( unsafe );
    }



    public static Unsafe getUnsafeInstance() {
        System.out.println("\n");
        System.out.println("...........................................Unsafe instance operate begin............................");
        Unsafe unsafe = null;
        //Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");//也可以这样,作用相同
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        try {
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        System.out.format("Unsafe instance operate:    获取Unsafe 实例:[ %s ] \n", unsafe.toString() );
        System.out.println("...........................................Unsafe instance operate end..............................");
        System.out.println("\n");
        return unsafe;
    }

    public static void ObjectFieldOperate(Unsafe unsafe) throws NoSuchFieldException {
        System.out.println("\n");
        System.out.println("...........................................Unsafe object field operate begin........................");
        System.out.format("Unsafe object field operate get method:    [%s] \n", "getInt getBoolean getByte getShort getChar getLong getFloat getDouble getObject");
        System.out.format("Unsafe object field operate put method:    [%s] \n", "putInt putBoolean putByte putShort putChar putLong putFloat putDouble putObject");
        User user = new User();

        //1.1 计算相对object address的offset
        long fieldOffset = unsafe.objectFieldOffset(User.class.getDeclaredField("age"));
        System.out.format("Unsafe object field operate:    object(age) offset:[%d] \n", fieldOffset);

        //1.2 设置age为20 putObject(Object o, long offset, Object x)
        unsafe.putObject(user,fieldOffset,20);
        System.out.format("Unsafe object field operate:    get object field by getAge() age:[%d] \n", user.getAge());
        //1.3 获取age属性 getObject(Object o, long offset);
        System.out.format("Unsafe object field operate:    get object field by unsafe.getObject(Object o, long offset) age:[%d] \n", unsafe.getObject( user, fieldOffset));

        System.out.println("...........................................Unsafe object field operate end..........................");
        System.out.println("\n");

    }

    public static void byteOperate(Unsafe unsafe) {
        System.out.println("\n");
        System.out.println("...........................................Unsafe byte memory operate begin................................");
        System.out.format("Unsafe memory operate memory(physical memory):");
        System.out.format("Unsafe memory operate get method:    [%s] \n", "getInt getBoolean getByte getShort getChar getLong getFloat getDouble");
        System.out.format("Unsafe memory operate put method:    [%s] \n", "putInt putBoolean putByte putShort putChar putLong putFloat putDouble");


        //2.1 get memory address
        long memoryBytes = 2;
        System.out.format("Unsafe byte memory operate:    byte memory length: [%d] \n", memoryBytes);

        //2.2 allocate memory address, (long address is local pointer)
        long address = unsafe.allocateMemory( memoryBytes );
        System.out.format("Unsafe byte memory operate:    allocate memory( out of jvm dependent on physical memory ) ,size:[ %d ]\n", memoryBytes);

        //2.3 put byte_value
        byte byte_value = 1;
        unsafe.putByte(address,byte_value);
        System.out.format("Unsafe byte memory operate:    put byte value:[%s]\n", byte_value);

        //2.4 getByte
        byte byte_get = unsafe.getByte(address);
        System.out.format("Unsafe byte memory operate:    get byte value:[%s] \n", byte_get);

        //2.5 must be free memory
        unsafe.freeMemory( address );
        System.out.format("Unsafe byte memory operate:    free physical memory\n");

        System.out.println("...........................................Unsafe byte memory byte operate end..................................");
        System.out.println("\n");
    }

    public static void addressOperate(Unsafe unsafe) {
        System.out.println("\n");
        System.out.println("...........................................Unsafe address physical memory operate begin................................");
        String msg = "hello every one";
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        long memory_size = bytes.length;
        System.out.format("Unsafe address physical memory operate:    memory_size:[%d] \n", memory_size);

        long address = unsafe.allocateMemory(memory_size);
        System.out.format("Unsafe address physical memory operate:    address:[%d] \n", address);

        //todo 怎样操作内存 存量值
        //init value: set memory value by address
//        for(int i=0; i<memory_size; i++){
//            unsafe.setMemory(address,i,bytes[i]);
//        }
//        System.out.format("Unsafe address physical memory operate:    init set bytes value  by memory address :[%d] \n", address);

        //get memory value by address
//        byte[] values = new byte[ (int) memory_size ];
//        for(int i=0; i<memory_size; i++){
//            unsafe.getByte(address)
//            unsafe.setMemory(address,i,bytes[i]);
//        }

        System.out.println(String.valueOf (unsafe.getByte(address)));
        System.out.format("Unsafe address physical memory operate:    get bytes value  by memory address :[%d] \n", address);


        long pointer = unsafe.getAddress(address);
        System.out.format("Unsafe address physical memory operate:    pointer:[%d] \n", pointer);

        long pointer_memory_var = 0;
        unsafe.putAddress(address, pointer_memory_var);
        System.out.format("Unsafe address physical memory operate:    memory address to pointer var :[%d] \n", pointer_memory_var);

        long address_size = unsafe.addressSize();
        System.out.format("Unsafe address physical memory operate:    pointer address size:[%d] \n", address_size);

        int memory_page_size = unsafe.pageSize();
        System.out.format("Unsafe address physical memory operate:    memory page size:[%d] \n", unsafe.pageSize());

        System.out.println("...........................................Unsafe address physical memory operate end..................................");
        System.out.println("\n");
    }

    public static void memoryOperate(Unsafe unsafe) {
        System.out.println("\n");
        System.out.println("...........................................Unsafe physical memory operate begin................................");

        // allocate 100 bit memory space
        long address = unsafe.allocateMemory(100);
        System.out.format("Unsafe physical memory operate:    allocate memory size:[%d] \n", 100);

        // var pointer value -> to memory address space
        long pointer = unsafe.getAddress( address );
        System.out.format("Unsafe physical memory operate:    memory address to address:[%d] pointer:[%d] \n", address, pointer);

        // reallocate memory size 100 -> 200
        long reallocate_address = unsafe.reallocateMemory(address,200 );
        System.out.format("Unsafe physical memory operate:    reallocate memory size: 100 -> [%d], reallocate_address:[%d] \n", 200, reallocate_address);

        // set memory value
        long memory_size = 2;
        byte value_byte = 1;
        unsafe.setMemory(address,memory_size,value_byte);
        System.out.format("Unsafe physical memory operate:    set memory space, address:[%d] memory_size:[%d] value:[%d] \n", address, memory_size, value_byte);

        // get memory value
        byte byte_value = unsafe.getByte( address );
        System.out.format("Unsafe physical memory operate:    get value by address:[%d] \n", byte_value);

        // memory copy
        byte value = 2;
        unsafe.putByte(address , value);
        System.out.format("Unsafe physical memory operate:    put value to address, address:[%d] value:[%d]\n", address, value);

        // copy memory to new address
        long second_address = unsafe.allocateMemory( 200 );
        System.out.format("Unsafe physical memory operate:    allocate anther memory address:[%d] \n", second_address);

        unsafe.copyMemory(address,  second_address,100);
        System.out.format("Unsafe physical memory operate:    copy memory address to new address 100 byte  \n");

        //System.out.println(unsafe.getByte(second_address));
        System.out.format("Unsafe physical memory operate:    get copied second value:[%d]  \n", unsafe.getByte( second_address ) );

        long third_address = unsafe.allocateMemory( 200 );
        System.out.format("Unsafe physical memory operate:    allocate third memory address:[%d] \n", third_address);

        unsafe.copyMemory(null,address, null,third_address,100);
        System.out.format("Unsafe physical memory operate:    copied third value:[%d]  \n", unsafe.getByte( third_address ) );

        // free memory
        unsafe.freeMemory( address );
        System.out.format("Unsafe physical memory operate:   free memory address:[%d]  \n", address );
        unsafe.freeMemory( second_address );
        System.out.format("Unsafe physical memory operate:   free memory second_address:[%d]  \n", second_address );
        unsafe.freeMemory( third_address );
        System.out.format("Unsafe physical memory operate:   free memory third_address:[%d]  \n", third_address );

        System.out.println("...........................................Unsafe address physical memory operate end..................................");
        System.out.println("\n");
    }

    public static void staticFieldOperate(Unsafe unsafe) throws NoSuchFieldException {
        System.out.println("\n");
        System.out.println("...........................................Unsafe static field operate begin................................");
        User user = new User();

        Field static_field = User.class.getDeclaredField("gender");
        static_field.setAccessible(true);
        System.out.format("Unsafe physical memory operate:   get class invoke method:[%s]  \n", static_field );

        long static_field_offset = unsafe.staticFieldOffset(static_field);
        System.out.format("Unsafe physical memory operate:   object static field offset:[%d]  \n", static_field_offset);

        Object value = unsafe.getObject(user,static_field_offset);
        System.out.format("Unsafe physical memory operate:   get static field value:[%s]  \n", value);

        unsafe.putObject(user,static_field_offset,"fmail");
        System.out.format("Unsafe physical memory operate:   put static field new value:[%s]  \n", "fmail");

        Object new_value = unsafe.getObject(user,static_field_offset);
        System.out.format("Unsafe physical memory operate:   get static field new value:[%s]  \n", new_value);

        // static field memory snapshot
        Object v_snapshot = unsafe.staticFieldBase( static_field );
        System.out.format("Unsafe physical memory operate:   get static field memory snapshot:[%s]  \n", v_snapshot);

        System.out.println("...........................................Unsafe static field operate end..................................");
        System.out.println("\n");
    }

    public static void classOperate(Unsafe unsafe) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        System.out.println("\n");
        System.out.println("...........................................Unsafe class operate begin................................");

        File f = new File("D:\\unsafe_tmp\\A.class");
        FileInputStream input = new FileInputStream(f);
        byte[] content = new byte[ (int)f.length() ];
        //取一个class文件到byte数组中
        input.read(content);
        input.close();
        System.out.format("Unsafe class operate:   read class file:[%s]  \n", f.getAbsolutePath() );

        // 然后通过Unsafe.defineClass()来加载对应的Class  defineClass(String name, byte[] b, int off, int len,ClassLoader loader,ProtectionDomain protectionDomain);
        Class c = unsafe.defineClass( null, content, 0, content.length, UnsafeMainTest.class.getClassLoader(), null);
        System.out.format("Unsafe class operate:   unsafe load class file byte content as class object:[%s]  \n", c.getClass().getName() );

        // invoke class method
        Object invoke_value = c.getMethod("a").invoke( c.newInstance(), null);
        System.out.format("Unsafe class operate: defineClass -> invoke method returned:[%s]  \n", invoke_value);

        //分配一个实例，但不运行任何构造函数。初始化类（如果尚未初始化）。
        User unsafe_user = (User) unsafe.allocateInstance(User.class);
        System.out.format("Unsafe class operate: unsafe allocate class instance with no construct called: User.name:[%s]  \n", unsafe_user.getName());

        boolean shouldBeInitialized = unsafe.shouldBeInitialized( User.class );
        System.out.format("Unsafe class operate: unsafe check class shouldBeInitialized:[%b]  \n", shouldBeInitialized);


        System.out.println(".............................................Unsafe class operate end..................................");
        System.out.println("\n");
    }

    public static void threadOperate(Unsafe unsafe) throws InterruptedException {

        System.out.println("\n");
        System.out.println("...........................................Unsafe thread operate begin................................");
        System.out.format("Unsafe thread lock operate: thread park unpark operate.................................. \n" );

        Thread thread  = new Thread(new Runnable(){
            @SneakyThrows
            @Override
            public void run() {
                System.out.format("Unsafe thread lock operate: thread park testing--------------------------------  \n" );

                System.out.println("thread run begin--------------------------------");

                System.out.println("thread running--------------------------------");

                System.out.println("thread is blocking--------------------------------");

                unsafe.park(false,0);
                System.out.println("thread run end--------------------------------");

            }
        });

        thread.start();

        Thread.sleep(5000);

        System.out.println("thread unsafe unpark Interrupt blocking--------------------------------");

        unsafe.unpark(thread);

        System.out.println("thread unsafe unparked--------------------------------");

        System.out.format("Unsafe thread operate: thread unpark testing............................... \n" );

        System.out.println(".............................................Unsafe thread lock operate end..................................");
        System.out.println("\n");
    }

    public static void cpuSystemLoadavgMonitor(Unsafe unsafe) {
        System.out.println("\n");
        System.out.println("...........................................Unsafe cpu system loadavg monitor begin................................");

        double[] loadavg = new double[100];
        // nelems 只能取值为 1 到 3，分别代表最近 1、5、15 分钟内系统的平均负载.
        int monitor_result = unsafe.getLoadAverage(loadavg,3);

        System.out.format("Unsafe cpu system loadavg monitor:  monitor_result = unsafe.getLoadAverage(loadavg,1), monitor_result:[%d]  \n", monitor_result);

        for(int i=0; i<100; i++) {
            if( loadavg[i] != 0) {
                System.out.println("cpu loadavg: "+ loadavg[i]);
            }
        }

        System.out.println(".............................................Unsafe cpu system loadavg monitor operate end..................................");
        System.out.println("\n");
    }

    public static void synchronizeMonitorOperate(Unsafe unsafe) {
        System.out.println("\n");
        System.out.println("...........................................Unsafe synchronize monitor operate begin................................");



        System.out.format("Unsafe synchronize monitor(lock object monitor) operate: test begining  \n");

        Thread mainThread = new Thread(new Runnable() {

            @Override
            public void run() {
                    thread_01.start();
                    thread_03.start();
                    thread_02.start();
            }

            Object monitorObject = new Object();

            Thread thread_01 = new Thread(new Runnable() {
                public static final  String threadName = "thread_01";
                @Override
                public void run() {
                    unsafe.monitorEnter( monitorObject );
                    for(int i=0; i<5; i++) {
                        System.out.format("____________________________:  running thread:[%s]_______[%d]  \n", threadName,i);
                    }
                    unsafe.monitorExit( monitorObject );
                }
            });

            Thread thread_02 = new Thread(new Runnable() {
                public static final  String threadName = "thread_02";
                @Override
                public void run() {
                    unsafe.monitorEnter( monitorObject );
                    for(int i=0; i<5; i++) {
                        System.out.format("____________________________:  running thread:[%s]_______[%d]  \n", threadName,i);
                    }
                    unsafe.monitorExit( monitorObject );
                }
            });

            Thread thread_03 = new Thread(new Runnable() {
                public static final  String threadName = "thread_03";
                @Override
                public void run() {

                    boolean getMonitor = unsafe.tryMonitorEnter( monitorObject );

                    if( getMonitor ) {
                        for(int i=0; i<5; i++) {
                            System.out.format("____________________________:  running thread:[%s]_______[%d]  \n", threadName,i);
                        }
                        unsafe.monitorExit( monitorObject );
                    } else {
                        this.run();
                    }

                }
            });


        });

        mainThread.start();

        System.out.format("Unsafe synchronize monitor(lock object monitor) operate: test end  \n");
        System.out.println(".............................................Unsafe synchronize operate end..................................");
        System.out.println("\n");
    }
    @Data
    @AllArgsConstructor
    //@NoArgsConstructor
    public static  class User {
        private Integer age;
        private String name;
        public User() {
            this.name = "called User construct method";
        }
        private static String gender = "male";
    }

    public static void compareAndSwapOperate(Unsafe unsafe) throws NoSuchFieldException {
        System.out.println("\n");
        System.out.println("...........................................Unsafe cas (compareAndSwap)  operate begin................................");

        User user = new User();
        user.setAge(20);
        long fieldOffset = unsafe.objectFieldOffset(User.class.getDeclaredField("age"));
        unsafe.compareAndSwapObject(user,fieldOffset,20,30);
        unsafe.compareAndSwapObject(user,fieldOffset,50,100);
        System.out.format("Unsafe cas (compareAndSwap)  operate:  cas compareAndSwapObject user.age: 20->[%d] \n" ,user.getAge());

        Integer intObject = new Integer(100);
        long value_offset = unsafe.objectFieldOffset(Integer.class.getDeclaredField("value"));
        unsafe.compareAndSwapInt(intObject,value_offset,100,200);
        System.out.format("Unsafe cas (compareAndSwap)  operate:  cas compareAndSwapInt Integer intValue: 100->[%d] \n" , intObject.intValue() );

        Long longObject = new Long(300);
        long lvalue_offset = unsafe.objectFieldOffset(Long.class.getDeclaredField("value"));
        unsafe.compareAndSwapLong(longObject,lvalue_offset,300,400);
        System.out.format("Unsafe cas (compareAndSwap)  operate:  cas compareAndSwapLong Long longValue: 300->[%d] \n" , longObject.intValue() );

        System.out.println(".............................................Unsafe cas (compareAndSwap)  operate end..................................");
        System.out.println("\n");
    }

    public static void volatileOperate(Unsafe unsafe) throws NoSuchFieldException {
        System.out.println("\n");
        System.out.println("...........................................Unsafe volatile get put operate begin................................");
        User user = new User();
        user.setAge(20);
        long fieldOffset = unsafe.objectFieldOffset(User.class.getDeclaredField("age"));

        Thread thread_01 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.format("____________________________:thread_01  user old age:[%d]  \n", user.getAge());
                unsafe.putObjectVolatile(user,fieldOffset,user.getAge()+10);
                System.out.format("____________________________:thread_01  user new age:[%d]  \n", user.getAge());
            }
        });

        Thread thread_02 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.format("____________________________:thread_02  user old age:[%d]  \n", user.getAge());
                unsafe.putObjectVolatile(user,fieldOffset,user.getAge()+50);
                System.out.format("____________________________:thread_02  user new age:[%d]  \n", user.getAge());
            }
        });

        thread_01.start();
        thread_02.start();

        System.out.println(".............................................Unsafe volatile get put operate end..................................");
        System.out.println("\n");
    }

    /**
     * No results were verified
     * @param unsafe
     * @throws InterruptedException
     * @throws NoSuchFieldException
     */
    public static void fenceOperate(Unsafe unsafe) throws InterruptedException, NoSuchFieldException {
        System.out.println("\n");
        System.out.println("...........................................Unsafe fence operate begin................................");

        System.out.println("___________________________________load(read) fence example start____________________________________");
        /*
        FenceRunnable fenceRunnable = new FenceRunnable();
        Thread thread = new Thread( fenceRunnable );
        thread.start();
         */

        boolean cycle = true;
        /*
        System.out.println();
        System.out.format("++++++++++++++++++++++++++++ load(read): with unsafe no load fence start ++++++++++++++++++++++++++++  \n");
        int no_load_fence_count = 0;
        while (cycle) {
            boolean flag_copy = fenceRunnable.isFlag();
            System.out.format("____________________________load(read): 1. with no load fence  \n");
            System.out.format("____________________________load(read): 1. no copy:storage memory doesn't copy to master memory with no load fence. flag_copy:[%b]  \n", flag_copy);
            cycle = !flag_copy;
            no_load_fence_count ++;
            System.out.format("____________________________load(read): 1. with no load fence  no_load_fence_count:[%d]  \n", no_load_fence_count);
        }
        System.out.format("++++++++++++++++++++++++++++ load(read): with unsafe no load fence end ++++++++++++++++++++++++++++  \n");
        */
        /*
        System.out.println();

        System.out.format("++++++++++++++++++++++++++++ load(read): with unsafe no load fence,but with unsafe volatile get start ++++++++++++++++++++++++++++  \n");
        int no_load_fence_with_volatile_count = 0;
        while (cycle) {
            boolean flag_copy = fenceRunnable.isFlag();
            System.out.format("____________________________load(read): 2. with no load fence,but with unsafe volatile get  \n");
            System.out.format("____________________________load(read): 2. copy:storage memory copy to master memory with no load fence,but with volatile get. flag_copy:[%b]  \n", flag_copy);
            cycle = !flag_copy;
            no_load_fence_with_volatile_count ++;
            System.out.format("____________________________load(read): 2. with no load fence,but with unsafe volatile get no_load_fence_with_volatile_count:[%d]  \n", no_load_fence_with_volatile_count);
        }
        System.out.format("++++++++++++++++++++++++++++ load(read): with unsafe no load fence,but with unsafe volatile get end ++++++++++++++++++++++++++++  \n");

        System.out.println();
         */
        /*
        System.out.format("++++++++++++++++++++++++++++ load(read): with unsafe load fence start ++++++++++++++++++++++++++++  \n");
        int load_fence_count = 0;
        while (cycle) {
            unsafe.loadFence();
            boolean flag_copy = fenceRunnable.isFlag();
            System.out.format("____________________________load(read): 3. load fence  \n");
            System.out.format("____________________________load(read): 3. copy:storage memory copy to master memory with load fence. flag_copy:[%b]  \n", flag_copy);
            cycle = !flag_copy;
            load_fence_count ++;
            System.out.format("____________________________load(read): 3. load fence load_fence_count:[%d]  \n", load_fence_count);
        }
        System.out.format("++++++++++++++++++++++++++++ load(read): with unsafe load fence end ++++++++++++++++++++++++++++  \n");

        System.out.println();

        Thread.sleep(2000);
         */


        System.out.println("___________________________________load(read) fence example end____________________________________");

        System.out.println(".............................................Unsafe fence operate end..................................");
        System.out.println("\n");
    }

    @Data
    public static class FenceRunnable implements Runnable {
        boolean flag = false;
        @Override
        public void run() {
            System.out.format("____________________________load(read): thread running start protected flag value:[%b]  \n", this.flag);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.flag = true;
            System.out.format("____________________________load(read): thread running end protected flag value:[%b]  \n", this.flag);
        }

    }


    static volatile int number = 0;

    public static void memoryBarrierOperate(Unsafe unsafe) {
        System.out.println("\n");
        System.out.println("...........................................Unsafe memory barrier operate begin................................");

        Thread thread_01 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                number++;
            }
        });

        Thread thread_02 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                number++;
            }
        });

        thread_01.start();
        thread_02.start();

        try {
            thread_01.join();
            thread_02.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 等待所有操作完成 (同一代码块)
        unsafe.loadFence();
        System.out.format("Unsafe memory barrier operate: 1.loadFence（加载屏障）：在读取从存储器或其他设备中读取的数据之前，必须确保所有读或写访问都已完成。 \n");
        System.out.format("Unsafe memory barrier operate: 1.loadFence number:[%d] \n", number);
        System.out.println();

        number = 0;
        System.out.format("Unsafe memory barrier operate: the next storeFence init number:[%d] \n", number);
        System.out.println();

        Thread thread_21 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                number++;
                unsafe.storeFence();//进行存储
            }
        });

        Thread thread_22 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                number++;
                unsafe.storeFence();//进行存储
            }
        });

        thread_21.start();
        thread_22.start();

        try {
            thread_21.join();
            thread_22.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 保证数据已完成写入
        unsafe.storeFence();
        System.out.format("Unsafe memory barrier operate: 2.storeFence（存储屏障）：在将数据写入内存或其他设备之前，必须确保先完成了所有读取或写入访问 \n");
        System.out.format("Unsafe memory barrier operate: 2.storeFence number:[%d] \n", number);
        System.out.println();

        number = 0;
        System.out.format("Unsafe memory barrier operate:  the next fullFence init number:[%d] \n", number);
        System.out.println();

        Thread thread_31 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                number++;
                unsafe.fullFence(); // 确保所有操作（读和写）都已完成
            }
        });

        Thread thread_32 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                number++;
                unsafe.fullFence(); // 确保所有操作（读和写）都已完成
            }
        });

        thread_31.start();
        thread_32.start();

        try {
            thread_31.join();
            thread_32.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确保所有操作（读和写）都已完成
        unsafe.fullFence();
        System.out.format("Unsafe memory barrier operate: 3.fullFence（全屏障）：在读取和写入数据之前，必须确保所有读/写访问均已完成 \n");
        System.out.format("Unsafe memory barrier operate: 3.fullFence number:[%d] \n", number);
        System.out.println();

        System.out.println(".............................................Unsafe memory barrier operate end..................................");
        System.out.println("\n");
    }

    public static void arrayOperate(Unsafe unsafe) {
        System.out.println("\n");
        System.out.println("...........................................Unsafe array operate begin................................");


        String[] string_array = new String[]{"1", "2", "3","4",""};
        System.out.format("Unsafe array operate:  init an String array:[%s] \n\n", string_array);

        //获取数组的在内容中的基本偏移量
        //给定数组类的存储分配中第一个元素的偏移量。相对数组array 起始地址（也是数组头地址长度）
        System.out.format("Unsafe array operate: arrayBaseOffset, get array head address length, this is an array base address offset \n");
        long baseArrayOffset = unsafe.arrayBaseOffset(String[].class);
        System.out.format("Unsafe array operate: string_array base of offset(baseArrayOffset/array head of address length):[%d] \n\n",baseArrayOffset);

        //every index scale:每个元素的地址长度
        //数组中元素的大小通过arrayIndexScale获得，即第n+1个元素的开始位置应该是 ：arrayOffset + n * arrayScale
        System.out.format("Unsafe array operate: arrayIndexScale, every index scale, is each element memory content length \n");
        System.out.format("Unsafe array operate: arrayIndexScale,  n+1 offset = arrayOffset + n * arrayScale \n");
        long scale = unsafe.arrayIndexScale(String[].class);
        System.out.format("Unsafe array operate: string_array every index scale, is each element memory content length:[%d] \n\n",scale);

        System.out.format("Unsafe array operate: string_array first element :[%s] \n\n", unsafe.getObject(string_array, baseArrayOffset));

        //set 5 to last string
        unsafe.putObject(string_array,  baseArrayOffset +  4 * scale, "5");
        System.out.format("Unsafe array operate: set '5' to last element unsafe.putObject() \n");

        //print first string in strings[] again
        System.out.format("Unsafe array operate: get last element:[%s] \n", unsafe.getObject(string_array, baseArrayOffset + scale * 4));

        System.out.println(".............................................Unsafe array operate end..................................");
        System.out.println("\n");
    }

}


