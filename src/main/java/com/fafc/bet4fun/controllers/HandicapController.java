package com.fafc.bet4fun.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Bet;
import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.entities.Handicap;
import com.fafc.bet4fun.services.AuthenticationService;
import com.fafc.bet4fun.services.BetService;
import com.fafc.bet4fun.services.HandicapService;

@Controller
public class HandicapController {

    @Autowired
    HandicapService handicapService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    BetService betService;

    @RequestMapping(value="/handicaps/upcoming", method = RequestMethod.GET)
    public String getAllUpcomingHandicaps(Model model, HttpServletRequest request) {
        List<Handicap> upcomingHandicaps = this.handicapService.getAllUpcomingHandicaps();
        model.addAttribute("upcomingHandicaps", upcomingHandicaps);
        model.addAttribute(Constants.MESSAGE, request.getParameter(Constants.MESSAGE));
        return "handicaps-upcoming";
    }

    @RequestMapping(value="/handicap/{handicapId}/bet/create", method = RequestMethod.GET)
    public String addBet(@PathVariable String handicapId, Model model) {
        Handicap handicap = this.handicapService.findById(handicapId);
        model.addAttribute("handicap", handicap);
        model.addAttribute("bet", new Bet(handicap));

        return "bet-add";
    }

    @RequestMapping(value="/handicap/{handicapId}/bet/create", method = RequestMethod.POST)
    public String addBet(@PathVariable String handicapId, @Valid @ModelAttribute("bet") Bet bet, BindingResult errors, RedirectAttributes redir) {

        Handicap handicap = this.handicapService.findById(handicapId);
        bet.setHandicap(handicap);

        Client client = this.authenticationService.getLoggedInUser();
        bet.setClient(client);

        this.betService.saveBet(bet);
  
        redir.addAttribute(Constants.MESSAGE, "You bet successfully.");
        return "redirect:/handicaps/upcoming";
    }
}
