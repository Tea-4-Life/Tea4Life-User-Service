package tea4life.user_service.dto.response.driver;

public record DriverResponse(
        String id,
        String keycloakId,
        String fullName,
        String phone
) {
}
