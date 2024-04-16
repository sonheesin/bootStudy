package org.zerock.board.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
//Lombok에서 @Getter, @Setter 어노테이션 처럼
// @RequiredArgsConstructor어노테이션은 클래스에 선언된
// final 변수들,필드들을 매개변수로 하는 생성자를 자동으로 생성해주는 어노테이션입니다.
@Log4j2

public class BoardServiceImpl implements  BoardService{
    private final BoardRepository repository; //자동 주입 final
    private final ReplyRepository replyRepository;

    @Override
    public  Long register(BoardDTO dto){
        log.info(dto); //보드 dto 확인
        Board board = dtoToEntity(dto);
        //dtoToEntity()는 DTO 연관관계를 가진 board 엔티티 객체와
        //member 엔티티 객체를 구성해야하므로 내부적으로 member 엔티티를 처리하는 과정을 거쳐야함
        //이때 member는 실제 데이터베이스 에 있는 이메일 주소를 사용해야합니다
        //작성된 dtoToEntity()는 실제 게시물 등록하는 boardServiceImpl의 register()에서 사용

        repository.save(board);
        return board.getBno();
    }

    @Override
    //getList는 dtoToEntity() 이용하여 pageResultDTO 객체를 구성하는 부분
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);
        Function<Object[],BoardDTO> fn = (en-> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));

   // Page<Object[]> result = repository.getBoardWithReplyCount(
     //       pageRequestDTO.getPageable(Sort.by("bno").descending()));


        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()));
        return new PageResultDTO<>(result,fn);

    }

    @Override

    //게시물 조회는 BoardRepository 의 Board 엔티티와 Member엔티티 ,댓글수(Long)를
    //가져오는 getBoardByBno 이용해서 처리
    public BoardDTO get(Long bno) {

        Object result= repository.getBoardByBno(bno);
        Object[] arr =(Object[]) result;
        return entityToDTO((Board)arr[0],(Member) arr[1],(Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) { //삭제 기능 구현 ,트랜잭션 추가
        //댓글부터 삭제
        replyRepository.deleteByBno(bno);
        repository.deleteById(bno);


    }


    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = repository.getOne(boardDTO.getBno());

        if(board != null) {

            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            repository.save(board);
        }
    }



}
