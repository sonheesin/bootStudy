package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.Memo;

public interface MemoRepository extends JpaRepository <Memo,Long>{
    //extends JpaRepository <엔티티명 , pk 타입>
    //jpa 에서 crud를 담당한다.
    //JpaRepository 내장된 메서드
}
