package ru.castroy10.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.castroy10.doctor.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
