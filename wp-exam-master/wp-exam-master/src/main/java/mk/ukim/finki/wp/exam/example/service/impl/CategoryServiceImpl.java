package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Category;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidCategoryIdException;
import mk.ukim.finki.wp.exam.example.repository.CategoryRepository;
import mk.ukim.finki.wp.exam.example.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    public final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(InvalidCategoryIdException::new);
    }

    @Override
    public List<Category> listAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category create(String name) {
        return this.categoryRepository.save(new Category(name));
    }
}
