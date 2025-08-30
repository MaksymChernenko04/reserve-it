package com.maksymchernenko.reserveit.model.dto;

import com.maksymchernenko.reserveit.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object that represents {@link User} with only first and last name.
 * <p>
 * Used for editing user profile.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
}
