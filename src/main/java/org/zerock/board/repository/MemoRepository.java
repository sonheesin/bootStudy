package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository <Memo,Long>{
    //extends JpaRepository <엔티티명 , pk 타입>
    //jpa 에서 crud를 담당한다.
    //JpaRepository 내장된 메서드

    //쿼리메서드는 메서드명 자체가 쿼리문으로 동작한다.


    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from,Long to);
    //List<Memo> 리턴 타입 -> 리스트 타입에 객체는 memo
    //매개값으로 받은 from 부터 to 까지 select 를 진행하여 리스트로 리턴하는 쿼리메서드



    Page<Memo> findByMnoBetween(Long from,Long to,Pageable pageable);
    //Page<Memo> 리턴 타입 -> 리스트 타입에 객체는 memo
    //매개값으로 받은 from 부터 to 까지 select 를 진행하여 페이징타입으로 리턴하는 쿼리메서드


    //예를 들어 10보다 작은 데이터를 삭제한다.
    void deleteMemoByMnoLessThan(Long num);

    //@Query 는 순수한 sql 쿼리문으로 작성한다. 단 , 테이블 명이 아니라 엔티티 명으로 사용함.
    //쿼리문은 대문자 , 변수명은 소문자
    @Query("SELECT m FROM Memo m ORDER BY m.mno DESC ")
    List<Memo> getListDese(); //내가 만든 메서드 명

    //매개값이 있는 @Query 문 : 값 ( 타입으로 받음)
    @Query("UPDATE Memo m SET m.memoText =:memoText WHERE m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("mno") String memoText);

//    //매개값이 객체(빈)으로 들어올 경우
//    @Query("UPDATE Memo m SET m.memoText =: #{memoBean.memoText} WHERE m.mno = #{memoBean.mno}")
//    int updateMemoBean(@Param("memoBean")Memo memo);

    //@Query 메서드로 페이징 처리해보기 -> 리턴 타입이  Page<Memo>
    //쿼리문 두개가 들어갈때는 value
    @Query(value = "SELECT m FROM Memo m WHERE m.mno > :mno"
                    ,countQuery = "SELECT count(m) FROM Memo m WHERE m.mno > :mno")
    Page<Memo> getListWithQuery(Long mno,Pageable pageable);

    //db 에 존재하지 않는 값 처리 해보기 : 예를 들어 날짜
    @Query(value = "SELECT m.mno , m.memoText , CURRENT_DATE FROM Memo m WHERE m.mno > :mno" ,
                    countQuery = "SELECT count(m) FROM Memo m WHERE m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno,Pageable pageable);

    //Native SQL 처리 : DB용 쿼리로 사용 기법
    //entity 대신에 테이블 명을 사용 (대문자가 들어가면 클래스)
    @Query(value = "SELECT m FROM memo WHERE mno" , nativeQuery = true)
    List<Object[]> getNativeResult();





}
