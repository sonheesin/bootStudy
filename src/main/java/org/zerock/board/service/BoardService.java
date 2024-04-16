package org.zerock.board.service;
//Client의 요청(request)에 대해 어떤 처리를 할지 결정하는 부분
//요청들어온 부분을 개발자가 어떻게 변환하여(가공하여) 다시 사용자에게 전달할지 결정하는 부분


import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {
    Long register(BoardDTO dto);
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
    //목록 처리

    BoardDTO get(Long bno);

    void removeWithReplies(Long bno);//삭제 기능
    void modify(BoardDTO boardDTO);


    default Board dtoToEntity(BoardDTO dto){
        Member member = Member.builder().email(dto.getWriterEmail()).build();
        //Member entity 에 생성자에 email가져와서 member 변수에 넣음
        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }

    //BoardService 인터페이스에 추가하는 entityToDTO()
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){

        BoardDTO boardDTO= BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //long 으로 나오므로 int로 처리하도록
                .build();
        return boardDTO;
    }
    
    
    
}
