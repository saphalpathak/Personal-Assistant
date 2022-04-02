package com.wicc.personalassistant.repo.transaction;
import com.wicc.personalassistant.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
}
