package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.board.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook,Long>,
        QuerydslPredicateExecutor<Guestbook> {// Guestbook의 PK는 Long 타입
    //extends JpaRepository<앤티티명,PK 타입>
    //JPA가 CRUD를 해준다.

    //inset 작업 : save 메서드 ( 엔티티객체)
    //select 작업 : findById (키 타입),getOne(키 타입)
    //update 작업: save(엔티티 객체)
    //delete 작업 : deleteById (키 타입) , delete(엔티티 객체)

    //Querydsl : Q도메인을 이용해서 자동으로 검색 조건을 완성 시킴(다중검색용)
    //http://querydsl.com/ 사이트 -> 참고하여 api 의존성 주입을 받아야한다.
    //QuerydslPredicateExecutor<Guestbook>  인터페이스는 다중상속됨 (Qdomain 사용)

}
