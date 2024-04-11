package org.zerock.board.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Guestbook;
import org.zerock.board.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest //스프링부트 테스트용 명시


public class GuestbookRepositoryTests {

    @Autowired //인터페이스 자동 주입 GuestbookRepository SAVE FINDBYID 사용 가능
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("제목" + i)
                    .content("내용" + i)
                    .writer("유저" +(i % 10)) //10으로 나눈 나머지 user 0~ user30까지
                    .build(); //빌더 패턴은 @Builder 필수
            System.out.println(guestbookRepository.save(guestbook));
            //jpa save 메서드를 실행하며 출력까지 진행
        });
    }


    @Test
    public void updateTest() {
        //게시물을 수정하는 테스트 진행
        Optional<Guestbook> result = guestbookRepository.findById(300L);
        //300번에 게시물을 찾아본다 .찾아 온 게시물을 엔티티가 result에 들어감
        //존재하는 번호로 테스트

        if (result.isPresent()) { //객체가 있으면


            Guestbook guestbook = result.get(); //엔티티를 가져와 변수에 넣음

            System.out.println(guestbook.getTitle());
            System.out.println(guestbook.getContent());
            System.out.println(guestbook.getWriter()); //300번 찾아온거 출력하고

            guestbook.changerTitle("수정된 제목"); //엔티티에 만든 메서드
            guestbook.changerContent("수정된 내용"); //엔티티에 만든 메서드 //수정된것을

            guestbookRepository.save(guestbook); //있으면 update, 없으면 insert //여기에다가 저장
            
        }
    }

    @Test
    public void testQuery1(){ //단일 조건으로 쿼리 생성
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());
        //페이지 타입으로 요청을 처리함 (0번 페이지에 10개씩 객체생성 , gno 기준으로 내림차순)
        QGuestbook qGuestbook = QGuestbook.guestbook; //1
        //Querydsl 용 객체 생성 ( 동적처리용)

        String keyword = "9"; //포론트 페이지에서 1번을 찾겠다라는 변수

        BooleanBuilder builder =new BooleanBuilder();//다중조건처리용 객체

        BooleanExpression expressionTitle =qGuestbook.title.contains(keyword);
        BooleanExpression expressionContent =qGuestbook.content.contains(keyword);
        //expression (표현) ?title=1 표현식이 생성됨
        BooleanExpression exAll=expressionTitle.or(expressionContent);//2개의 조건을 합체

        builder.and(exAll); //다중조건 처리용 객체에 표현식을 밀어 넣음
        builder.and(qGuestbook.gno.gt(0L)); //where 문 추가 (gno >0)

        Page<Guestbook> result=guestbookRepository.findAll(builder,pageable);
        //페이지 타입에 객체가 나옴 (.findall은 검색된 모든 객체가 나옴)
        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
