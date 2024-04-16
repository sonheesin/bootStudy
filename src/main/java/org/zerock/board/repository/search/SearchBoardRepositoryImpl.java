package org.zerock.board.repository.search;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
//배열로서 각 인덱스마다 다른 타입의 값을 가지고 있는 배열을 뜻한다.
// 주로 파이썬과 자바스크립트에서 많이 사용하는 타입이다.
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2


public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    public SearchBoardRepositoryImpl(){

            super(Board.class);
        }

    @Override
    public Board search1() {

        log.info("search1........................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //select b ,w , count(r) from board b
        //left join b.writer w left join reply r on r.board=b

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("---------------------------");
        log.info(tuple);
        log.info("---------------------------");

        List<com.querydsl.core.Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage.............................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);

        //JPQL(Java Persistence Query Language)
        //JPQL은 엔티티 객체를 조회하는 객체지향 쿼리다.
        //테이블을 대상으로 쿼리하는 것이 아니라 엔티티 객체를 대상으로 쿼리한다.
        //SQL과 비슷한 문법을 가지며, JPQL은 결국 SQL로 변환된다.


        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());
        // 튜플 생성

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // 빌더 생성
        BooleanExpression expression = board.bno.gt(0L);
        //표현식 생성

        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split("");

            //타입 정보 배열로 추출
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            ;// 검색 조건 생성하기


            for (String t:typeArr) {   // 검색 타입의 종류 추출
                switch (t){
                    case "t":      // 제목
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":    // 작성자 id
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":     // 내용
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);   // 조건 추가
        }
        tuple.where(booleanBuilder);      // 조건 적용

        //order by
        Sort sort = pageable.getSort();    //      // 정렬 조건
        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order->{
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();  // gno, title 등 정렬할 속성

            // OrderSpecifier에는 정렬이 필요

            // PathBuilder를 생성할 때 문자열로 된 이름은 JPQLQuery를 생성할 때 이용하는 변수명과 동일해야 함

            PathBuilder orderByExpression = new PathBuilder(Board.class,"board");

            tuple.orderBy(new OrderSpecifier(direction,orderByExpression.get(prop)));

        });
        tuple.groupBy(board);    // 그룹 함수 때문에 추가
        //page 처리

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple>result = tuple.fetch();    // 쿼리 실행
        log.info(result);    // 결과물 확인

        long count=tuple.fetchCount();         // 갯수 가져오기
        log.info("COUNT" + count);
        return new PageImpl<Object[]>(
                result.stream().map(t->t.toArray()).collect(Collectors.toList()), // 리스트로 반환
                pageable,count);

        }

    }



