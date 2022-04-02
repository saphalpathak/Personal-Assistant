package com.wicc.personalassistant.repo.todo;

import com.wicc.personalassistant.entity.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface TodoRepo extends JpaRepository<Todo,Integer> {

    //finding all today's task
    @Query(value = "select t from Todo t where t.dueDate = current_date and t.user.id=:user")
    List<Todo> findAllByDate(@Param("user") Integer id);

}
