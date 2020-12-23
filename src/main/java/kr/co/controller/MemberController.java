package kr.co.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.service.MemberService;
import kr.co.vo.MemberVO;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService service;
	
	//security_암호화 기능 사용 위해
	@Inject
	BCryptPasswordEncoder pwdEncoder;
	
	//sign up get
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void getRegister() throws Exception {
		logger.info("get register");
	}
	
	//add1
	//sign up post
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public String postRegister(MemberVO vo) throws Exception {
//		logger.info("post register");
//		
//		service.register(vo);
//		return null;
//	}
	
	//=====
	
	//22강에서 수정된 회원가입 post
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String postRegister(MemberVO vo) throws Exception {
		logger.info("post register");
		int result = service.idChk(vo);
		try {
			if(result == 1) {
				return "/member/register";
			} else if(result == 0) {
				//add1
				//회원가입 요청이 들어오면 비밀번호 암호화 후 vo에 넣기
				String inputPass = vo.getUserPass();
				String pwd = pwdEncoder.encode(inputPass);
				vo.setUserPass(pwd);
				//add2
				
				//중복된 id가 없으므로 이 함수 호출
				service.register(vo);
			}
			//요기에서~ 입력된 아이디가 존재한다면 => 다시 회원가입 페이지로 돌아감
			//존재하지 않는 다면 => register
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
		//실행이 완료되면 redirect 덕분에 로그인 페이지로 이동
		return "redirect:/";
	}
	//add2
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(MemberVO vo, HttpServletRequest req, RedirectAttributes rttr) throws Exception{
		logger.info("post login");
		
		HttpSession session = req.getSession();
		
		session.getAttribute("member");
		
		MemberVO login = service.login(vo);
		
		boolean pwdMatch = pwdEncoder.matches(vo.getUserPass(), login.getUserPass());
		
		
		if(login != null && pwdMatch == true) {
			session.setAttribute("member", login);
		}else {
			session.setAttribute("member", null);
			rttr.addFlashAttribute("msg", false);
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception{
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/memberUpdateView", method = RequestMethod.GET)
	public String registerUpdateView() throws Exception {
		//로그인 시 Member 값들을 session에서 이미 받고 있어서 사용 가능하므로
		//파라미터를 받지 않게끔 함수가 구성되어 있음
		return "member/memberUpdateView";
	}
	
	//회원정보 수정 post
	@RequestMapping(value = "/memberUpdate", method = RequestMethod.POST)
	public String registerUpdate(MemberVO vo, HttpSession session) throws Exception {
		//파라미터들을 서비스로 보내기
		service.memberUpdate(vo);
		//세션을 끊어줌
		session.invalidate();
		//로그인 페이지로 redirect 하기
		return "redirect:/";
	}
	
	//signout get
	@RequestMapping(value = "/memberDeleteView", method = RequestMethod.GET)
	public String memberDeleteView() throws Exception {
		return "member/memberDeleteView";
	}
	//signout post
	@RequestMapping(value = "/memberDelete", method = RequestMethod.POST)
	public String memberDelete(MemberVO vo, HttpSession session, RedirectAttributes rttr) throws Exception {
		//세션에 있는 member를 가져와 member변수에 넣어줌
//		MemberVO member = (MemberVO)session.getAttribute("member");
//		//세션에 있는 비밀번호
//		String sessionPass = member.getUserPass();
//		//vo 로 들어오는 비밀번호
//		String voPass = vo.getUserPass();
//		
//		if(!(sessionPass.equals(voPass))) {
//			rttr.addFlashAttribute("msg", false);
//			return "redirect:/member/memberDeleteView";
//		}
		
		service.memberDelete(vo);
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 패스워드 체크
	@ResponseBody
	@RequestMapping(value="/passChk", method = RequestMethod.POST)
	public boolean passChk(MemberVO vo) throws Exception {
//		int result = service.passChk(vo);
//		return result;
		
		MemberVO login = service.login(vo);
		boolean pwdChk = pwdEncoder.matches(vo.getUserPass(), login.getUserPass());
		return pwdChk;
	}
	
	//id duplicate check
	@ResponseBody
	@RequestMapping(value = "/idChk", method = RequestMethod.POST)
	public int idChk(MemberVO vo) throws Exception {
		int result = service.idChk(vo);
		return result;
	}
}
