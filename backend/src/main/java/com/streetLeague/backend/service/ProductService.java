package com.streetLeague.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.streetLeague.backend.dto.ProductDTO;
import com.streetLeague.backend.dto.ProductSearchRequest;
import com.streetLeague.backend.entity.Product;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.mapper.ProductMapper;
import com.streetLeague.backend.repository.ProductRepository;
import com.streetLeague.backend.repository.UserRepository;
import com.streetLeague.backend.specification.ProductSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;

    public ProductDTO create(ProductDTO dto) {

        if (dto.getUser() == null || dto.getUser().getIdUser() == null) {
            throw new RuntimeException("User is required");
        }

        Integer userId = dto.getUser().getIdUser();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productMapper.toEntity(dto);
        product.setUser(user);

        Product saved = productRepository.save(product);

        return productMapper.toDTO(saved);
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public Page<ProductDTO> search(ProductSearchRequest req) {
        var spec = ProductSpecification.build(
                req.getQuery(), req.getCategory(), req.getSubcategory(),
                req.getMinPrice(), req.getMaxPrice(),
                req.getPointure(), req.getTaille());
        Sort sort = Sort.by(Sort.Direction.fromString(req.getSortDir()), req.getSortBy());
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);
        return productRepository.findAll(spec, pageable).map(productMapper::toDTO);
    }

    public ProductDTO getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductDTO update(Long id, ProductDTO dto) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setCategory(dto.getCategory());
        existing.setSubcategory(dto.getSubcategory());
        existing.setPointure(dto.getPointure());
        existing.setTaille(dto.getTaille());
        if (dto.getImageUrl() != null) {
            existing.setImageUrl(dto.getImageUrl());
        }

        return productMapper.toDTO(productRepository.save(existing));
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        imageService.deleteImage(product.getImageUrl());
        productRepository.deleteById(id);
    }

    public ProductDTO uploadImage(Long id, MultipartFile file) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String imageUrl = imageService.storeImage(file, id);
        product.setImageUrl(imageUrl);
        return productMapper.toDTO(productRepository.save(product));
    }
}
