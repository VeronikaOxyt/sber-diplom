package com.oxytoca.app.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity-класс пользователя.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullname;
    @NotBlank(message = "Пожалуйста, заполните поле")
    private String email;
    @NotBlank(message = "Пожалуйста, заполните поле")
    private String username;
    @NotBlank(message = "Пожалуйста, заполните поле")
    private String password;
    private boolean active;
    private String activationCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "activity_participant",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    private Set<Activity> myActivities = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private Set<Activity> authorsActivities;

    public User() {

    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username,
                String password, Set<Role> roles,
                Set<Activity> authorsActivities,
                boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorsActivities = authorsActivities;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
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
        return isActive();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Activity> getMyActivities() {
        return myActivities;
    }

    public void setMyActivities(Set<Activity> myActivities) {
        this.myActivities = myActivities;
    }

    public void removeActivity(Activity activity) {
        this.myActivities.remove(activity);
        activity.getParticipants().remove(this);
    }


    public Set<Activity> getAuthorsActivities() {
        return authorsActivities;
    }

    public void setAuthorsActivities(Set<Activity> authorsActivities) {
        this.authorsActivities = authorsActivities;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
