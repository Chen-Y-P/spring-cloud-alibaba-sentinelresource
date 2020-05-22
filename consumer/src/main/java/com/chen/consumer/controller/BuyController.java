package com.chen.consumer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BuyController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("buy/{name}/{count}")
    @SentinelResource(value = "buy", fallback = "buyFallback", blockHandler = "buyBlock")
    public String buy(@PathVariable String name, @PathVariable Integer count) {
        if (count >= 20) {
            throw new IllegalArgumentException("购买数量过多");
        }
        if ("miband".equalsIgnoreCase(name)) {
            throw new NullPointerException("已售罄");
        }
        Map<String, Object> params = new HashMap<>(2);
        params.put("name", name);
        params.put("count", count);
        return this.restTemplate.getForEntity("http://provider/goods/buy/{name}/{count}", String.class, params).getBody();
    }

    // 异常回退
    public String buyFallback(@PathVariable String name, @PathVariable Integer count, Throwable throwable) {
        return String.format("【进入fallback方法】购买%d份%s失败，%s", count, name, throwable.getMessage());
    }

    // sentinel回退
    public String buyBlock(@PathVariable String name, @PathVariable Integer count, BlockException e) {
        return String.format("【进入blockHandler方法】购买%d份%s失败，当前购买人数过多，请稍后再试", count, name);
    }
}