package com.graduates.test.resposity;

import com.graduates.test.model.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributorResposity extends JpaRepository<Distributor,Integer> {

}
