package com.fafc.bet4fun.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fafc.bet4fun.entities.Role;
import com.fafc.bet4fun.services.RoleService;

@Controller 
public class RoleController {

    public static final String NEW_ROLE = "new_role";
    public static final String DELETED_ROLE = "deleted_role";

    @Autowired
    RoleService roleService;

    @RequestMapping(value="/role/add", method = RequestMethod.GET)
    public String add() {
        return "role-add";
    }

    @RequestMapping(value = "/role/add", method = RequestMethod.POST)
    public String add(@RequestParam(name="roleName") String roleName, RedirectAttributes redir) {
        Role role = this.roleService.saveRole(roleName);
        redir.addAttribute(NEW_ROLE, role.getRoleName());

        return "redirect:/roles";
    }

    @RequestMapping(value="/roles", method = RequestMethod.GET)
    public String getAllRoles(Model model, HttpServletRequest request) {
        List<Role> roles = this.roleService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute(NEW_ROLE, request.getParameter(NEW_ROLE));
        model.addAttribute(DELETED_ROLE, request.getParameter(DELETED_ROLE));
        return "roles";
    }

    @RequestMapping(value = "/role/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(name="uuid") String uuid, RedirectAttributes redir) {
        this.roleService.deleteRole(uuid);
        redir.addAttribute(DELETED_ROLE, uuid);
        return "redirect:/roles";
    }
}