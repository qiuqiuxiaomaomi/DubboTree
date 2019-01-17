# DubboTree
阿里Dubbo微服务

###Dubbo角色图
![](https://i.imgur.com/WLHpVnC.png)

<pre>
角色：
      1）Consumer服务消费者:
      2）Provider服务提供者
      3）Registry注册中心

      服务提供者Provider先Start，向Registry注册服务，消费者Consumer订阅服务。
      新的服务注册到注册中心以后，注册中心会将这些服务通过notify到消费者。

      Monitor是一个监控系统，Consumer和Provider通过异步的方式发送消息至Monitor。Consumer和
  Provider会将信息存放在本地磁盘，平均1min会发送一次信息。
</pre>

###Spring Cloud角色图

![](https://i.imgur.com/THPrkds.png)

<pre>
1) Eureka Server: 服务注册中心，服务发现中心
2）Service Consumer:调用远程服务的服务消费方
3）Service Provider:暴露服务的提供方
</pre>

###Dubbo 和 Spring Cloud区别

![](https://i.imgur.com/P17v7yX.png)

<pre>
区别：
          Dubbo只是实现了服务治理，而Spring Cloud子项目分别覆盖了微服务下的众多部件，
      服务治理只是其中之一。
   
          Dubbo提供了各种Filter，对于上述中无的要素，可以通过扩展Filter来完善，比如：
               1）分配式配置：可以使用淘宝的Diamond，百度的Disconf来实现分布式配置管理。
               2）服务跟踪：
               3）批量任务：可以使用开源的Elastic-Job

          从核心要素来看，Spring Cloud更胜一筹。Dubbo需要自己实现各种Filter。

      通信协议：
              1）Dubbo:Dubbo缺省协议采用单一长连接和NIO异步通讯，适合于小数据量大并发
                       的服务调用，以及服务消费者机器数远大于服务提供者机器数的情况。
              2）RMI：RMI协议采用JDK标准的java.rmi.*实现，采用阻塞式短连接和JDK标准序列
                      化方式。
              3）Hessian:Hessian协议用于集成Hessian的服务，Hessian底层采用HTTP通信，
                         采用Servlet暴露服务，Dubbo缺省内嵌Jetty作为服务器实现。
     
      点评：Dubbo 支持各种通信协议，而且消费方和服务方使用长链接方式交互，通信速度上略
           胜 Spring Cloud，如果对于系统的响应时间有严格要求，长链接更合适.

      Dubbo:
           需要为每个微服务定义各自的 Interface 接口，并通过持续集成发布到私有仓库中。调
           用方应用对微服务提供的抽象接口存在强依赖关系，开发、测试、集成环境都需要严格的
           管理版本依赖。

      Spring Cloud：
           服务提供方和服务消费方通过 Json 方式交互，因此只需要定义好相关 Json 字段即
           可，消费方和提供方无接口依赖
</pre>

超时重试：

Dubbo 超时与重试的实现分析

https://blog.csdn.net/Revivedsun/article/details/72851417

<pre>
     DUBBO消费端设置额超时时间不能随心所欲，需要根据业务实际情况来设定，如果设置的时间
     太短，导致复杂业务本来就需要很长时间完成，导致在设定的超时时间内无法完成正常的业务
     处理。如果消费端达到超时时间，那么dubbo会进行重试机制（如果配置
     了dubbo.reference.retries>1），这种情况其实给服务提供端带来莫名的压力，而压力是
     正常值*dubbo.reference.retries，最终dubbo的消费端会出现RpcException提示retry了
     多少次还是失败。这种情况就是没有合理设置接口超时时间带来的问题。 


     说完超时时间，再说说重试机制。重试机制是在等待超时时间到了之后或者服务提供端出现异
     常进行再次重试的机制。这个并不代表服务提供端完全执行失败了。所以不是所有接口都适合
     重试，如果一个服务是不等幂，那么不适合重试的机制，因为会存在重复提交的问题，否则
     是可以进行重试的。比如提交一个订单的接口是不能进行重试的，而查询类型的接口是可以重试的
</pre>

###Dubbo组件运行图

![](https://i.imgur.com/UrMTfTt.png)

<pre>

</pre>

###Spring Cloud组件运行图

![](https://i.imgur.com/sd1eVMj.png)

<pre>
1）所有请求都统一通过 API 网关（Zuul）来访问内部服务。
2）网关接收到请求后，从注册中心（Eureka）获取可用服务。
3）由 Ribbon 进行均衡负载后，分发到后端的具体实例。
5）微服务之间通过 Feign 进行通信处理业务
</pre>

###互联网通用的网络架构图

![](https://i.imgur.com/LFNSsq5.png)

<pre>
1）网关集群：
          数据的聚合、实现对接入客户端的身份认证、防报文重放与防数据篡改、功能调用的业务
       鉴权、响应数据的脱敏、流量与并发控制等
2）业务集群：
          一般情况下移动端访问和浏览器访问的网关需要隔离，防止业务耦合
3）Local Cache：
          由于客户端访问业务可能需要调用多个服务聚合，所以本地缓存有效的降低了服务调用的
       频次，同时也提示了访问速度。本地缓存一般使用自动过期方式，业务场景中允许有一定的数
       据延时。
5）服务层：
          原子服务层，实现基础的增删改查功能，如果需要依赖其他服务需要在 Service 层主动调用
6）Remote Cache：
          访问 DB 前置一层分布式缓存，减少 DB 交互次数，提升系统的TPS
7）DAL：
          数据访问层，如果单表数据量过大则需要通过 DAL 层做数据的分库分表处理。
8）MQ：
          消息队列用来解耦服务之间的依赖，异步调用可以通过 MQ 的方式来执行。
9）数据库主从：
          服务化过程中必经的阶段，用来提升系统的 TPS。
</pre>

<pre>
      dubbo用ProxyFactoty代理工厂将HelloServiceImpl封装成1个Inoke执行，即
      ProxyFactory.getInvoke(ref, (Class)接口，注册URL，解码参数)，并将Invoke导出成
      1个Exporter，包括去注册中心ZK注册服务。Invoke有本地执行的Invoke，远程通信执行的
      Invoke。
</pre>

<pre>
28，Dubbo源码使用了哪些设计模式

A，工厂模式，ExtenstionLoader.getExtenstionLoader(Protocol.class).getAdaptiveExtenstion()

B，装饰器模式+责任链，以provider的调用链为例，具体调用链代码是在protocolFilterWrapper的
buildInvokeChain完成的,将注解中含有group=provider的Filter实现，调用顺序为
        EchoFilter -> 
        ClassLoaderFilter -> 
        GenericFilter -> 
        ContextFilter -> 
        ExceptionFilter -> 
        TimeoutFilter -> 
        MonitorFilter -> 
       TraceFilter。
装饰器模式和责任链混合使用，Echo是回声测试请求，ClassLoaderFilter则只是在其主功能上添加了功能。

C，观察者模式，provider启动时需要与注册中心交互，先注册自己的服务，再订阅自己的服务，订阅时采用了观察
   者模式，注册中心每5s定时检查是否有服务更新，有更新则向服务提供者发送1个notify消息后即可运行
   NotifyListener的notity方法，执行监听器方法。

D，动态代理模式。  扩展JDK的ExtensionLoaderdeAdaptive实现，根据调用阶段动态参数决定调用哪个类，生成
  ****代理类的代码是ExtensionLoader的createAdaptiveExtenstionClassLoader方法。
</pre>

<pre>
负载均衡算法？

常见6种负载均衡算法：轮询，随机，源地址哈希，加权轮询，加权随机，最小连接数。

nginx5种负载均衡算法：轮询，weight，ip_hash，fair（响应时间），url_hash

dubbo负载均衡算法：随机，轮询，最少活跃调用数，一致性Hash
</pre>

<pre>
一个线程池正在处理服务如果忽然断电该怎么办？

答：

队列实现持久化储存，下次启动自动载入。
但是实际需要看情况，大体思路是这样。
添加标志位，未处理 0，处理中 1，已处理 2。每次启动的时候，把所有状态为 1 的，置为 0。或者定时器处理
关键性的应用就给电脑配个 UPS。
</pre>

<pre>
java并发包下有哪些类？

答：ConcurrentHashMap，ConcurrentSkipListMap，ConcurrentNavigableMap

CopyOnWriteArrayList

BlockingQueue，BlockingDeque （ArrayBlockingQueue，LinkedBlockingDeque，LinkedBlockingQueue，
DelayQueue，PriorityBlockingQueue，SynchronousQueue）

ConcurrentLinkedDeque，ConcurrentLinkedQueue，TransferQueue，LinkedTransferQueue

CopyOnWriteArraySet，ConcurrentSkipListSet

CyclicBarrier，CountDownLatch

Lock（ReetrantLock，ReetrantReadWriteLock）

Atomic包
</pre>

<pre>
threadlocal为什么会出现oom？

答：ThreadLocal里面使用了一个存在弱引用的map, map的类型是ThreadLocal.ThreadLocalMap. Map中的key为
一个threadlocal实例。这个Map的确使用了弱引用，不过弱引用只是针对key。每个key都弱引用指向threadlocal。 
当把threadlocal实例置为null以后，没有任何强引用指向threadlocal实例，所以threadlocal将会被gc回收。
但是，我们的value却不能回收，而这块value永远不会被访问到了，所以存在着内存泄露。因为存在一条从current
 thread连接过来的强引用。只有当前thread结束以后，current thread就不会存在栈中，强引用断开，Current 
Thread、Map value将全部被GC回收。最好的做法是将调用threadlocal的remove方法。

在ThreadLocal的get(),set(),remove()的时候都会清除线程ThreadLocalMap里所有key为null的value，但是这
些被动的预防措施并不能保证不会内存泄漏：

（1）使用static的ThreadLocal，延长了ThreadLocal的生命周期，可能导致内存泄漏。
（2）分配使用了ThreadLocal又不再调用get(),set(),remove()方法，那么就会导致内存泄漏，因为这块内存一直存在。
</pre>

<pre>
mysql数据库锁表怎么解决？

答：查询锁表信息
当前运行的所有事务
select * from information_schema.innodb_trx
当前出现的锁
select * from information_schema.innodb_locks
锁等待的对应关系
select * from information_schema.innodb_lock_waits  

通过 select * from information_schema.innodb_trx 查询 trx_mysql_thread_id然后执行 kill 线程ID
KILL   8807;//后面的数字即时进程的ID
</pre>

<pre>
Spring+MyBatis实现读写分离简述？

答：

    方案一：通过MyBatis配置文件创建读写分离两个DataSource，每个SqlSessionFactoryBean对象的
           mapperLocations属性制定两个读写数据源的配置文件。将所有读的操作配置在读文件中，所有写的操作配置在写文件中。
    方案二：通过Spring AOP在业务层实现读写分离，在DAO层调用前定义切面，利用Spring的
          AbstractRoutingDataSource解决多数据源的问题，实现动态选择数据源
    方案三：通过Mybatis的Plugin在业务层实现数据库读写分离，在MyBatis创建Statement对象前通过拦截器选
          择真正的数据源，在拦截器中根据方法名称不同（select、update、insert、delete）选择数据源。
    方案四：通过spring的AbstractRoutingDataSource和mybatis Plugin拦截器实现非常友好的读写分离，原
          有代码不需要任何改变。推荐第四种方案
</pre>

![](https://i.imgur.com/zlKivHr.png)

<pre>
Dubbo的四种注册中心

      1:Multicast注册中心
        不需要启动任何中心节点，只要广播地址一样，就可以互相发现。组播受网络结构限制，只适合
        小规模应用或开发阶段使用。

        步骤：
            1）提供方启动时广播自己的地址。
            2）消费方启动时广播订阅请求。
            3）提供方收到订阅请求时，单播自己的地址给订阅者，如果设置了unicast=false，则
               广播给订阅者。
            5）消费方收到提供方地址时，连接该地址进行RPC调用。
</pre>

<pre>
Dubbo的四种注册中心

      1:Zookeeper注册中心
</pre>

<pre>
Dubbo的四种注册中心

      1:Redis的发布订阅实现注册中心
</pre>