package org.pqh.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.pqh.service.InsertService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InsertController extends ExceptionController {
	@Resource
	private InsertService is;
	@RequestMapping("/insert.do")
	public void execute(Integer aid){
		is.insertBili(aid,1);
		//return "redirect:findaids.do?startid="+(aid-20);
	}
	@RequestMapping("/count.do")
	public void execute_1(int aid){
		is.insertLimit(aid);
	}
	
}
