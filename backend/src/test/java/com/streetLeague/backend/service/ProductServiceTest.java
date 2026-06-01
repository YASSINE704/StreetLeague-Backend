package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.ProductDTO;
import com.streetLeague.backend.dto.ProductSearchRequest;
import com.streetLeague.backend.entity.Product;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.mapper.ProductMapper;
import com.streetLeague.backend.repository.ProductRepository;
import com.streetLeague.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_shouldSucceed() {
        User user = User.builder().idUser(1).nom("Seller").build();
        ProductDTO dto = ProductDTO.builder().name("Ball").price(29.99).user(user).build();
        Product entity = Product.builder().name("Ball").price(29.99).build();
        Product saved = Product.builder().id(1L).name("Ball").price(29.99).user(user).build();
        ProductDTO resultDto = ProductDTO.builder().id(1L).name("Ball").price(29.99).user(user).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productMapper.toEntity(dto)).thenReturn(entity);
        when(productRepository.save(any(Product.class))).thenReturn(saved);
        when(productMapper.toDTO(saved)).thenReturn(resultDto);

        ProductDTO result = productService.create(dto);

        assertThat(result.getName()).isEqualTo("Ball");
        assertThat(result.getPrice()).isEqualTo(29.99);
    }

    @Test
    void create_whenUserNull_shouldThrow() {
        ProductDTO dto = ProductDTO.builder().name("Ball").build();

        assertThatThrownBy(() -> productService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User is required");
    }

    @Test
    void create_whenUserNotFound_shouldThrow() {
        ProductDTO dto = ProductDTO.builder().user(User.builder().idUser(99).build()).build();

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getAll_shouldReturnList() {
        when(productRepository.findAll()).thenReturn(List.of(
                Product.builder().id(1L).build(), Product.builder().id(2L).build()
        ));
        when(productMapper.toDTO(any())).thenReturn(
                ProductDTO.builder().id(1L).build(),
                ProductDTO.builder().id(2L).build()
        );

        List<ProductDTO> result = productService.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void getById_whenFound_shouldReturn() {
        Product product = Product.builder().id(1L).name("Ball").build();
        ProductDTO dto = ProductDTO.builder().id(1L).name("Ball").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(dto);

        ProductDTO result = productService.getById(1L);

        assertThat(result.getName()).isEqualTo("Ball");
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void update_shouldSucceed() {
        Product existing = Product.builder().id(1L).name("Old").price(10.0).build();
        ProductDTO dto = ProductDTO.builder().name("New").price(20.0).category("Shoes").build();
        Product saved = Product.builder().id(1L).name("New").price(20.0).category("Shoes").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(saved);
        when(productMapper.toDTO(saved)).thenReturn(ProductDTO.builder().id(1L).name("New").price(20.0).category("Shoes").build());

        ProductDTO result = productService.update(1L, dto);

        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getCategory()).isEqualTo("Shoes");
    }

    @Test
    void delete_shouldSucceed() {
        Product product = Product.builder().id(1L).name("Ball").imageUrl("uploads/products/1/img.jpg").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(imageService).deleteImage("uploads/products/1/img.jpg");
        verify(productRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotFound_shouldThrow() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void search_shouldApplySpecAndPagination() {
        ProductSearchRequest req = ProductSearchRequest.builder()
                .query("ball").category("Sports").minPrice(10.0).maxPrice(100.0)
                .sortBy("price").sortDir("asc").page(0).size(10).build();

        Page<Product> page = new PageImpl<>(List.of(Product.builder().id(1L).name("Ball").build()));

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(productMapper.toDTO(any())).thenReturn(ProductDTO.builder().id(1L).name("Ball").build());

        Page<ProductDTO> result = productService.search(req);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Ball");
    }

    @Test
    void uploadImage_shouldSucceed() {
        MultipartFile file = mock(MultipartFile.class);
        Product product = Product.builder().id(1L).name("Ball").build();
        Product saved = Product.builder().id(1L).name("Ball").imageUrl("uploads/products/1/new.jpg").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(file.isEmpty()).thenReturn(false);
        when(imageService.storeImage(file, 1L)).thenReturn("uploads/products/1/new.jpg");
        when(productRepository.save(any(Product.class))).thenReturn(saved);
        when(productMapper.toDTO(saved)).thenReturn(ProductDTO.builder().id(1L).name("Ball").imageUrl("uploads/products/1/new.jpg").build());

        ProductDTO result = productService.uploadImage(1L, file);

        assertThat(result.getImageUrl()).isEqualTo("uploads/products/1/new.jpg");
    }

    @Test
    void uploadImage_whenFileEmpty_shouldThrow() {
        MultipartFile file = mock(MultipartFile.class);
        Product product = Product.builder().id(1L).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(file.isEmpty()).thenReturn(true);

        assertThatThrownBy(() -> productService.uploadImage(1L, file))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("File is empty");
    }

    @Test
    void uploadImage_whenProductNotFound_shouldThrow() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.uploadImage(99L, mock(MultipartFile.class)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}
