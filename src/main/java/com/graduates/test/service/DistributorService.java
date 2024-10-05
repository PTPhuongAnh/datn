package com.graduates.test.service;

import com.graduates.test.model.Distributor;
import org.springframework.data.domain.Page;


import java.util.List;

public interface DistributorService {
    public String createDistributor(Distributor distributor);
    public String updateDistributor(Distributor distributor);
    public String deleteDistributor(Integer idDistributor);
    public Distributor getDistributor(Integer idDistributor);
    public List<Distributor> getAllDistributor();

    Distributor findById(Integer IdDistributor);

    Page<Distributor> getList(String nameDistributor, String addressDistributor, int page, int size);

    void markDistributorAsDeleted(Integer idDistributor);
}
