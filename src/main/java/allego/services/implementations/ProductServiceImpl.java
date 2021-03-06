package allego.services.implementations;

import allego.models.Product;
import allego.repositories.ProductRepository;
import allego.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ibm on 2017-10-17.
 */
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id){
        return productRepository.findById(id);
    }
}
