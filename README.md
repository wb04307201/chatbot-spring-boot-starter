# 钉钉群机器人starter

Dingtalk Chat Group Robot 钉钉聊天群机器人(鉴签)消息发送

[![](https://jitpack.io/v/com.gitee.wb04307201/dingtalk-chat-group-robot-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/dingtalk-chat-group-robot-spring-boot-starter)

在启动类上加上`@EnableDCGR`注解

```java
@EnableDCGR
@SpringBootApplication
public class SpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTestApplication.class, args);
	}
	
}
```

`application.yml`配置文件中添加以下相关配置

```yaml
dingtalk:
  robot:
    props:
      accessToken: accessToken #钉钉群accessToken
      secret: secret #钉钉群secret
```

`application.yml`配置文件中添加以下相关配置

```java
    @Autowired
    IRobotService robotService;

    @PostMapping("/test")
    public OapiRobotSendResponse test() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ApiException {
        return robotService.sendMarkDown("标题","内容");
    }
```

发送的消息可通过/dingtalk/robot/list进行查看
![img.png](img.png)