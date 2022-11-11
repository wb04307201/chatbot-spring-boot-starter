# 群机器人发送消息

[![](https://jitpack.io/v/com.gitee.wb04307201/chatbot-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/chatbot-spring-boot-starter)

#### 对如下移动办公系统的群机器人API做了一层封装，让使用更简单便捷。
> - [钉钉](https://open.dingtalk.com/document/group/custom-robot-access)
> - [企业微信](https://developer.work.weixin.qq.com/document/path/91770)
> - [飞书](https://open.feishu.cn/document/ukTMukTMukTM/ucTM5YjL3ETO24yNxkjN)

> 只需要简单的配置和编码，即可将相同的消息发送到多个聊天群  
> 目前支持两种消息模式 文本 和 markdown(飞书对应为富文本)  
> 钉钉和飞书需要使用加签

## 第一步 增加 JitPack 仓库
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

## 第二步 引入jar
版本请到[jitpack](https://jitpack.io/#com.gitee.wb04307201/file-preview-spring-boot-starter)查看
```xml
    <dependency>
        <groupId>com.gitee.wb04307201</groupId>
        <artifactId>chatbot-spring-boot-starter</artifactId>
        <version>版本</version>
    </dependency>
```

## 第三步 在启动类上加上`@EnableChatbot`注解

```java
@EnableChatbot
@SpringBootApplication
public class ChatbotDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTestApplication.class, args);
	}
	
}
```

## 第四步 `application.yml`配置文件中添加以下相关配置，可以配置多个群

```yaml
chatbot:
  config:
    chatbot-info:
      - chatbot-type: dingtalk
        token: token
        secret: secret
      - chatbot-type: weixin
        token: token
      - chatbot-type: feishu
        token: token
```

## 第五步 根据需要注入 并调用发送

```java
    @Autowired
    IRobotService robotService;

    @PostMapping("/test")
    public OapiRobotSendResponse test() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ApiException {
        return robotService.sendMarkDown("标题","内容");
    }
```

发送的消息可通过/chat/robot/list进行查看