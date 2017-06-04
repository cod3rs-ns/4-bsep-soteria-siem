package bsep.sw.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends EntityMeta {

    @NotNull
    @Column(name = "u_username", unique = true, nullable = false, length = 20)
    @Size(min = 3, max = 20)
    private String username;

    @NotNull
    @Email
    @Column(name = "u_email", unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(name = "u_password", nullable = false)
    @Size(min = 6)
    private String password;

    @NotNull
    @Column(name = "u_first_name", nullable = false, length = 20)
    @Size(min = 2, max = 20)
    private String firstName;

    @NotNull
    @Column(name = "u_last_name", nullable = false, length = 30)
    @Size(min = 2, max = 30)
    private String lastName;

    @Column(name = "u_phone", length = 16)
    @Size(max = 16)
    private String phoneNumber;

    @NotNull
    @Column(name = "u_role", nullable = false)
    private UserRole role;

    @Column(name = "u_image")
    private String imagePath;

    @Column(name = "u_verified", nullable = false)
    private Boolean verified = false;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Project> projects = new HashSet<>(0);

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Project> ownedProjects = new HashSet<>(0);

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public User role(UserRole role) {
        this.role = role;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public User verified(Boolean verified) {
        this.verified = verified;
        return this;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public User projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Set<Project> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    public User owned(Set<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof User)) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(username, user.username)
                .append(email, user.email)
                .append(password, user.password)
                .append(firstName, user.firstName)
                .append(lastName, user.lastName)
                .append(phoneNumber, user.phoneNumber)
                .append(role, user.role)
                .append(imagePath, user.imagePath)
                .append(verified, user.verified)
                .append(ownedProjects, user.ownedProjects)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(username)
                .append(email)
                .append(password)
                .append(firstName)
                .append(lastName)
                .append(phoneNumber)
                .append(role)
                .append(imagePath)
                .append(verified)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", verified=" + verified +
                '}';
    }
}