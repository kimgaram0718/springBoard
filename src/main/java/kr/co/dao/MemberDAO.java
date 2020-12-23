package kr.co.dao;

import kr.co.vo.MemberVO;

public interface MemberDAO {
	
	// 회원가입
	public void register(MemberVO vo) throws Exception;
	
	//login
	public MemberVO login(MemberVO vo) throws Exception;
	//member info edit
	public void memberUpdate(MemberVO vo) throws Exception;
	//member signout
	public void memberDelete(MemberVO vo) throws Exception;
	//password check
	public int passChk(MemberVO vo) throws Exception;
	//id duplicate check
	public int idChk(MemberVO vo) throws Exception;
	
}