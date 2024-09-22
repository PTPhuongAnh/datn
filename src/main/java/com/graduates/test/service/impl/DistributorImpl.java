package com.graduates.test.service.impl;

import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Distributor;
import com.graduates.test.resposity.BookCategoryResposity;
import com.graduates.test.resposity.DistributorResposity;
import com.graduates.test.service.DistributorService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DistributorImpl implements DistributorService {
    private DistributorResposity distributorResposity;
    private BookCategoryResposity bookCategoryResposity;

    public DistributorImpl(DistributorResposity distributorResposity, BookCategoryResposity bookCategoryResposity) {
        this.distributorResposity = distributorResposity;
        this.bookCategoryResposity = bookCategoryResposity;
    }

    @Override
    public String createDistributor(Distributor distributor) {
        distributorResposity.save(distributor);
        return "create distributor success";
    }

    @Override
    public String updateDistributor(Distributor distributor) {
        distributorResposity.save(distributor);
        return "update distributor success";
    }
    public boolean idDistributorUsed(Integer idPublisher) {
        return bookCategoryResposity.existsByPublisher_IdPublisher(idPublisher);
    }
    @Override
    public String deleteDistributor(Integer idDistributor) {
        if (idDistributorUsed(idDistributor)) {
            throw new IllegalStateException("Cannot delete category. It is used in one or more books.");
        }
       distributorResposity.deleteById(idDistributor);
        return "Category deleted successfully.";
    }

    @Override
    public Distributor getDistributor(Integer idDistributor) {
        return  distributorResposity.findById(idDistributor)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with ID " + idDistributor + " not found"));

    }

    @Override
    public List<Distributor> getAllDistributor() {
        return distributorResposity.findAll();
    }

    @Override
    public Distributor findById(Integer idDistributor) {
        return distributorResposity.findById(idDistributor)
                .orElse(null); // Trả về null nếu không tìm thấy
    }
}
