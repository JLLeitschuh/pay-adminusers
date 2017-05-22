package uk.gov.pay.adminusers.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
public class Invite {

    private final String email;
    private String telephoneNumber;
    private Boolean disabled = Boolean.FALSE;
    private Integer loginCounter = 0;

    private List<Link> links = new ArrayList<>();

    public Invite(String email) {
        this.email = email;
    }

    public Invite(String email, String telephoneNumber,
                  Boolean disabled, Integer loginCounter) {
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.disabled = disabled;
        this.loginCounter = loginCounter;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("telephone_number")
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    @JsonProperty("disabled")
    public Boolean isDisabled() {
        return disabled;
    }

    @JsonProperty("login_counter")
    public Integer getLoginCounter() {
        return loginCounter;
    }

    @JsonProperty("_links")
    public List<Link> getLinks() {
        return links;
    }

    public void setInviteLink(String targetUrl) {
        Link inviteLink = Link.from(Link.Rel.invite, "GET", targetUrl);
        this.links = ImmutableList.of(inviteLink);
    }
}
