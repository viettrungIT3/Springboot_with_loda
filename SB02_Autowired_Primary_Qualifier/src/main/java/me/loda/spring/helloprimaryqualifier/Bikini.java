package me.loda.spring.helloprimaryqualifier;

import org.springframework.stereotype.Component;

@Component("bikini")
public class Bikini implements Outfit {
    @Override
    public void wear() {
        System.out.println("Mặc bikini");
    }
}
