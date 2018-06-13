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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Bet;
import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.entities.Handicap;
import com.fafc.bet4fun.entities.Match;
import com.fafc.bet4fun.services.AuthenticationService;
import com.fafc.bet4fun.services.BetService;
import com.fafc.bet4fun.services.HandicapService;
import com.fafc.bet4fun.services.MatchService;
import com.fafc.bet4fun.services.UserService;

@Controller
public class MatchController {

    @Autowired
    MatchService matchService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    HandicapService handicapService;

    @Autowired
    BetService betService;

    @Autowired
    UserService userService;

    @RequestMapping(value="/matches", method = RequestMethod.GET)
    public String getAllMatches(Model model, HttpServletRequest request) {
        List<Match> matches = this.matchService.getAllMatches();
        model.addAttribute("matches", matches);
        model.addAttribute(Constants.MESSAGE, request.getParameter(Constants.MESSAGE));
        return "matches";
    }

    @RequestMapping(value="/matches/upcoming", method = RequestMethod.GET)
    public String getAllUpcomingMatches(Model model, HttpServletRequest request) {
        List<Match> upcomingMatches = this.matchService.getAllUpcomingMatches();
        model.addAttribute("upcomingMatches", upcomingMatches);
        model.addAttribute(Constants.MESSAGE, request.getParameter(Constants.MESSAGE));
        return "matches-upcoming";
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

    @RequestMapping(value="/match/{matchId}/handicap/create", method = RequestMethod.GET)
    public String addHandicap(@PathVariable String matchId, Model model) {
        Match match = this.matchService.findById(matchId);
        model.addAttribute("match", match);
        model.addAttribute("handicap", new Handicap(match.getScheduleDate()));

        return "handicap-add";
    }

    @RequestMapping(value="/match/{matchId}/handicap/create", method = RequestMethod.POST)
    public String addHandicap(@PathVariable String matchId, @Valid @ModelAttribute("handicap") Handicap handicap, BindingResult errors, RedirectAttributes redir) {

        handicap.convertLocalExpiredDateToUTC();
        handicap.setClient(this.authenticationService.getLoggedInUser());
        handicap = this.handicapService.saveHandicap(handicap);

        Match match = this.matchService.findById(matchId);
        match.getHandicaps().add(handicap);
        this.matchService.saveMatch(match);
        redir.addAttribute(Constants.MESSAGE, "You has added a handicap for match " + match.getMatchId() + " successfully.");
        return "redirect:/matches/upcoming";
    }

    @RequestMapping(value="/match/{matchId}/result/update", method = RequestMethod.GET)
    public String updateResult(@PathVariable String matchId, Model model) {
        Match match = this.matchService.findById(matchId);
        model.addAttribute("match", match);

        return "match-update-result";
    }

    @RequestMapping(value="/match/{matchId}/result/update", method = RequestMethod.POST)
    public String updateResult(@PathVariable String matchId, @Valid @ModelAttribute("match") Match match, BindingResult errors, RedirectAttributes redir) {
        Match updatedMatch = this.matchService.findById(matchId);
        updatedMatch.setNumberGoalHome(match.getNumberGoalHome());
        updatedMatch.setNumberGoalAway(match.getNumberGoalAway());
        updatedMatch.setStatus(Constants.MATCH_FINISH_STATUS);
        this.matchService.saveMatch(updatedMatch);

        // Update handicap result.
        List<Handicap> handicapsOfMatch = updatedMatch.getHandicaps();
        for (Handicap handicap : handicapsOfMatch) {
            List<Bet> betsOfHandicap = handicap.getBets();
            Client bookie = handicap.getClient();
            for (Bet bet : betsOfHandicap) {
                bet.calculatePunterRevenue(match.getNumberGoalHome(), match.getNumberGoalAway());
                Bet updatedBet = this.betService.saveBet(bet);
                this.updateBalanceForUsers(bookie, updatedBet.getClient(), updatedBet.getPunterRevenue());
            }
        }

        redir.addAttribute(Constants.MESSAGE, "Match has been updated.");
        return "redirect:/matches";
    }

    private void updateBalanceForUsers(Client bookie, Client punter, double punterRevenue) {
        double newBookieBalance = bookie.getBalance() - punterRevenue;
        bookie.setBalance(newBookieBalance);
        this.userService.saveUser(bookie);

        double newPunterBalance = punter.getBalance() + punterRevenue;
        punter.setBalance(newPunterBalance);
        this.userService.saveUser(punter);
    }
}
