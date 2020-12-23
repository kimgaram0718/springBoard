package kr.co.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.dao.BoardDAO;
import kr.co.util.FileUtils;
import kr.co.vo.BoardVO;
import kr.co.vo.SearchCriteria;

@Service
public class BoardServiceImpl implements BoardService {
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	@Inject
	private BoardDAO dao;

	// 게시글 작성
	@Override
	public void write(BoardVO boardVO, MultipartHttpServletRequest mpRequest) throws Exception {
		dao.write(boardVO);
		
		//add1
		//24강 첨부파일 관련
		List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(boardVO, mpRequest);
		int size = list.size();
		for(int i=0; i<size; i++) {
			dao.insertFile(list.get(i));
		}
		//add2
	}

	// 게시물 목록 조회
	@Override
	public List<BoardVO> list(SearchCriteria scri) throws Exception {

		return dao.list(scri);
	}
	
	//게시물 총 개수
	@Override
	public int listCount(SearchCriteria scri) throws Exception {
		// TODO Auto-generated method stub
		return dao.listCount(scri);
	}
	
	// 게시물 목록 조회
	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public BoardVO read(int bno) throws Exception {
		//add1
		dao.boardHit(bno);
		//add2
		return dao.read(bno);
	}
	@Override
	public void update(BoardVO boardVO,
					   String[] files,
					   String[] fileNames,
					   MultipartHttpServletRequest mpRequest) throws Exception {

		dao.update(boardVO);
		//파일 업데이트 할 값들을 list에 담음
		List<Map<String, Object>> list = fileUtils.parseUpdateFileInfo(boardVO, files, fileNames, mpRequest);
		Map<String, Object> tempMap = null;
		int size = list.size();
		//fileUtils.parseUpdateFileInfo() 결과의 size 만큼 for문을 돌림
		for(int i=0; i<size; i++) {
			//tempMap 에 list.get(i) 를 담고 if문을 이용하여 tempMap 에서 IS_NEW 를 꺼내옴
			tempMap = list.get(i);
			if(tempMap.get("IS_NEW").equals("Y")) {
				//값이 Y와 같으면 insertFile 을 실행시킴
				dao.insertFile(tempMap);
			} else {
				//다르면 updateFile 을 실행시킴
				dao.updateFile(tempMap);
			}
		}
	}

	@Override
	public void delete(int bno) throws Exception {
		
		dao.delete(bno);
	}

	@Override
	public List<Map<String, Object>> selectFileList(int bno) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectFileList(bno);
	}

	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectFileInfo(map);
	}
}
