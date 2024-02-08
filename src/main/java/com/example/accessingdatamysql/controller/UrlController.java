package com.example.accessingdatamysql.controller;

import com.example.accessingdatamysql.DTO.urlDto;
import com.example.accessingdatamysql.Entity.Url;
import com.example.accessingdatamysql.Repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

@RestController
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;


    @GetMapping("/ping")
    public String healthEndpoint() {
        return "pong";
    }


    @PostMapping("/url")
    private String generateUrl(@RequestBody urlDto payload) {
        try {
            Url ur = new Url();
            ur.setName(payload.getName());
            ur.setUrl(payload.getUrl());

            if (payload.getKey() == null) {
                ur.setUkey(UUID.randomUUID().toString());
            } else {
                Optional<Url> keyName = urlRepository.findKey(payload.getKey());
                if (keyName.isPresent()) {
                    System.out.println("this is empty");
                    return "select another key";
                } else {
                    ur.setUkey(payload.getKey());
                }
            }

            urlRepository.saveUrl(ur);
            Optional<Url> result = urlRepository.findKey(ur.getUkey());
            System.out.println(result.get().getId());

            return "http://localhost:8080/"+result.get().getUkey();

        } catch (Exception e) {
            System.out.println(e);
        }
        return "success";
    }

    @GetMapping("/{key}")
    public ModelAndView redirect(@PathVariable("key") String key){

        System.out.println(key);
        Optional<Url> mask = urlRepository.findKey(key);

        if(mask.isEmpty()){
            System.out.println("does not exits");
        }
        return new ModelAndView("redirect:" + mask.get().getUrl());

    }

    @GetMapping("/url")
    public Iterable<Url> getUrlList(){
       return urlRepository.findall();
    }

    @DeleteMapping("/url/{key}")
    public String deleteUrlList(@PathVariable("key") String key){
         urlRepository.delete(key);
        return "sucess";
    }





}
