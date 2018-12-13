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