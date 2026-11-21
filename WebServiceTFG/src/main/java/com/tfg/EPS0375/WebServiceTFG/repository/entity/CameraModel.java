package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "camera_model")
public class CameraModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Short id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "transmitted_image_format", nullable = false)
    private String transmittedImageFormat;

    public CameraModel(String name, String transmittedImageFormat) {
        this.name = name;
        this.transmittedImageFormat = transmittedImageFormat;
    }
}
