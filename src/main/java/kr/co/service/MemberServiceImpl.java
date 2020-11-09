package kr.co.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.dao.MemberDAO;
import kr.co.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Inject MemberDAO dao;
	
	@Override
	public void register(MemberVO vo) throws Exception {
		
		dao.register(vo);
		
	}
	
}