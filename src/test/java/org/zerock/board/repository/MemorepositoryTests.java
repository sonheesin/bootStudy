package org.zerock.board.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest //부트용 테스트 코드임을 명시함

public class MemorepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        //객체 주입 테스트 (memoRepository는 인터페이스임을 기억해라)
        //인터페이스는 구현객체가 있어야된다.
        System.out.println(memoRepository.getClass().getName());
        //memoRepository 생성된 객체의 클래스명과 이름을 알아보자.
        //콘솔결과 jdk.proxy3.$Proxy115 (동적 플록시: 인터페이스 실행 구현 클래스임)

    }

    @Test
    public void testInsertDummies(){
        //memo 테이블에 더미데이터 추가
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo =Memo.builder().memoText("샘플..." +i).build();
            //memo 클래스에 memoText(1~100) 생성 반복
            memoRepository.save(memo); //.save(jpa 상속으로 사용)
            //save 없으면 insert,있으면 update 함
        });

    }
    @Test
    public void testSelect(){
        //있는 정보 가져오기 (mno를 이용)
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        //import java.util.Optional;
        // findById(mno) -> select * from 표 where mno = 100;

        System.out.println("=============mno = 100=======================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }

    }
    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno); //getOne 현재 차단된 메서드 (보안상)
        System.out.println("===========Long mno = 100L; .getOne(mno)===================");
        System.out.println(memo);
     //[org.zerock.board.entity.Memo#100] - no session ->  @Transactional 필요
        //===============Long mno = 100L;.getone(mno)========================
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        tbl_memo m1_0
//        where
//        m1_0.mno=?
//        Memo(mno=100, memoText=Sample...100)

    }
    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(300L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));

        //.save(memo));  ->없으면 insert , 있으면 update
//        Hibernate:
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        tbl_memo m1_0
//        where
//        m1_0.mno=?
//        Hibernate:
//        update
//                tbl_memo
//        set
//        memo_text=?
//        where
//        mno=?
//        Memo(mno=100, memoText=Update Text)


//        Hibernate:
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        tbl_memo m1_0
//        where
//        m1_0.mno=?
//        Hibernate:
//        insert
//                into
//        tbl_memo
//                (memo_text)
//        values
//                (?)
//        Memo(mno=201, memoText=Update Text)
//
    }

    @Test
    public void testDelete(){
        Long mno = 300L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0,10);
        //내장된 페이징 처리 pagesize 10페이지중 현재는 pagenumber o 페이지를 보여라
        Page<Memo> result = memoRepository.findAll(pageable);
        //findAll 페이지 전부를 가져와라
        System.out.println(result);
//
//        Hibernate:
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        tbl_memo m1_0
//        limit (레코드 제한 개수)
//                ?,?
//        Hibernate:
//        select
//        count(m1_0.mno)
//        from
//        tbl_memo m1_0
//        Page 1 of 21 containing org.zerock.board.entity.Memo instances
    }

    @Test
    public void testPageDefault1() {
        // jpa에 내장된 페이징, 정렬 기법 활용

        Sort sort1=Sort.by("mno").descending();
        Sort sort2=Sort.by("memoText").ascending();
        Sort sortAll =sort1.and(sort2); //내림차순 번호 & 메모 텍스트 오름차순


        Pageable pageable = PageRequest.of(0, 10,sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);


        //Hibernate:
        //    select
        //        m1_0.mno,
        //        m1_0.memo_text
        //    from
        //        tbl_memo m1_0
        //    limit
        //        ?, ?
        //Hibernate:
        //    select
        //        count(m1_0.mno)
        //    from
        //        tbl_memo m1_0
        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances

        System.out.println("---------------------------------------");

        System.out.println("Total Pages: "+result.getTotalPages()); // 총 몇 페이지

        System.out.println("Total Count: "+result.getTotalElements()); // 전체 개수

        System.out.println("Page Number: "+result.getNumber()); // 현재 페이지 번호

        System.out.println("Page Size: "+result.getSize()); // 페이지당 데이터 개수

        System.out.println("has next page?: "+result.hasNext());    // 다음 페이지 존재 여부

        System.out.println("first page?: "+result.isFirst());  //  시작페이지 여부

        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances
        //---------------------------------------
        //Total Pages: 10
        //Total Count: 99
        //Page Number: 0
        //Page Size: 10
        //has next page?: true
        //first page?: true

        System.out.println("-----------------------------------");

        for(Memo memo : result.getContent()) {
            System.out.println(memo);
            //-----------------------------------
            //Memo(mno=1, memoText=Sample....1)
            //Memo(mno=2, memoText=Sample....2)
            //Memo(mno=3, memoText=Sample....3)
            //Memo(mno=4, memoText=Sample....4)
            //Memo(mno=5, memoText=Sample....5)
            //Memo(mno=6, memoText=Sample....6)
            //Memo(mno=7, memoText=Sample....7)
            //Memo(mno=8, memoText=Sample....8)
            //Memo(mno=9, memoText=Sample....9)
            //Memo(mno=10, memoText=Sample....10)
        }

    }

    @Test
    public void testQueryMethods(){
        List<Memo> list=memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);
        //memoRepository 에 있는 쿼리메서드를 실행하여 리스트 객체로 받음
//
//        Hibernate:
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        tbl_memo m1_0
//        where
//        m1_0.mno between ? and ?
//                order by
//        m1_0.mno desc
//

        for(Memo memo : list){
            System.out.println(memo);
        }//받은 리스트객체를 for 문을 이용하여 콘솔 출력
    }

    @Test
    public void testQuerymethodWithPage(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        //페이지 타입은 of를 이용해서 요청을 처리함 , 0번에 10개를 mno 기준으로 내림차순 정렬을 매개 값으로 전달
        Page<Memo> result =memoRepository.findByMnoBetween(10L,50L,pageable);
        result.get().forEach(memo-> System.out.println(memo));
    }

    @Transactional //delete 에서는 2개의 쿼리문이 동작해야함
    @Commit //delete인 경우에는 aotu commit 이 안됨
    @Test
    public void testDeleteQuerMethods(){
        //쿼리 메서드로 delete 처리를 하면 9번의 쿼리문이 전달됨 (비효율적) ->@Query를 사용해야 좋다
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}

