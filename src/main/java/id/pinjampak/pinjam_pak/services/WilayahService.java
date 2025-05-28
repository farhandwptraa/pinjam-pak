package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.models.Branch;
import id.pinjampak.pinjam_pak.models.City;
import id.pinjampak.pinjam_pak.models.Province;
import id.pinjampak.pinjam_pak.repositories.BranchRepository;
import id.pinjampak.pinjam_pak.repositories.CityRepository;
import id.pinjampak.pinjam_pak.repositories.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WilayahService {

    private final CityRepository cityRepository;
    private final ProvinceRepository provinceRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public WilayahService(CityRepository cityRepository, ProvinceRepository provinceRepository, BranchRepository branchRepository) {
        this.cityRepository = cityRepository;
        this.provinceRepository = provinceRepository;
        this.branchRepository = branchRepository;
    }

    public List<City> getCitiesByProvinceId(Long provinceId) {
        return cityRepository.findByProvinceId(provinceId);
    }

    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }

    @Transactional
    public void updateBranchProvinces(UUID branchId, List<Long> provinceIds) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Ambil hanya provinsi yang dikirimkan
        List<Province> toAssign = provinceRepository.findAllById(provinceIds);
        if (toAssign.size() != provinceIds.size()) {
            throw new RuntimeException("Beberapa provinsi tidak ditemukan");
        }

        // Assign mereka ke branch
        toAssign.forEach(p -> p.setBranch(branch));
        provinceRepository.saveAll(toAssign);
    }

    public List<Province> getProvincesByBranchId(UUID branchId) {
        // validasi cabang ada atau tidak
        branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        return provinceRepository.findByBranchBranchId(branchId);
    }
}

