package tea4life.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.FileMoveRequest;

/**
 * Admin 2/8/2026
 *
 **/
@FeignClient(name = "TEA4LIFE-STORAGE-SERVICE", url = "${openfeign.storage.url}")
public interface StorageClient {

    @PostMapping("/storage/confirm")
    ApiResponse<String> confirmFile(@RequestBody FileMoveRequest request);

}
