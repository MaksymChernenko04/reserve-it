package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user in the system.
 * <p>
 * Has a relationship with {@link Role} and stores user email, password, first and last name.
 */
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    /**
     * Instantiates a new User.
     *
     * @param role      the user role
     * @param email     the user email
     * @param password  the user password
     * @param active    the user activity
     * @param firstName the user first name
     * @param lastName  the user last name
     */
    public User(Role role,
                String email,
                String password,
                Boolean active,
                String firstName,
                String lastName) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
