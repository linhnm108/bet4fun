package com.fafc.bet4fun.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Bet;
import com.fafc.bet4fun.services.BetService;

@Controller
public class BetController {

    @Autowired
    BetService betService;

    @RequestMapping(value="/my-bets", method = RequestMethod.GET)
    public String getAllMatches(Model model, HttpServletRequest request) {
        List<Bet> bets = this.betService.getAllBetsOfCurrentUser();
        model.addAttribute("bets", bets);
        model.addAttribute(Constants.MESSAGE, request.getParameter(Constants.MESSAGE));
        return "my-bets";
    }
}
