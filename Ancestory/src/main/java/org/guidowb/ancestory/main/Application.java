package org.guidowb.ancestory.main;

import java.io.IOException;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.guidowb")
@EnableAutoConfiguration
public class Application {

	private static Gedcom gedcom;

    public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: AncestoryServer <gedfile>");
			System.exit(1);
		}
		gedcom = GedcomReader.load(args[0]);
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public Gedcom getGedcom() {
    	return gedcom;
    }
}
