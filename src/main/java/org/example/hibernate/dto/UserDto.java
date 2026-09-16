package org.example.hibernate.dto;

public record UserDto(Long id,
                      String name,
                      String email,
                      Integer age) {
}
