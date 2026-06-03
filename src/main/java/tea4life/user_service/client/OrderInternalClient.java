package tea4life.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tea4life.user_service.dto.base.ApiResponse;
import tea4life.user_service.dto.request.driver.UpsertDriverRequest;
import tea4life.user_service.dto.response.driver.DriverResponse;

@FeignClient(
        name = "TEA4LIFE-ORDER-SERVICE",
        url = "${service.url.order-internal:}",
        path = "/internal/drivers"
)
public interface OrderInternalClient {

    @PostMapping("/sync")
    ApiResponse<DriverResponse> syncDriver(@RequestBody UpsertDriverRequest request);
}
