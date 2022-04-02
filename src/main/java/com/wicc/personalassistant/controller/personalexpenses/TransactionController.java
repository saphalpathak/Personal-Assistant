package com.wicc.personalassistant.controller.personalexpenses;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import com.wicc.personalassistant.service.transaction.TransactionService;
import com.wicc.personalassistant.service.transactioncategory.CategoryService;
import com.wicc.personalassistant.service.user.CurrentUserDetailService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final CurrentUserDetailService currentUserDetailService;

    public TransactionController(TransactionService transactionService, CurrentUserDetailService currentUserDetailService) {
        this.transactionService = transactionService;
        this.currentUserDetailService = currentUserDetailService;
    }

    @GetMapping("/home")
    public String home(Model model){
        Map<String, Integer> expenses = transactionService.currentMonthExpenses(currentUserId());
        Map<String, Integer> incomes = transactionService.currentMonthIncomes(currentUserId());

        model.addAttribute("incomeKeys",incomes.keySet());
        model.addAttribute("incomeValues",incomes.values());

        model.addAttribute("expensesKeys",expenses.keySet());
        model.addAttribute("expensesValues",expenses.values());
        return "personalexpenses/personal_expenses_landing";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("transactionDto",new TransactionDto());
        model.addAttribute("categoryData",transactionService.getAllCategory(currentUserId()));
        return "personalexpenses/add_transaction";
    }

    //saving the data
    @PostMapping("/create")
    public String saveTransaction(@Valid @ModelAttribute TransactionDto transactionDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("transactionDto",transactionDto);
            model.addAttribute("categoryData",transactionService.getAllCategory(currentUserId()));
            model.addAttribute("message","Transaction addition failed!!");
            return "personalexpenses/add_transaction";
        }
        transactionDto.setTransactionDate(new Date());
        //getting the current user
        transactionDto.setUser(currentUserDetailService.getCurrentUser());
        ResponseDto save = transactionService.save(transactionDto);
        redirectAttributes.addFlashAttribute("message",save.getMessage());
        return "redirect:/transaction/add";
    }

    //all the transaction
    @GetMapping("all")
    public String getAllTransaction(Model model){
        List<TransactionDto> all = transactionService.findAll(currentUserId());
        model.addAttribute("allTransaction",all);
        model.addAttribute("heading","Transaction Log");
        return "personalexpenses/transaction_log";
    }

    //additional details
    @GetMapping("/additional-details")
    public String getAdditionalDetails(Model model){
        return "personalexpenses/additional_details";
    }

    @GetMapping("/all/export")
    public void exportToExcel(HttpServletResponse response,Model model){
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = Transaction Details.xlsx";
        response.setHeader(headerKey,headerValue);

        List<TransactionDto> transactionDtos = transactionService.findAll(currentUserId());

        ExcelServiceUtil excelServiceUtil = new ExcelServiceUtil(transactionDtos);
        try{
            excelServiceUtil.export(response);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        List<TransactionDto> all = transactionService.findAll(currentUserId());
        model.addAttribute("allTransaction",all);
        model.addAttribute("heading","Transaction Log");
    }

    //Getting the current user id
    public Integer currentUserId() {
        return currentUserDetailService.getCurrentUserId();
    }
}
