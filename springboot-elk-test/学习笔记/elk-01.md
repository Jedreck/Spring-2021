### E(ElasticSearch)L(Logstash)K(Kibana)搭建

#### 搭建elasticsearch，单机

1. 首先去官网下载最新的安装包 `https://www.elastic.co/cn/downloads/elasticsearch` ，现在最新的是`Version: 7.13.2 | Release date: June 15, 2021` 

2. 一定要切换为非root用户，否则后续会有安全问题，耽误启动

3. 解压 `tar -zxvf elasticsearch-7.13.2-linux-x86_64.tar.gz` 

4. es7以上需要`JDK 11` ，但安装包已经内置JDK，所以可以在 `bin/elasticsearch` 文件的头部加上 

   ```tex
   ES_JAVA_HOME="/jj/soft/elk/elasticsearch-7.13.2/jdk"
   ```

   配置，就可以正常启动了

5. 没什么需求，直接启动：`bin/elasticsearch` ，后台：`bin/elasticsearch -d` 

6. 外网访问，需要修改配置 `config/elasticsearch.yml` 中

   ```yaml
   node.name: node-1
   network.host: 0.0.0.0
   ```

7. 访问 `http://222.2.2.2:9200/` 有状态输出



> 启动有点问题

- [1] `max file descriptors [4096] for elasticsearch process is too low, increase to at least [65535]`

   修改 `/etc/security/limits.conf` 添加

  ```
  * soft nofile 65536
  * hard nofile 65536
  ```

  

- [2] `max number of threads [3818] for user [es] is too low, increase to at least [4096]`

  修改 `/etc/security/limits.conf` 添加

  ```
  * soft nproc 4096
  * hard nproc 4096
  ```

  

- [3] `max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]`

  修改 `/etc/sysctl.conf` 文件，添加

  ```conf
  vm.max_map_count=262144
  ```

  

- [4] `the default discovery settings are unsuitable for production use; at least one of [discovery.seed_hosts, discovery.seed_providers, cluster.initial_master_nodes] must be configured`

  修改 `/config/elasticsearch.yml` 中 

  ```yaml
  node.name: node-1
  cluster.initial_master_nodes: ["node-1"]
  ```



#### 搭建Logstash，单机

1. 官网下载最新安装包 `https://www.elastic.co/cn/downloads/logstash` 

2. 解压 `tar -zxvf logstash-7.13.2-linux-x86_64.tar.gz ` 

3. 修改配置文件 `config/logstash.yml` 

   ```
   http.host: "222.2.2.2"
   ```

4. 新建配置文件 `vim logstash.conf` 

   ```
   input {
     tcp {
       #模式选择为server
       mode => "server"
       #ip和端口根据自己情况填写，端口默认4560,对应下文logback.xml里appender中的destination
       host => "222.2.2.22"
       port => 4560
       #格式json
       codec => json_lines
     }
   }
   filter {
     #过滤器，根据需要填写
   }
   output {
     elasticsearch {
       action => "index"
       #这里是es的地址，多个es要写成数组的形式
       hosts  => ["222.2.2.2:9200"]
       #用于kibana过滤，可以填项目名称
       index  => "elk-test-index"
     }
   }
   ```

5. 启动 `bin/logstash -f logstash.conf `，启动后可访问 ` 222.2.2.2:9600 ` 有状态信息



#### 搭建SpringBoot环境

1. 引入依赖，

   注意：会有依赖冲突，以`spring-boot-starter-web`为主，否则无法启动SpringBoot。

   ```xml
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
           </dependency>
           <dependency>
               <groupId>net.logstash.logback</groupId>
               <artifactId>logstash-logback-encoder</artifactId>
               <version>4.11</version>
               <exclusions>
                   <exclusion>
                       <artifactId>jackson-databind</artifactId>
                       <groupId>com.fasterxml.jackson.core</groupId>
                   </exclusion>
                   <exclusion>
                       <artifactId>jackson-core</artifactId>
                       <groupId>com.fasterxml.jackson.core</groupId>
                   </exclusion>
               </exclusions>
           </dependency>
       </dependencies>
   ```

   

2. 配置 logstash，在 `resources` 下新建 `logback.xml` 文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <configuration>
       <include resource="org/springframework/boot/logging/logback/base.xml"/>
   
       <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
           <destination>222.2.2.2:4560</destination>
           <encoder charset="UTF-8" class="net.logstash.logback.encoder.LogstashEncoder"/>
       </appender>
   
       <root level="INFO">
           <appender-ref ref="LOGSTASH"/>
           <appender-ref ref="CONSOLE"/>
       </root>
   </configuration>
   ```

   

3. 搭建一个最简 Springboot 环境，写一个 Controller

   ```java
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   @Slf4j
   @RestController
   @RequestMapping("/t01")
   public class Controller {
       @GetMapping("/1")
       public String t1() {
           log.debug("debug-01");
           log.info("info-01");
           log.warn("warn-01");
           log.error("error-01");
           return "done";
       }
   }
   ```

   

4. 请求接口后，访问 ` 222.2.2.2:9200/elk-test-index/_search ` 能看到打出的日志信息，使用idea的http请求可以看到最新的日志

   ```http
   ###
   POST /elk-test-index/_search HTTP/1.1
   Host: 222.2.2.2:9200
   Content-Type: application/json
   
   {
     "sort": [
       { "@timestamp": "desc" }
     ]
   }
   ```

   



### 网络配置

- 查看开放的端口列表

  ```bash
  firewall-cmd --zone=public --list-ports
  ```

-  开启端口 

  ```bash
  firewall-cmd --zone=public --add-port=9200/tcp --permanent
  ```

- 关闭端口

  ```bash
  firewall-cmd --zone=public --remove-port=5672/tcp --permanent 
  ```

- 重新加载

  ```\
  firewall-cmd --reload
  ```

- 关闭防火墙

  ```bash
  systemctl stop firewalld.service
  ```

- 查看防火墙状态

  ```bash
  firewall-cmd --state
  ```

- 查看监听的端口

  ```bash
  netstat -lnpt
  
  netstat -lnpt | grep 5672
  ```

  

