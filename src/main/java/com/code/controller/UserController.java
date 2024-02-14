package com.code.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.code.entities.User;
import com.code.repository.UserRepo;
import com.code.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
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
	
	@GetMapping("/profile")
	public String profile() {
		return "profile";
		
	}
	@GetMapping("/changepass")
	public String LoadChangePassword() {
		return "user/change_password";
	}
	@PostMapping("/updatepassword")
	public String ChangePassword(Principal p,@RequestParam("oldpass")String oldpass,
			@RequestParam("newpass") String newpass,HttpSession Session) {
		 String email = p.getName();
		   User loginUser  = userRepo.findByEmail(email);
		 boolean f=  PasswordEncoder.matches(oldpass,loginUser.getPassword());
		   if(f) {
			   loginUser.setPassword(PasswordEncoder.encode(newpass));
			  User updatepassUser = userRepo.save(loginUser);
			  
			  if(updatepassUser!=null) {
				  Session.setAttribute("msg", "password change succesfully");
			  }else {
				  Session.setAttribute("msg", "something wrong on server");
			  }
		   }else {
			   Session.setAttribute("msg", "old password is incorrect");
		   }
			return "redirect:/user/changepass";
	}
	
}
