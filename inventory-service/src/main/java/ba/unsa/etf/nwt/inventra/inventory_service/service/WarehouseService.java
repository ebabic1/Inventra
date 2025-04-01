package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.model.Warehouse;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Transactional
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @Transactional
    public Optional<Warehouse> findById(Long id) {
        return warehouseRepository.findById(id);
    }

    @Transactional
    public Warehouse create(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public Warehouse update(Long id, Warehouse warehouseDTO) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Warehouse not found with id: " + id));

        existing.setName(warehouseDTO.getName());
        existing.setAddress(warehouseDTO.getAddress());
        existing.setZipCode(warehouseDTO.getZipCode());

        return warehouseRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Warehouse not found with id: " + id);
        }
        warehouseRepository.deleteById(id);
    }
}