package org.zerock.board.repository;
//DAO(Repository) 실제로 DB에 접근하여 데이터를 CRUD 하는 객체이다
//Service와 DB를 연결해주는 역할을 하고 있으며,
// 인터페이스와 이에 대한 구현체를 만들어서 구현체에 CRUD관련 기능을 구현하고,
// 이를 DI(Dependency Injection) 해주는 방식으로 사용된다.

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;


public interface ReplyRepository extends JpaRepository<Reply,Long> {
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno=:bno")
    //Reply의 r 해당 번호 삭제
    void deleteByBno(Long bno);

    //게시물로 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);




}
