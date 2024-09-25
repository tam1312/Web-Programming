package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidProductIdException;
import mk.ukim.finki.wp.exam.example.repository.ProductRepository;
import mk.ukim.finki.wp.exam.example.service.CategoryService;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> listAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
    }

    @Override
    public Product create(String name, Double price, Integer quantity, List<Long> categories) {
        Product product = new Product(
                name,
                price,
                quantity,
                categories.stream().map(cat -> this.categoryService.findById(cat)).collect(Collectors.toList())
        );
        return this.productRepository.save(product);
    }

    @Override
    public Product update(Long id, String name, Double price, Integer quantity, List<Long> categories) {
        Product product = this.findById(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategories(categories.stream().map(this.categoryService::findById).collect(Collectors.toList()));

        return this.productRepository.save(product);
    }

    @Override
    public Product delete(Long id) {
        Product product = this.findById(id);
        this.productRepository.delete(product);
        return product;
    }

    @Override
    public List<Product> listProductsByNameAndCategory(String name, Long categoryId) {
        if(name != null && categoryId != null){
            return this.productRepository.findAllByNameLikeAndCategoriesContaining(
                    "%" + name + "%",
                    this.categoryService.findById(categoryId)
            );
        }else if(name != null){
            return this.productRepository.findAllByNameLike("%" + name + "%");
        }else if(categoryId != null){
            return this.productRepository.findAllByCategoriesContaining(
                    this.categoryService.findById(categoryId)
            );
        }else{
            return this.productRepository.findAll();
        }
    }
}
