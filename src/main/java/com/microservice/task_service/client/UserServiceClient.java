package com.microservice.task_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservice.task_service.model.dto.MeTestResponse;

/*@FeignClient(
    name = "user-ms",
    url = "http://localhost:8071" ,
    configuration = FeignAuthConfig.class
)*/
@FeignClient(
  name = "user-directory",
  url = "${USER_MS_BASE_URL:http://user-ms:8071}", // default Docker
  configuration = FeignAuthConfig.class
)
public interface UserServiceClient {

  @GetMapping("/me/test")
  MeTestResponse test(@RequestParam("kcIss") String kcIss,
                      @RequestParam("kcSub") String kcSub);

  @GetMapping("me/friends/ids")
   List<Long> getMyFriendIds();
}