package com.bfxx.ioconfig;

import com.bfxx.netty.Server;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Properties;


public class ReadConfig {
    public static Properties readConfig(Class c1,String configNameFile) throws IOException {
//        InputStream resourceAsStream = c1.getClassLoader().getResourceAsStream(configNameFile);
//        InputStream inputStream = new BufferedInputStream(new FileInputStream(configNameFile));
        ClassPathResource resource = new ClassPathResource(configNameFile);
        InputStream inputStream = resource.getInputStream();
        Properties pro = new Properties();
        pro.load(inputStream);
        return pro;



    }

}
