package com.robocubs4205.cubscout.model.access;

import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class User implements UserDetails {

    private long id;

    @NotNull
    private String username;

    private Set<GrantedAuthorityImpl> authorities = new HashSet<>();

    private SidImpl sid;

    /**
     * even though the field is called {@literal password}, the password is
     * not stored directly. This field contains the hash.
     * This naming is to match the naming of the UserDetails interface
     * provided by Spring.
     */

    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Sid asSid() {
        return new UserSid(this);
    }

    public SidImpl getSid() {
        return sid;
    }

    public void setSid(SidImpl sid) {
        this.sid = sid;
    }

    private class UserSid extends User implements Sid {

        private final User user;

        public UserSid(User user) {

            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            return !(o == null) && o instanceof UserSid && ((UserSid) o).user.getId() == getId();
        }
    }

}
