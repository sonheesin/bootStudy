package org.zerock.board.repository;

////DAO(Repository) 실제로 DB에 접근하여 데이터를 CRUD 하는 객체 테스트

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;
    @Test
    public void insertBoard() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            //1부터 100까지의 임의의 번호
            long bno  = (long)(Math.random() * 100) + 1;
            Member member = Member.builder().email("user"+i+"@aaa.com").build();

            Board board = Board.builder()
                    .title("Title......." +i)
                    .content("Content......." +i)
                    .writer(member)
                    .build();

            boardRepository.save(board);

        });


    }

    @Transactional //해당 메서드를 하나의 트랙잭션 처리 필요할때 다시 db 연결
    @Test
    public void testRead1(){
        Optional<Board> result = boardRepository.findById(100L);
        //데이터베이스에 존재하는 번호
        Board board = result.get();
        System.out.println(board);
        System.out.println(board.getWriter());

//        Hibernate:
//        select
//        b1_0.bno,
//                b1_0.content,
//                b1_0.moddate,
//                b1_0.regdate,
//                b1_0.title,
//                w1_0.email,
//                w1_0.moddate,
//                w1_0.name,
//                w1_0.password,
//                w1_0.regdate
//        from
//        board b1_0
//        left join
//        member w1_0
//        on w1_0.email=b1_0.writer_email
        //조인은 두 개의 테이블을 가로 방향으로 이어붙이는 작업
        // 이 말은 board 테이블을 기준으로 member 테이블을 이어붙인다는 뜻
        //ON 절은 FROM 절의 내용대로 두 테이블을 이어붙일 때
        //board 테이블의 email 컬럼과 writer 테이블의 email 컬럼의 값이 같은 row들을 이어붙이라는 뜻

//        where
//        b1_0.bno=?
//        Board(bno=100, title=Title.......100, content=Content.......100, writer=Member(email=user100@aaa.com, password=1111, name=USER100))
//        Member(email=user100@aaa.com, password=1111, name=USER100)


//
//        could not initialize proxy [org.zerock.board.entity.Member#user100@aaa.com] - no Session

        //지연로딩방식으로 로딩
        //board 테이블만 가져와서 syout 하는 것은 문제 없지만
        //board.getwirter()는 member 테이블 로딩해야하는데
        // db 연결은 이미 끝난 상태라 문제 발생

    }

    @Test
    public void testReadWithWriter( ){
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[]) result;
        System.out.println("----------------------------");
        System.out.println(Arrays.toString(arr)); //배열 내용 출력하기
    }


    @Test
    public void testGetBoardWithReply( ){
        List<Object[]> result = boardRepository.getBoardWithReply(100L);
        for(Object[]arr:result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testGetBoardWithReplyCount(){
        Pageable pageable= PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row->{
            Object[] arr=(Object[])row;
            System.out.println(Arrays.toString(arr));
        });


    }
    @Test
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));  //배열 내용 출력하기


//        Hibernate:
//        select
//        b1_0.bno,
//                b1_0.content,
//                b1_0.moddate,
//                b1_0.regdate,
//                b1_0.title,
//                b1_0.writer_email,
//                w1_0.email,
//                w1_0.moddate,
//                w1_0.name,
//                w1_0.password,
//                w1_0.regdate,
//                count(r1_0.rno)
//        from
//        board b1_0
//        left join
//        member w1_0
//        on w1_0.email=b1_0.writer_email
//        left join
//        reply r1_0
//        on r1_0.board_bno=b1_0.bno
//        where
//        b1_0.bno=?
//[Board(bno=100, title=Title.......100, content=Content.......100, writer=Member(email=user100@aaa.com, password=1111, name=USER100)), Member(email=user100@aaa.com, password=1111, name=USER100), 3]

    }
    @Test
    public void testSearch1(){
        boardRepository.search1();
    }

    @Test
    public void testSearchPage(){
        Pageable pageable=PageRequest.of(0,10,Sort.by("bno").descending()
                .and(Sort.by("title").ascending()));
        Page<Object[]>result=boardRepository.searchPage("t","1",pageable);

    }





}


