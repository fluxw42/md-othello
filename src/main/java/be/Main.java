/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Mark Dechamps
 */
@ComponentScan
@EnableAutoConfiguration
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}
