### 连接dashboard

1. 配置dashboard，配置后就可以启动了，默认账号密码：`sentinel`，

   ```properties
   server.port=50089
   project.name=sentinel-dashboard
   csp.sentinel.dashboard.server=localhost:${server.port}
   auth.username=sentinel
   auth.password=sentinel
   ```

   

2. 配置sentinel应用，要先看是否已经引用了`sentinel-transport-simple-http`，如果没有引用，则需要单独在pom中引用

   ```yaml
   project:
     name: ${spring.application.name}
   
   spring:
     application:
       name: test-sentinel-01
     cloud:
       sentinel:
         transport:
           dashboard: localhost:50089
           port: 8719
   ```

3. 要有一次监控动作，才能注册上，才能看到



### Fallback

fallback是指发生异常的情况



### blockHandler

block是指被拒绝请求的情况

