package com.wicc.personalassistant.service.transaction;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.dto.transactioncategory.CategoryDto;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import com.wicc.personalassistant.enums.TransactionStatus;
import com.wicc.personalassistant.repo.transaction.TransactionRepo;
import com.wicc.personalassistant.service.transactioncategory.CategoryService;
import com.wicc.personalassistant.utils.CurrentUserDetailService;
import com.wicc.personalassistant.utils.CurrentDateTimeUtil;
import com.wicc.personalassistant.utils.IncomeExpensesCalculatorUtil;
import com.wicc.personalassistant.utils.validators.UserValidator;
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
    private final CurrentUserDetailService currentUserDetailService;
    private final UserValidator userValidator;
    private final CategoryService categoryService;

    public TransactionServiceImpl(TransactionRepo transactionRepo,
                                  IncomeExpensesCalculatorUtil incomeExpensesCalculatorUtil,
                                  CurrentDateTimeUtil currentDateTimeUtil,
                                  CurrentUserDetailService currentUserDetailService,
                                  UserValidator userValidator,
                                  CategoryService categoryService) {
        this.transactionRepo = transactionRepo;
        this.incomeExpensesCalculatorUtil = incomeExpensesCalculatorUtil;
        this.currentDateTimeUtil = currentDateTimeUtil;
        this.currentUserDetailService = currentUserDetailService;
        this.userValidator = userValidator;
        this.categoryService = categoryService;
    }

    //add new transaction
    @Override
    public ResponseDto save(TransactionDto transactionDto) {
        try {
            transactionDto.setUser(currentUserDetailService.getCurrentUser());

            //validation to validate amount if it is greater than 0
            if (transactionDto.getAmount() <= 0) {
                throw new ArithmeticException("Amount can't be 0 or less than 0");
            }
            Transaction transaction = new Transaction(transactionDto);
            transaction = transactionRepo.save(transaction);

            //adding transaction to the related category
            Category category = new Category(categoryService.findById(transaction.getCategory().getId()));
            category.getTransactions().add(transaction);

            //adding transaction to user for better communication
            currentUserDetailService.getCurrentUser().getTransactions().add(transaction);

            log.info("Transaction Added");
            //returning value as a response dto
            return new ResponseDto("Transaction added!!");
        } catch (ArithmeticException e) {
            log.error("Amount error");
            return new ResponseDto(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto("Transaction addition failed!!");
        }
    }

    //finding all the transactions
    @Override
    public List<TransactionDto> findAll() {

        return TransactionDto.toDtos(currentUserDetailService.getCurrentUser().getTransactions());
    }

    //finding transaction by id
    @Override
    public TransactionDto findById(Integer integer) {
        Transaction transaction;

        //after this validation user can't access another user detail
        boolean validTransactionId = userValidator.isValidTransactionId(integer);
        if (validTransactionId) {
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
        return null;
    }

    //deleting transaction by id
    @Override
    public void deleteById(Integer integer) {
        boolean validTransactionId = userValidator.isValidTransactionId(integer);
        if (validTransactionId) {
            transactionRepo.deleteById(integer);
        }
    }

    //fetching current month income by category
    @Override
    public Map<String, Integer> currentMonthIncomes() {
        //transactions that have TransactionStatus.INCOME enum are income
        // and if .isGreater is true this transaction is done in this month
        List<TransactionDto> incomes = findAll().stream()
                .filter(transaction -> transaction.getCategory()
                        .getTransactionStatus()
                        .equals(TransactionStatus.INCOME) && currentDateTimeUtil.isGreater(transaction.getTransactionDate()))
                .collect(Collectors.toList());
        return incomeExpensesCalculatorUtil.calculateIncomeExpenses(incomes);
    }


    //fetching current month expenses by category
    @Override
    public Map<String, Integer> currentMonthExpenses() {
        //transactions that have TransactionStatus.EXPENSES enum are expenses
        // and if .isGreater is true this transaction is done in this month
        List<TransactionDto> expenses = findAll().stream()
                .filter(transaction -> transaction.getCategory()
                        .getTransactionStatus()
                        .equals(TransactionStatus.EXPENSES) && currentDateTimeUtil.isGreater(transaction.getTransactionDate()))
                .collect(Collectors.toList());
        return incomeExpensesCalculatorUtil.calculateIncomeExpenses(expenses);
    }

    @Override
    public Integer getTotalIncome() {
        //returns the sum of all incomes
        return currentMonthIncomes().values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public Integer getTotalExpenses() {
        //returns the sum of all expenses
        return currentMonthExpenses().values().stream().mapToInt(Integer::intValue).sum();
    }

    //profitLoss calculation
    @Override
    public Integer getProfitLoss() {
        return getTotalIncome() - getTotalExpenses();
    }
}
