package kg.jenkins.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping(value = "/home.html")
    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }
}
