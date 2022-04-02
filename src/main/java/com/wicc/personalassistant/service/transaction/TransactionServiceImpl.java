package com.wicc.personalassistant.service.transaction;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.enums.TransactionStatus;
import com.wicc.personalassistant.repo.transaction.TransactionRepo;
import com.wicc.personalassistant.utils.CurrentDateTimeUtil;
import com.wicc.personalassistant.utils.IncomeExpensesCalculatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;
    private final IncomeExpensesCalculatorUtil incomeExpensesCalculatorUtil;
    private final CurrentDateTimeUtil currentDateTimeUtil;

    public TransactionServiceImpl(TransactionRepo transactionRepo,
                                  IncomeExpensesCalculatorUtil incomeExpensesCalculatorUtil,
                                  CurrentDateTimeUtil currentDateTimeUtil) {
        this.transactionRepo = transactionRepo;
        this.incomeExpensesCalculatorUtil = incomeExpensesCalculatorUtil;
        this.currentDateTimeUtil = currentDateTimeUtil;
    }

    //add new transaction
    @Override
    public ResponseDto save(TransactionDto transactionDto) {
        try {
            Transaction transaction = new Transaction(transactionDto);
            transactionRepo.save(transaction);
            //returning value as a response dto
            log.info("Transaction Added");
            return new ResponseDto("Transaction added!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto("Transaction addition failed!!");
        }
    }

    //finding all the transactions
    @Override
    public List<TransactionDto> findAll(Integer userId) {
        return TransactionDto.toDtos(transactionRepo.findAllByUserId(userId));
    }

    //finding transaction by id
    @Override
    public TransactionDto findById(Integer integer) {
        Transaction transaction;
        try {
            transaction = transactionRepo.findById(integer).orElseThrow(
                    () -> new RuntimeException("Not Found")
            );
            return new TransactionDto(transaction);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //deleting transaction by id
    @Override
    public void deleteById(Integer integer) {
        transactionRepo.deleteById(integer);
    }

    //fetching current month income by category
    @Override
    public Map<String, Integer> currentMonthIncomes(Integer userId) {
        //transactions that have TransactionStatus.INCOME enum are income
        // and if .isGreater is true this transaction is done in this month
        List<TransactionDto> incomes = findAll(userId).stream()
                .filter(transaction -> transaction.getCategory()
                        .getTransactionStatus()
                        .equals(TransactionStatus.INCOME) && currentDateTimeUtil.isGreater(transaction.getTransactionDate()))
                .collect(Collectors.toList());
        return incomeExpensesCalculatorUtil.calculate(incomes);
    }


    //fetching current month expenses by category
    @Override
    public Map<String, Integer> currentMonthExpenses(Integer userId) {
        //transactions that have TransactionStatus.EXPENSES enum are expenses
        // and if .isGreater is true this transaction is done in this month
        List<TransactionDto> expenses = findAll(userId).stream()
                .filter(transaction -> transaction.getCategory()
                        .getTransactionStatus()
                        .equals(TransactionStatus.EXPENSES) && currentDateTimeUtil.isGreater(transaction.getTransactionDate()))
                .collect(Collectors.toList());
        return incomeExpensesCalculatorUtil.calculate(expenses);
    }

    //collecting all the list of category of current user
    @Override
    public List<String> getAllCategory(Integer id) {
        return findAll(id).stream()
                .map(transaction -> transaction.getCategory().getName())
                .collect(Collectors.toList());
    }
}
