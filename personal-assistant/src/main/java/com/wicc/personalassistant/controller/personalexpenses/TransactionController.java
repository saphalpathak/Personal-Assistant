package com.wicc.personalassistant.controller.personalexpenses;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.service.transaction.TransactionService;
import com.wicc.personalassistant.service.transactioncategory.CategoryService;
import com.wicc.personalassistant.utils.ExcelServiceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        Map<String, Integer> expenses = transactionService.currentMonthExpenses();
        Map<String, Integer> incomes = transactionService.currentMonthIncomes();

        model.addAttribute("incomeKeys", incomes.keySet());
        model.addAttribute("incomeValues", incomes.values());

        model.addAttribute("expensesKeys", expenses.keySet());
        model.addAttribute("expensesValues", expenses.values());

        model.addAttribute("profitLoss",transactionService.getProfitLoss());
        return "personalexpenses/personal_expenses_landing";
    }

    @GetMapping("/add-income")
    public String addIncome(Model model) {
        model.addAttribute("transactionDto", new TransactionDto());
        model.addAttribute("categoryData", categoryService.getAllIncomeCategories());
        return "personalexpenses/add_transaction";
    }
    @GetMapping("/add-expenses")
    public String addExpenses(Model model) {
        model.addAttribute("transactionDto", new TransactionDto());
        model.addAttribute("categoryData", categoryService.getAllExpensesCategories());
        return "personalexpenses/add_transaction";
    }

    //saving the data
    @PostMapping("/create")
    public String saveTransaction(@Valid @ModelAttribute TransactionDto transactionDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("transactionDto", transactionDto);
            model.addAttribute("categoryData", categoryService.findAll());
            model.addAttribute("message", "Transaction addition failed!!");
            return "personalexpenses/add_transaction";
        }
        transactionDto.setTransactionDate(new Date());
        ResponseDto save = transactionService.save(transactionDto);
        redirectAttributes.addFlashAttribute("message", save.getMessage());
        return "redirect:/transaction/home";
    }

    //all the transaction
    @GetMapping("all")
    public String getAllTransaction(Model model) {
        List<TransactionDto> all = transactionService.findAll();
        model.addAttribute("allTransaction", all);
        model.addAttribute("heading", "Transaction Log");
        return "personalexpenses/transaction_log";
    }

    //additional details
    @GetMapping("/additional-details")
    public String getAdditionalDetails(Model model) {
        Integer income = transactionService.getTotalIncome();
        Integer expenses = transactionService.getTotalExpenses();
        Integer profitLoss = transactionService.getProfitLoss();
        Map<String, Integer> currentMonthIncomes = transactionService.currentMonthIncomes();
        Map<String, Integer> currentMonthExpenses = transactionService.currentMonthExpenses();
        model.addAttribute("totalIncome", income);
        model.addAttribute("totalExpenses", expenses);
        model.addAttribute("profitLoss",profitLoss);
        model.addAttribute("allIncomes", currentMonthIncomes);
        model.addAttribute("allExpenses", currentMonthExpenses);
        return "personalexpenses/additional_details";
    }

    @GetMapping("/all/export")
    public void exportToExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = Transaction Details.xlsx";
        response.setHeader(headerKey, headerValue);

        List<TransactionDto> transactionDtos = transactionService.findAll();

        ExcelServiceUtil excelServiceUtil = new ExcelServiceUtil(transactionDtos);
        try {
            excelServiceUtil.export(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
