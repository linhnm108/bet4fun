package com.fafc.bet4fun.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Handicap;
import com.fafc.bet4fun.services.HandicapService;

@Controller
public class HandicapController {

    @Autowired
    HandicapService handicapService;

    @RequestMapping(value="/handicaps/upcoming", method = RequestMethod.GET)
    public String getAllUpcomingHandicaps(Model model, HttpServletRequest request) {
        List<Handicap> upcomingHandicaps = this.handicapService.getAllUpcomingHandicaps();
        model.addAttribute("upcomingHandicaps", upcomingHandicaps);
        model.addAttribute(Constants.MESSAGE, request.getParameter(Constants.MESSAGE));
        return "handicaps-upcoming";
    }
}
