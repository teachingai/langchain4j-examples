# Langchain4j + Javalin 迁移指南

## 迁移原则

1. **移除所有 Spring Boot 依赖**，改为 Javalin
2. **移除 Spring AI 依赖**，只使用 langchain4j
3. **移除所有 Spring 注解**（@SpringBootApplication, @RestController, @Service, @Autowired 等）
4. **使用 Javalin 路由**替代 Spring MVC Controller
5. **手动配置 langchain4j 模型**，不使用 Spring Boot Starter

## 标准 pom.xml 模板

```xml
<dependencies>
    <!-- For Langchain4j Common -->
    <dependency>
        <groupId>io.github.partmeai</groupId>
        <artifactId>langchain4j-common</artifactId>
        <version>${revision}</version>
    </dependency>
    <!-- For Chat Completion & Embedding -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-{provider}</artifactId>
    </dependency>
    <!-- For Javalin -->
    <dependency>
        <groupId>io.javalin</groupId>
        <artifactId>javalin-bundle</artifactId>
    </dependency>
    <!-- For Test -->
    <dependency>
        <groupId>io.javalin</groupId>
        <artifactId>javalin-testtools</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>io.github.partmeai.{module}.Application</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## 标准 Application 类模板

```java
package io.github.partmeai.{module};

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class Application {
    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.jsonMapper(new JavalinJackson());
            javalinConfig.showJavalinBanner = false;
        });
        
        // 注册路由
        // Router.register(app, config);
        
        app.start(8080);
    }
}
```

