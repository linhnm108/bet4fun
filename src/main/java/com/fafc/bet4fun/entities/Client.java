package com.fafc.bet4fun.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fafc.bet4fun.common.Constants;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="client")
public class Client implements UserDetails {

    private static final long serialVersionUID = 950514943286864670L;

    @Transient
    private String [] strRoles;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="client_id")
    private UUID clientId;

    @Column(name = "client_name")
    private String clientName;

    private String password;

    private double balance;

    @OneToMany(mappedBy="client")
    private List<Handicap> handicaps;

    @OneToMany(mappedBy="client")
    private List<Bet> bets;

    @ManyToMany(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
    @JoinTable(
            name="client_role_link",
            joinColumns = { @JoinColumn(name="client_id") },
            inverseJoinColumns = { @JoinColumn(name="role_id") })
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : this.roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return this.clientName;
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

    public String getStringRoles() {
        List <String> roles = new ArrayList<>();
        for (Role role : this.roles) {
            roles.add(role.getRoleName());
        }
        return Arrays.toString(roles.toArray());
    }

    public boolean isAdmin() {
        return this.getStringRoles().contains(Constants.ADMIN_ROLE);
    }

    public boolean isBookie() {
        return this.getStringRoles().contains(Constants.BOOKIE_ROLE);
    }

    public boolean isPunter() {
        return this.getStringRoles().contains(Constants.PUNTER_ROLE);
    }
}