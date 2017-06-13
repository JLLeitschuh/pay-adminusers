package uk.gov.pay.adminusers.persistence.dao;

import org.junit.Before;
import org.junit.Test;
import uk.gov.pay.adminusers.fixtures.UserDbFixture;
import uk.gov.pay.adminusers.model.User;
import uk.gov.pay.adminusers.persistence.entity.ServiceRoleEntity;
import uk.gov.pay.adminusers.persistence.entity.UserServiceId;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ServiceRoleDaoTest extends DaoTestBase {

    private ServiceRoleDao serviceRoleDao;

    @Before
    public void before() throws Exception {
        serviceRoleDao = env.getInstance(ServiceRoleDao.class);
    }

    @Test
    public void shouldRemoveAServiceRoleOfAUserSuccessfully() throws Exception {

        User user = UserDbFixture.userDbFixture(databaseHelper).insertUser();
        UserServiceId userServiceId = new UserServiceId();
        userServiceId.setServiceId(user.getServices().get(0).getId());
        userServiceId.setUserId(user.getId());

        List<Map<String, Object>> serviceRoles = databaseHelper.findServiceRoleForUser(user.getId());
        assertThat(serviceRoles.size(), is(1));

        ServiceRoleEntity serviceRoleOfUser = serviceRoleDao.findById(userServiceId).get();

        serviceRoleDao.remove(serviceRoleOfUser);

        List<Map<String, Object>> serviceRolesAfterRemove = databaseHelper.findServiceRoleForUser(user.getId());

        assertThat(serviceRolesAfterRemove.size(), is(0));
    }
}
