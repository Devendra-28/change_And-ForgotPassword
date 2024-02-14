package com.code.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.code.entities.User;
import com.code.repository.UserRepo;
import com.code.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;
	
	@ModelAttribute
	public void CommonUser(Principal p,Model m) {
		if(p!=null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user",user);	
		}	
	}
	
	@GetMapping("/")
	public String index() {
		return "index";	
	}
	@GetMapping("/register")
	public String register() {
		return "register";
	}	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	@PostMapping("saveUser")
	public String SaveUser(@ModelAttribute User user, HttpSession session) {
		
		//System.out.println(user);
		
		User u = userService.saveUser(user);
		if(u!=null) {
			//System.out.println("save Success");
			session.setAttribute("msg", "Register Successfully..");
		}else {
		//	System.out.println("Error in Server");
			session.setAttribute("msg", "Something wrong on server..");
		}
		return "redirect:/register";
	}
	@GetMapping("/loadforgotpass")
	public String LoadForgotPassword() {
		return "forgot_pass";	
	}
	@GetMapping("/loadresetpass/{id}")
	public String LoadResetPassword(@PathVariable int id,Model m) {
		m.addAttribute("id",id);
		return "reset_pass";
	}
	@PostMapping("/forgotpassword")
	public String ForgotPassword(@RequestParam String email,@RequestParam String mobileNo, HttpSession session) {
		User user = userRepo.findByEmailAndMobileNo(email, mobileNo);
		if(user != null) {
			
			return "redirect:/loadresetpass/"+user.getId();
		}else {
			session.setAttribute("msg", "Invalid email and mobile No..");
			return "forgot_pass";
		}
	}
	@PostMapping("/changepassword")
	public String resetpassword(@RequestParam String pass,@RequestParam Integer id,HttpSession session) {
		User user = userRepo.findById(id).get();
		String encode = PasswordEncoder.encode(pass);
		user.setPassword(encode);
		User updateuser = userRepo.save(user);
		
		 if(updateuser != null) {
			 session.setAttribute("msg", "password change sucssfully..");
		 }
		return "redirect:/loadforgotpass";
		
	}
	
	
}
