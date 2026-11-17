package com.tfg.EPS0375.WebServiceTFG.repository;

import com.tfg.EPS0375.WebServiceTFG.repository.entity.AlertImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface IAlertImageRepository extends JpaRepository<AlertImage, UUID> {
}
