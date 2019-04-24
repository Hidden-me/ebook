package org.ebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EbookApplication {

    public static void main(String[] args) {
        try{
            ClassLoader.getSystemClassLoader().loadClass("org.ebook.DatabaseUtils");
        }catch(Exception e){
            e.printStackTrace();
        }
        SpringApplication.run(EbookApplication.class, args);
    }

}
