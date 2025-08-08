package com.hongsite.jisu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 极速外贸管理系统启动类
 * 
 * @author 鸿思特科技有限公司
 * @contact hst_co@163.com
 * @phone 17600198256
 */
@SpringBootApplication
@MapperScan("com.hongsite.jisu.mapper")
public class JisuForeignTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JisuForeignTradeApplication.class, args);
        System.out.println("===========================================");
        System.out.println("极速外贸管理系统启动成功！");
        System.out.println("鸿思特科技有限公司");
        System.out.println("联系邮箱：hst_co@163.com");
        System.out.println("联系电话：17600198256");
        System.out.println("API文档：http://localhost:8080/doc.html");
        System.out.println("===========================================");
    }
}