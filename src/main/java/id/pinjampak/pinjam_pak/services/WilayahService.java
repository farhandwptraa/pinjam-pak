package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.models.City;
import id.pinjampak.pinjam_pak.models.Province;
import id.pinjampak.pinjam_pak.repositories.CityRepository;
import id.pinjampak.pinjam_pak.repositories.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WilayahService {

    private final CityRepository cityRepository;
    private final ProvinceRepository provinceRepository;

    @Autowired
    public WilayahService(CityRepository cityRepository, ProvinceRepository provinceRepository) {
        this.cityRepository = cityRepository;
        this.provinceRepository = provinceRepository;
    }

    public List<City> getCitiesByProvinceId(Long provinceId) {
        return cityRepository.findByProvinceId(provinceId);
    }

    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
}

