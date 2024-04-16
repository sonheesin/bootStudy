package org.zerock.board.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister() {
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("제목")
                .content("내용")
                .writer("유저")
                .build();
        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("이전" + resultDTO.isPrev());
        System.out.println("다음" + resultDTO.isNext());
        System.out.println("총합" + resultDTO.getTotalPage()); //총 페이지 가져옴

        System.out.println("----------------------------------------------------");

        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
        System.out.println("=====================================================");
        resultDTO.getPageList().forEach(i-> System.out.println(i));
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc") // 검색조건
                .keyword("한글") //검색 키워드
                .build();

        PageResultDTO<GuestbookDTO,Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("이전"  + resultDTO.isPrev());
        System.out.println("다음"  + resultDTO.isNext());
        System.out.println("전체"  + resultDTO.getTotalPage());

        System.out.println("--------------------------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);

        }
        System.out.println("============================");
        resultDTO.getPageList().forEach(i-> System.out.println(i));
    }
    
}
