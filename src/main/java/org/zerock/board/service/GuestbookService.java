package org.zerock.board.service;
//Client의 요청(request)에 대해 어떤 처리를 할지 결정하는 부분
//요청들어온 부분을 개발자가 어떻게 변환하여(가공하여) 다시 사용자에게 전달할지 결정하는 부분

import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Guestbook;

public interface GuestbookService {
    Long register(GuestbookDTO dto);
    GuestbookDTO read(Long gno);
    void remove(Long gno);
    void modify(GuestbookDTO dto);



    PageResultDTO<GuestbookDTO , Guestbook> getList (PageRequestDTO requestDTO);


    default Guestbook doToEntity(GuestbookDTO dto){
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }
    default GuestbookDTO entityToDto(Guestbook entity){
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return  dto;
    }

}
