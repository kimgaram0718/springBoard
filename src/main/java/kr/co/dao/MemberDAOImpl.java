package kr.co.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.vo.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO {
	
	@Inject SqlSession sql;
	
	// 회원가입
	@Override
	public void register(MemberVO vo) throws Exception {
		sql.insert("memberMapper.register", vo);
	}

	//login
	@Override
	public MemberVO login(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		return sql.selectOne("memberMapper.login", vo);
	}

	//서비스에서 보낸 파라미터들을 memberUpdate(MemberVO vo) 에 담아내기
	@Override
	public void memberUpdate(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		//vo 에 담긴 파라미터들은 memberMapper.xml 에 memberMapper 라는 
		//namespace에 아이디가 memberUpdate 인 쿼리에 파라미터들을 넣어줌
		sql.update("memberMapper.memberUpdate", vo);
	}

	//업데이트와 흐름이 같음 
	@Override
	public void memberDelete(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		//MemberVo 에 담긴 값들을 보내줌
		//그러면 xml 에서 memberMapper.memberDelete 에 보면
		//#{userId}, #{userPass} 에 파라미터 값이 매칭이 됨
		sql.delete("memberMapper.memberDelete", vo);
	}

	//password check
	@Override
	public int passChk(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		int result = sql.selectOne("memberMapper.passChk", vo);
		return result;
	}

	//id duplicate check
	@Override
	public int idChk(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		int result = sql.selectOne("memberMapper.idChk", vo);
		return result;
	}
}