/**
 * 
 */
package com.avc.mis.beta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/zvi")
public class HelloworldController {
  @GetMapping("/")
  public String hello() {
    return "Hello world - springboot-appengine-standard!";
  }
}