package com.tfg.EPS0375.WebServiceTFG.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceRequestDto {
    @JsonProperty("cameraModel")
    @NotBlank
    private String cameraModel;

    @JsonProperty("frequencyImage")
    @NotNull
    private Integer frequencyImage;

    @JsonProperty("transmittedImageFormat")
    @NotBlank
    private String transmittedImageFormat;

    @JsonProperty("macAddress")
    @NotBlank
    private String macAddress;

    @JsonProperty("latitude")
    @NotNull
    private Double latitude;

    @JsonProperty("longitude")
    @NotNull
    private Double longitude;
}
