package com.microservice.task_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservice.task_service.model.dto.MeTestResponse;

@FeignClient(
    name = "user-ms",
    url = "http://localhost:8071" ,
    configuration = FeignAuthConfig.class
)
public interface UserServiceClient {

  @GetMapping("/me/test")
  MeTestResponse test(@RequestParam("kcIss") String kcIss,
                      @RequestParam("kcSub") String kcSub);
}