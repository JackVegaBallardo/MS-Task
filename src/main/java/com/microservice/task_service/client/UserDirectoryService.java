package com.microservice.task_service.client;


import org.springframework.stereotype.Service;

import com.microservice.task_service.model.dto.MeTestResponse;

@Service
public class UserDirectoryService {

  private final UserServiceClient client;

  public UserDirectoryService(UserServiceClient client) {
    this.client = client;
  }

  public Long resolveLocalUserIdOrThrow(String kcIss, String kcSub) {
    MeTestResponse resp = client.test(kcIss, kcSub);
    if (resp == null || resp.localUserId() == null) {
      throw new IllegalStateException("No se pudo resolver localUserId");
    }
    return resp.localUserId();
  }
}