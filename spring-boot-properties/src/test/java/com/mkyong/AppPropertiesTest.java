package com.mkyong;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringBootWebApplication.class})
public class AppPropertiesTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    AppProperties appProperties;

    @Autowired
    DataDict dataDict;

    @Test
    public void test() {
        System.out.println(appProperties.getMenus());

        System.out.println(dataDict);
    }
}
