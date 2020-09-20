# fun-extension

`spring` 扩展点,简化开发过程中的逻辑判断,不同业务*分而治之*

## 为什么要使用扩展点

> 传统的业务中,遇到复杂的逻辑总少不了`if else`,那么如果才能减少逻辑判断,让代码更优雅呢


## 使用

```xml
 <dependency>
    <groupId>xin.tianhui.cloud</groupId>
    <artifactId>fun-extension-spring-boot-starter</artifactId>
</dependency>
```

声明扩扩展点

```java
@ExtensionPoint(value = "helloService",desc = "扩展点示例")
public interface HelloService extends IExtensionPoint {
    String hello(String name);
}
```

实现扩展点

```java
@ExtensionService(name = "helloService", bizCode = "xin.tianhui.cloud.hello")
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
```

调用

```java
public class Demo{
    @Autowired
    private ExtensionExecutor extensionExecutor;
    
    @Test
    public void test() {
        Context context = Context.create("xin.tianhui.cloud.hello");
        String result = extensionExecutor.submit(HelloService.class, context, helloService -> helloService.hello("tom"));
        log.info("result:{}",result);
    }
}
```

## Context

> 执行某个扩展点的依据,根据`bizCode`进行判断,是否执行

## ExtensionExecutor

|方法名|参数|描述|
|---|---|---|
|submit|{扩展点},{context},{带返回值的函数}|提交并返回|
|execute|{扩展点},{context},{带返回值的函数}|执行函数,无返回值|






