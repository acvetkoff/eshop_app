package bg.softuni.eshop.admin.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.admin.model.AdminUserViewModel;
import bg.softuni.eshop.admin.service.AdminUserService;
import bg.softuni.eshop.order.model.view.OrderViewModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(AdminUserController.CONTROLLER_PREFIX)
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminUserController extends BaseController {

    // ---- Static

    public static final String PAGE_PREFIX = "admin/user/";
    public static final String CONTROLLER_PREFIX = "admin/users";

    // ---- Fields

    private AdminUserService userService;

    @Autowired
    public AdminUserController(ModelParser modelParser, AdminUserService userService) {
        super(modelParser);
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        AdminUserViewModel userModel = this.map(userService.findById(id), AdminUserViewModel.class);
        model.addAttribute("user", userModel);
        return this.view(PAGE_PREFIX + "user-detail");
    }

    @GetMapping
    public String findAll(Model model) {
        List<AdminUserViewModel> users = this.map(userService.findAllUsers(), AdminUserViewModel.class);
        model.addAttribute("users", users);
        return this.view(PAGE_PREFIX + "users");
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, Model model) {
        userService.deleteById(id);
        return redirect(CONTROLLER_PREFIX);
    }

}
