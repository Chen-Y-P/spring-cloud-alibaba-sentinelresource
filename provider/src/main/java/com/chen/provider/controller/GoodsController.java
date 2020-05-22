package com.chen.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goods")
public class GoodsController {

    @GetMapping("buy/{name}/{count}")
    public String buy(@PathVariable String name, @PathVariable Integer count) {
        return String.format("购买%d份%s", count, name);
    }
}