package com.fafc.bet4fun.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fafc.bet4fun.entities.Match;
import com.fafc.bet4fun.services.MatchService;

@Controller
public class HomeController {
    @Autowired
    MatchService matchService;

    @RequestMapping("/")
    public String login(Model model) {
        List<Match> upcomingMatches = matchService.getAllUpcomingMatches();
        model.addAttribute("upcomingMatches", upcomingMatches);
        return "home";
    }
}
