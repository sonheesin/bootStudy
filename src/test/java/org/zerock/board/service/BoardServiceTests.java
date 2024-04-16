package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

@SpringBootTest
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test....")
                .writerEmail("user55@aaa.com") //현재 데이터베이스에 존재하는 회원 이메일
                .build();
        Long bno = boardService.register(dto);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO =new  PageRequestDTO();

        PageResultDTO<BoardDTO,Object[]> result = boardService.getList(pageRequestDTO);
        //보드서비스에서 처리한 getlist 를 결과 dto 배열에 넣음

        for(BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }




    }

    @Test
    public void testGet(){
        Long bno =100L;
        BoardDTO boardDTO = boardService.get(bno);
        System.out.println(boardDTO);

    }

    @Test
    public void testRemove(){
        Long bno =1L;
        boardService.removeWithReplies(bno);
    }


    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                        .bno(2L)
                        .title("제목변경")
                        .content("내용변경")
                        .build();
        boardService.modify(boardDTO);
    }



}
