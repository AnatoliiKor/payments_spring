package com.kor.payments.controller;

import com.kor.payments.constants.Constant;
import com.kor.payments.utils.ControllerUtils;
import com.kor.payments.domain.Account;
import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.services.AccountRequestService;
import com.kor.payments.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {
    private static final Logger log = LogManager.getLogger(AccountController.class);
    int page = 0;
    int maxPage = 1000;
    String sort = Constant.ID;
    String order = Constant.ASC;
    List<Account> accounts;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRequestService accountRequestService;

    @GetMapping("/accounts/{user}")
    public String getUserAccounts(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable User user,
            Model model) {
        if (!user.getEmail().equals(userDetails.getUsername()) && !userDetails.getAuthorities().contains(Role.ADMIN)) {
            return "accessDenied";
        }
        model.addAttribute(Constant.ACCOUNTS, accountService.findAccountsByUser(user));
        String userDate = user.getLastName() + " " + user.getName() + " " + user.getMiddleName() + " (" + user.getEmail() + ")";
        model.addAttribute("user_date", userDate);
        log.info("Accounts are requested for user {}", user.getId());
        return "users_accounts";
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAccounts(Model model) {
        if (page > maxPage) {
            --page;
        }
//        accounts = accountService.findAllPage(page, sort, order);
        accounts = accountService.findAllAccountsPagesSorted(accountService.findAllAccounts(), page, sort, order);
        fillModel(model);
        if (accounts.size() < 10) {
            maxPage = page;
        }
        log.info("Accounts are requested by Admin, page {}, sort {}, order {}", page, sort, order);
        return "accounts_list";
    }

    private void fillModel(Model model) {
        model.addAttribute(Constant.ACCOUNTS, accounts);
        model.addAttribute(Constant.SORT, sort);
        model.addAttribute(Constant.ORDER, order);
        model.addAttribute(Constant.PAGE, page);
    }

    @GetMapping("/accounts/page")
    public String pageAccounts(@RequestParam int p) {
        if (p >= 0) {
            page = p;
        }
        return "redirect:/accounts";
    }

    @GetMapping("/accounts/sort")
    public String pageSort(@RequestParam String sort, @RequestParam String order) {
        this.page = 0;
        this.sort = sort;
        this.order = order;
        return "redirect:/accounts";
    }

    @GetMapping("/new_account")
    public String getNewAccountPage() {
        return "new_account";
    }

    @PostMapping("/new_account")
    public String newAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid Account account,
            BindingResult bindingResult,
            Model model) {
        User user = (User) userDetails;
        account.setUser(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return "new_account";
        } else {
            if (accountService.newAccount(account)) {
                log.info("Account {} is added to DB", account.getAccountName());
                return "redirect:/";
            } else {
                log.info("Account {} is not added to DB", account.getAccountName());
                model.addAttribute(Constant.WARN, "account_not_opened");
                return "wallet";
            }
        }
    }

    @GetMapping("/account/{account}")
    public String getAccount(@PathVariable Account account, Model model) {
        model.addAttribute(Constant.ACCOUNT, account);
        return "account";
    }

    @PostMapping("/account/refill/{account}")
    public String refill(@PathVariable Account account, @RequestParam double amount) {
        int amountInt = (int) (amount * 100);
        if (account.isActive() && amount > 0 && accountService.refill(account, amountInt)) {
            return "redirect:/account/" + account.getId() + "?message=balance_refilled";
        }
        return "redirect:/account/" + account.getId() + "?warn=balance_not_refilled";
    }

    @PostMapping("/account/request/{account}")
    public String sendRequest(@PathVariable Account account, String request) {
        if (accountRequestService.newAccountRequest(account, request)) {
            return "redirect:/account/" + account.getId() + "?message=updated";
        }
        log.debug("New request {} for account {} is not created", request, account.getId());
        return "redirect:/account/" + account.getId() + "?warn=not_updated";
    }
}
