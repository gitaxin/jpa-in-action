### JPA 简介
#### 什么是JPA
-	JPA（Java Persistence API）是用于管理Java EE和Java SE环境中的持久化，以及对象/ 关系映射的Java API
-	最新规范为 "JSR 338: Java Persistence 2.1" [https://jcp.org/en/jsr/detail?id=338](https://jcp.org/en/jsr/detail?id=338)
-	实现：	 EclipseLink、Hibernate、Apache OpenJPA
#### 实体
-	实体表示关系数据库中的表
-	每个实体实例对应于该表中的行
-	类必须用javax.persistence.Entity注解
-	类必须有一个public或protected的无参数的构造函数
-	实体实例被当作值以分离对象方式进行传递（例如通过会话bean的远程业务接口）则该类必须实现Serializable接口
-	唯一的对象标识符： 简单主键（Javax.persistence.Id）、复合主键（javax.persistence.EmbeddedId和javax.persistence.IdClass）
#### 关系
-	一对一 ：@OneToOne
-	一对多 ：@OneToMany
-	多对一 ：@ManyToOne
-	多对多 ：@ManyToMany
#### EntityManager接口
-	定义用于与持久性上下文进行交互的方法
-	创建和删除持久实体实例，通过实体的主键查找实体
-	允许在实体上运行查询

获取EntityManager实例

```
		@persistenceUnit
		EntityManagerFactory emf;
		EntityManager em;
		@Resource
		UserTransaction utx;
		...
		em = emf.createEntityManager();
		try{
			utx.begin();
			em.persist(SomeEntity)	;
			em.merge(AnotherEntity);
			em.remove(ThirdEntity);
			utx.commit;
		}catch(Exception e){
			utx.rollback();
		}
```
查找实体

```
	@PersistenceContext
	EntityManager em;
	public void enterOrder(int custID, CustomerOrder newOrder){
			Customer cust = em.find(Customer.class,custID);
			cust.getOrders().add(newOrder);
			newOrder.setCustomer(cust);
	}
```

### Spring Data JPA 用法介绍 
#### 什么是Spring Data JPA
- 是更大的Spring Data家族的一部分
- 对基于JPA的数据访问层的增强支持
- 更容易构建基于使用Spring数据访问技术栈的应用程序
#### Spring Data JPA常用接口
##### CrudRepository

```
	public interface CrudRepository<T, ID extends Serializable>
		extends Repository<T, ID>{
		<S extends T> S save(S entity);

		T findOne(ID primaryKey);

		Iterable<T> findAll();
		
		Long count();

		void delete(T entity);

		boolean exists (ID primaryKey);

		//... more functionality 
	
}
```
##### PagingAndSortingRepository

```
	public interface PagingAndSortingRepository<T, ID extends Serializable>
		 extendsCrudRepository<T, ID>{
		 
			Iterable<T> findAll(Sort sort);
			
			Page<T> findAll(Pageable pageable);

	}
```
##### Spring Data JPA 自定义接口
######	根据方法名创建查询
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191217215009257.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTU0MTY1Nw==,size_16,color_FFFFFF,t_70)
	

### Spring Data JPA、Hibernate 与 Spring Boot 集成

#### 配置环境
-	MySQL Community Server 5.7.17
-	Spring Data JPA 1.11.1 RELEASE
-	MySQL Connector Java 5.1.32
#### 修改pom.xml

```
<!-- 引入spring-boot-starter-data-jpa后，会自动引入hibernate、spring-data-jpa、spring orm-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.32</version>
        </dependency>
         <!--添加此依赖后，会自动使用这个内存数据库，可以不用设置数据源，如果设置了数据源则使用数据源设置的数据库-->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.193</version>
        </dependency>
```

###	数据持久化实战

pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>cn.giteasy</groupId>
    <artifactId>jpa-in-action</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>jpa-in-action</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

       <!-- 引入spring-boot-starter-data-jpa后，会自动引入hibernate、spring-data-jpa、spring orm-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.32</version>
        </dependency>
        <!--添加此依赖后，会自动使用这个内存数据库，可以不用设置数据源，如果设置了数据源则使用数据源设置的数据库-->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.193</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

实体类

```
package cn.giteasy.action.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Axin in 2019/12/16 19:59
 */
@Entity//实体
public class User {

    @Id//主键
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer age;


    protected User() {

    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

```
UserRepository 接口

```
package cn.giteasy.action.reposition;

import cn.giteasy.action.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Axin in 2019/12/16 20:02
 *
 * 不用写实现类，JPA已经有实现类了
 */
public interface UserRepository extends CrudRepository<User,Long> {


}

```
UserController 控制器

```
package cn.giteasy.action.controller;

import cn.giteasy.action.entity.User;
import cn.giteasy.action.reposition.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

/**
 * Created by Axin in 2019/12/16 21:44
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Controller
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserRepository userRepository;





    /**
     * 查询所用用户
     * @return
     */
    @GetMapping
    public ModelAndView list(Model model) {
        model.addAttribute("userList", userRepository.findAll());
        model.addAttribute("title", "用户管理");
        return new ModelAndView("users/list", "userModel", model);
    }

    /**
     * 根据id查询用户
     * @return
     */
    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        User user1 = user.get();
        model.addAttribute("user", user1);
        model.addAttribute("title", "查看用户");
        return new ModelAndView("users/view", "userModel", model);
    }

    /**
     * 获取 form 表单页面
     * @return
     */
    @GetMapping("/form")
    public ModelAndView createForm(Model model) {
        model.addAttribute("user", new User(null,null));
        model.addAttribute("title", "创建用户");
        return new ModelAndView("users/form", "userModel", model);
    }

    /**
     * 新建用户
     * @param user
     * @return
     */
    @PostMapping
    public ModelAndView create(User user) {
        user = userRepository.save(user);
        return new ModelAndView("redirect:/users");
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id, Model model) {
        userRepository.deleteById(id);

        model.addAttribute("userList", userRepository.findAll());
        model.addAttribute("title", "删除用户");
        return new ModelAndView("users/list", "userModel", model);
    }

    /**
     * 修改用户
     * @return
     */
    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        User user1 = user.get();
        model.addAttribute("user", user1);
        model.addAttribute("title", "修改用户");
        return new ModelAndView("users/form", "userModel", model);
    }



}

```
配置文件

```

spring.thymeleaf.encoding=utf-8
#热部署静态文件
spring.thymeleaf.cache=false
#使用HTML5标准
spring.thymeleaf.mode=HTML5

#启用H2控制台
spring.h2.console.enabled=true

#DataSource
spring.datasource.url=jdbc:mysql://localhost/blog?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

#JPA
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create-drop
```
