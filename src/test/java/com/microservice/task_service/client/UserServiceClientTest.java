package com.microservice.task_service.client;

import com.microservice.task_service.model.dto.MeTestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceClientTest {

    @Mock
    private UserServiceClient client;

    @InjectMocks
    private UserDirectoryService userDirectoryService;

    @Test
    void resolveLocalUserIdOrThrow_ValidResponseTest() {
        // Given
        String kcIss = "http://test-issuer";
        String kcSub = "test-user";
        Long expectedUserId = 100L;
        MeTestResponse validResponse = new MeTestResponse(expectedUserId, kcIss, kcSub);

        when(client.test(kcIss, kcSub)).thenReturn(validResponse);

        // When
        Long result = userDirectoryService.resolveLocalUserIdOrThrow(kcIss, kcSub);

        // Then
        assertEquals(expectedUserId, result);
        verify(client, times(1)).test(kcIss, kcSub);
    }

    @Test
    void resolveLocalUserIdOrThrow_NullResponse_ShouldThrowIllegalStateException() {
        // Given
        String kcIss = "http://test-issuer";
        String kcSub = "test-user";

        when(client.test(kcIss, kcSub)).thenReturn(null);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDirectoryService.resolveLocalUserIdOrThrow(kcIss, kcSub);
        });

        assertEquals("No se pudo resolver localUserId", exception.getMessage());
        verify(client, times(1)).test(kcIss, kcSub);
    }

    @Test
    void resolveLocalUserIdOrThrow_ResponseWithNullLocalUserId_ShouldThrowIllegalStateException() {
        // Given
        String kcIss = "http://test-issuer";
        String kcSub = "test-user";
        MeTestResponse responseWithNullId = new MeTestResponse(null, kcIss, kcSub);

        when(client.test(kcIss, kcSub)).thenReturn(responseWithNullId);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userDirectoryService.resolveLocalUserIdOrThrow(kcIss, kcSub);
        });

        assertEquals("No se pudo resolver localUserId", exception.getMessage());
        verify(client, times(1)).test(kcIss, kcSub);
    }
}
