package com.graduates.test.resposity;

import com.graduates.test.model.Category;
import com.graduates.test.model.Distributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistributorResposity extends JpaRepository<Distributor,Integer> {
    List<Distributor> findByDeletedFalse();
    Page<Distributor> findByNameDistributorContainingOrAddressContaining(String nameDistributor,String address, Pageable pageable);
    Page<Distributor> findAllByDeletedFalse(Pageable pageable);
}
