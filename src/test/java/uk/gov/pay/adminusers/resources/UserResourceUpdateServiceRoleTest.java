package uk.gov.pay.adminusers.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import uk.gov.pay.adminusers.model.Role;

import static com.jayway.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static uk.gov.pay.adminusers.fixtures.RoleDbFixture.roleDbFixture;
import static uk.gov.pay.adminusers.fixtures.ServiceDbFixture.serviceDbFixture;
import static uk.gov.pay.adminusers.fixtures.UserDbFixture.userDbFixture;

public class UserResourceUpdateServiceRoleTest extends IntegrationTest {

    @Test
    public void shouldUpdateUserServiceRole() throws Exception {

        Role role = roleDbFixture(databaseHelper).insertAdmin();
        int serviceId = serviceDbFixture(databaseHelper).insertService();
        String username = userDbFixture(databaseHelper).withServiceRole(serviceId, role.getId()).insertUser().getUsername();
        userDbFixture(databaseHelper).withServiceRole(serviceId, role.getId()).insertUser();

        JsonNode payload = new ObjectMapper().valueToTree(ImmutableMap.of("role_name", "view-and-refund"));

        givenSetup()
                .when()
                .contentType(JSON)
                .body(payload)
                .put(format(USER_SERVICE_RESOURCE, username, serviceId))
                .then()
                .statusCode(200)
                .body("username", is(username))
                .body("role.name", is("view-and-refund"))
                .body("role.description", is("View and Refund"));
    }


    @Test
    public void shouldError404_ifUserNotFound_whenUpdatingServiceRole() throws Exception {

        int serviceId = serviceDbFixture(databaseHelper).insertService();
        JsonNode payload = new ObjectMapper().valueToTree(ImmutableMap.of("role_name", "view-and-refund"));

        givenSetup()
                .when()
                .contentType(JSON)
                .body(payload)
                .put(format(USER_SERVICE_RESOURCE, "non-existent", serviceId))
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldError412_ifNoOfMinimumAdminsLimitReached_whenUpdatingServiceRole() throws Exception {

        Role role = roleDbFixture(databaseHelper).insertAdmin();
        int serviceId = serviceDbFixture(databaseHelper).insertService();
        String username = userDbFixture(databaseHelper).withServiceRole(serviceId, role.getId()).insertUser().getUsername();

        JsonNode payload = new ObjectMapper().valueToTree(ImmutableMap.of("role_name", "view-and-refund"));

        givenSetup()
                .when()
                .contentType(JSON)
                .body(payload)
                .put(format(USER_SERVICE_RESOURCE, username, serviceId))
                .then()
                .statusCode(412)
                .body("errors", hasSize(1))
                .body("errors[0]", is("Service admin limit reached. At least 1 admin(s) required"));
    }
}
