package pl.konrad.feak_news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.konrad.feak_news.model.Statistics;
import pl.konrad.feak_news.model.UserSession;
import pl.konrad.feak_news.model.forms.PostForm;
import pl.konrad.feak_news.model.forms.UserForm;
import pl.konrad.feak_news.model.services.AdminSerivce;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class AdminController {
    final
    private AdminSerivce adminSerivce;
    private UserSession userSession;
    private Statistics stats;

    @Autowired
    public AdminController(AdminSerivce adminSerivce, UserSession userSession, Statistics stats) {
        this.adminSerivce = adminSerivce;
        this.userSession = userSession;
        this.stats = stats;
    }


    @GetMapping("/admin/post/add")
    public String showTempNewPost(Model model){
        model.addAttribute("newFeak", new PostForm())
                .addAttribute("loggedUser",userSession);
        return "/admin/addNewPost";

    }

    @PostMapping("/admin/post/add")
    public String addNewPost(@ModelAttribute PostForm postForm, RedirectAttributes redirectAttributes){
        try {
            redirectAttributes.addFlashAttribute("info", adminSerivce.addNews(postForm,userSession.getNick()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/post/add";
    }

    @GetMapping("/post/delete/{id}")
    public String removePost(@PathVariable("id") int id, HttpServletRequest request){
        try {
            adminSerivce.removePostById(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model){
        model.addAttribute("newFeak", new PostForm())
                .addAttribute("loggedUser",userSession)
                .addAttribute("postsPerMod",adminSerivce.showCount())
                .addAttribute("allPosts", adminSerivce.findAllPostsPerMod())
                .addAttribute("maxPostsByMod", adminSerivce.countMaxPostBySingleUser());


        return "/admin/dashboard";

    }

    @GetMapping("/admin/charts")
    public String showCharts(Model model){
        model.addAttribute("loggedUser",userSession)
            .addAttribute("activityChart", stats.getAmmountOfViews())
            .addAttribute("postsPerMod",adminSerivce.showCount());

        return "/admin/charts";
    }

    @GetMapping("/admin/counter/reset")
    public String resetCounter(HttpServletRequest request){
        stats.resetCounterOfViews();
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/admin/users/list")
    public String getUsersList(Model model){
        model.addAttribute("loggedUser",userSession)
                .addAttribute("allUsers", adminSerivce.getAllUsers())
                .addAttribute("user", new UserForm());
        return "/admin/usersList";
    }

    @GetMapping("/admin/users/remove/{id}")
    public String removeUserById(@PathVariable("id") int id, HttpServletRequest request){
        adminSerivce.removeUserById(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable("id") int id, @ModelAttribute UserForm userForm){
        adminSerivce.editUser(userForm,id);
        return "redirect:/admin/users/list";
    }

}
