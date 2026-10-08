package org.example.kafka.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class MetaApiResponse<T> {

    @NotBlank(message = "hostPortMessage can't be blank")
    private String hostPortMessage;

    private T dto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaApiResponse<?> that = (MetaApiResponse<?>) o;
        return Objects.equals(hostPortMessage, that.hostPortMessage)
                && Objects.equals(dto, that.dto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostPortMessage, dto);
    }

    @Override
    public String toString() {
        return "MetaApiResponse{" +
                "hostPortMessage='" + hostPortMessage + '\'' +
                ", dto=" + dto +
                '}';
    }
}
