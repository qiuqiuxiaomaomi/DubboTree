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