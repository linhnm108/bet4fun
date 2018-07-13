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

import com.fafc.bet4fun.api_models.SportDeer;
import com.fafc.bet4fun.api_models.Token;
import com.fafc.bet4fun.api_models.UpcomingMatch;
import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Bet;
import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.entities.Handicap;
import com.fafc.bet4fun.entities.Match;
import com.fafc.bet4fun.entities.SyncHistory;
import com.fafc.bet4fun.services.AuthenticationService;
import com.fafc.bet4fun.services.BetService;
import com.fafc.bet4fun.services.HandicapService;
import com.fafc.bet4fun.services.MatchService;
import com.fafc.bet4fun.services.SportDeerService;
import com.fafc.bet4fun.services.SyncHistoryService;
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

    @Autowired
    SyncHistoryService syncService;

    @Autowired
    SportDeerService sportDeerService;

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
        Handicap handicap1x2 = new Handicap(match.getScheduleDate());
        handicap1x2.setHandicapType(Constants.HANDICAP_1x2);
        model.addAttribute("handicap", handicap1x2);

        return "handicap-add";
    }

    @RequestMapping(value="/match/{matchId}/handicap/over-under/create", method = RequestMethod.GET)
    public String addOverUnderHandicap(@PathVariable String matchId, Model model) {
        Match match = this.matchService.findById(matchId);
        model.addAttribute("match", match);
        Handicap handicapOverUnder = new Handicap(match.getScheduleDate());
        handicapOverUnder.setHandicapType(Constants.HANDICAP_OVER_UNDER);
        model.addAttribute("handicap", handicapOverUnder);

        return "handicap-over-under-add";
    }

    @RequestMapping(value="/match/{matchId}/handicap/over-under/create", method = RequestMethod.POST)
    public String addOverUnderHandicap(@PathVariable String matchId, @Valid @ModelAttribute("handicap") Handicap handicap, BindingResult errors, RedirectAttributes redir) {

        handicap.convertLocalExpiredDateToUTC();
        handicap.setClient(this.authenticationService.getLoggedInUser());
        handicap.setHandicapType(Constants.HANDICAP_OVER_UNDER);
        handicap = this.handicapService.saveHandicap(handicap);

        Match match = this.matchService.findById(matchId);
        match.getHandicaps().add(handicap);
        this.matchService.saveMatch(match);
        redir.addAttribute(Constants.MESSAGE, "You has added a handicap for match " + match.getMatchId() + " successfully.");
        return "redirect:/matches/upcoming";
    }

    @RequestMapping(value="/match/{matchId}/handicap/create", method = RequestMethod.POST)
    public String addHandicap(@PathVariable String matchId, @Valid @ModelAttribute("handicap") Handicap handicap, BindingResult errors, RedirectAttributes redir) {

        handicap.convertLocalExpiredDateToUTC();
        handicap.setClient(this.authenticationService.getLoggedInUser());
        handicap.setHandicapType(Constants.HANDICAP_1x2);
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
                bet.calculatePunterRevenue(match.getNumberGoalHome(), match.getNumberGoalAway(), handicap.getHandicapType());
                Bet updatedBet = this.betService.saveBet(bet);
                this.updateBalanceForUsers(bookie, updatedBet.getClient(), updatedBet.getPunterRevenue());
            }
        }

        redir.addAttribute(Constants.MESSAGE, "Match has been updated.");
        return "redirect:/matches";
    }

    @RequestMapping(value="/matches/sync", method = RequestMethod.GET)
    public String syncMatches(Model model) {
        model.addAttribute("syncHistory", new SyncHistory());
        model.addAttribute("syncHistories" , this.syncService.getAllSyncHistory());
        return "matches-sync";
    }

    @RequestMapping(value="/matches/sync", method = RequestMethod.POST)
    public String syncMatches(@Valid @ModelAttribute("syncHistory") SyncHistory syncHistory, BindingResult errors, RedirectAttributes redir) {
        Token token = this.sportDeerService.getAccessToken(syncHistory.getRefreshToken());
        SportDeer sportDeer = this.sportDeerService.getUpcommingMatches(token.getNew_access_token(), syncHistory.getFromDate(), syncHistory.getToDate());
        this.syncMatchFromSportDeer(sportDeer);

        this.syncService.saveSyncHistory(syncHistory);

        redir.addAttribute(Constants.MESSAGE, "Sync successfully.");
        return "redirect:/matches/sync";
    }

    private void syncMatchFromSportDeer(SportDeer sportDeer) {
        for (UpcomingMatch upcomingMatch : sportDeer.getDocs()) {
            Match match = new Match(upcomingMatch);
            this.matchService.saveMatch(match);
        }
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
