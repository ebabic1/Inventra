package ba.unsa.etf.nwt.inventra.inventory_service.service;

import ba.unsa.etf.nwt.inventra.inventory_service.dto.SupplierDTO;
import ba.unsa.etf.nwt.inventra.inventory_service.mapper.SupplierMapper;
import ba.unsa.etf.nwt.inventra.inventory_service.model.Supplier;
import ba.unsa.etf.nwt.inventra.inventory_service.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Transactional
    public List<SupplierDTO> findAll() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<SupplierDTO> findById(Long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        return supplier.map(supplierMapper::toDTO);
    }

    @Transactional
    public SupplierDTO create(SupplierDTO supplierDTO) {
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDTO(savedSupplier);
    }

    @Transactional
    public SupplierDTO update(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found with id: " + id));

        if (supplierDTO.getId() != null && !id.equals(supplierDTO.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ID in path doesn't match request body");
        }

        existingSupplier.setName(supplierDTO.getName());
        Supplier savedSupplier = supplierRepository.save(existingSupplier);
        return supplierMapper.toDTO(savedSupplier);
    }

    @Transactional
    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }
}
