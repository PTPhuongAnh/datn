package com.graduates.test.service.impl;

import com.graduates.test.dto.DistributorRespone;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Category;
import com.graduates.test.model.Distributor;
import com.graduates.test.resposity.BookCategoryResposity;
import com.graduates.test.resposity.DistributorResposity;
import com.graduates.test.service.DistributorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public String deleteDistributor(Integer idDistributor) {
        return null;
    }

//    @Override
//    public String deleteDistributor(Integer idDistributor) {
//        if (idDistributorUsed(idDistributor)) {
//            throw new IllegalStateException("Cannot delete category. It is used in one or more books.");
//        }
//       distributorResposity.deleteById(idDistributor);
//        return "Category deleted successfully.";
//    }

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
//    public boolean idDistributorUsed(Integer idPublisher) {
//        return bookCategoryResposity.existsByPublisher_IdPublisher(idPublisher);
//    }

    public boolean idDistributorUsed(Integer idDistributor) {
        return bookCategoryResposity.existsByDistributor_IdDistributor(idDistributor);
    }
    public void markDistributorAsDeleted(Integer idDistributor) {
        if (idDistributorUsed(idDistributor)) {
            throw new IllegalStateException("Cannot delete distributor. It is used in one or more books.");
        }

        Optional<Distributor> distributorOptional = distributorResposity.findById(idDistributor);
        if (!distributorOptional.isPresent()) {
            throw new IllegalStateException("Distributor not found.");
        }

        Distributor distributor = distributorOptional.get();
        distributor.setDeleted(true); // Đánh dấu là đã bị xóa
        distributorResposity.save(distributor); // Lưu thay đổi
    }

    public Page<Distributor> getList(String nameDistributor,String address, int page, int sizes) {
        Pageable pageable = PageRequest.of(page, sizes);
//        return categoryResposity.searchCategory(nameCategory,pageable);
        if (nameDistributor != null && !nameDistributor.isEmpty()||address!=null&&address.isEmpty()) {
            return distributorResposity.findByNameDistributorContainingOrAddressContaining(nameDistributor,address, pageable);
        } else {
            return distributorResposity.findAllByDeletedFalse(pageable);
        }
    }
}
