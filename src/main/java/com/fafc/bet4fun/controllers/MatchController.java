package com.fafc.bet4fun.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Match;
import com.fafc.bet4fun.services.MatchService;

@Controller
public class MatchController {

    @Autowired
    MatchService matchService;

    @RequestMapping(value="/matches", method = RequestMethod.GET)
    public String getAllMatches(Model model, HttpServletRequest request) {
        List<Match> matches = this.matchService.getAllMatches();
        model.addAttribute("matches", matches);
        model.addAttribute(Constants.MESSAGE, request.getParameter(Constants.MESSAGE));
        return "matches";
    }

    @RequestMapping(value="/match/add", method = RequestMethod.GET)
    public String addMatch(@Valid @ModelAttribute("match") Match match) {
        return "match-add";
    }

    @RequestMapping(value="/match/add", method = RequestMethod.POST)
    public String addMatch(@Valid @ModelAttribute("match") Match match, BindingResult errors, RedirectAttributes redir) {
        match.convertLocalScheduleDateToUTC();
        match.setStatus(Constants.MATCH_NOT_STARTED_STATUS);
        match = this.matchService.saveMatch(match);
        redir.addAttribute(Constants.MESSAGE, "Match " + match.getMatchId() + " added successfully.");
        return "redirect:/matches";
    }

    @RequestMapping(value = "/match/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(name="uuid") String uuid, RedirectAttributes redir) {
        this.matchService.deleteMatch(uuid);
        redir.addAttribute(Constants.MESSAGE, "Match " + uuid + " has been deleted successfully.");
        return "redirect:/matches";
    }
}
