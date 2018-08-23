package uk.gov.pay.adminusers.persistence.entity;

import org.junit.Test;
import uk.gov.pay.adminusers.persistence.entity.service.ServiceNameEntity;
import uk.gov.pay.adminusers.persistence.entity.service.SupportedLanguage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServiceEntityTest {
    @Test
    public void addOrUpdateServiceName_shouldUpdateNameWhenAddingEnName_andNoPreviousEnName() {
        ServiceNameEntity serviceNameEntity = ServiceNameEntity.from(SupportedLanguage.ENGLISH, "newest-en-name");
        ServiceEntity serviceEntity = ServiceEntityBuilder.aServiceEntity().withName("old-en-name").build();

        assertThat(serviceEntity.getName(), is("old-en-name"));
        assertThat(serviceEntity.getServiceNames().size(), is(0));

        serviceEntity.addOrUpdateServiceName(serviceNameEntity);

        assertThat(serviceEntity.getName(), is("newest-en-name"));
        assertThat(serviceEntity.getServiceNames().size(), is(1));
        assertThat(serviceEntity.getServiceNames().get(SupportedLanguage.ENGLISH).getName(), is("newest-en-name"));
    }
}
