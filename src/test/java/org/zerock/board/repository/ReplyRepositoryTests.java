package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;
    @Test
    public void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            //1부터 100까지의 임의의 번호
            long bno  = (long)(Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply......." +i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);

        });

    }

    @Test
    public void readReply1() {
        Optional<Reply> result = replyRepository.findById(1L);
        //데이터베이스에 존재하는 번호
        Reply reply = result.get();
        System.out.println(reply);
        System.out.println(reply.getBoard());

//        Hibernate:
//        select
//        r1_0.rno,
//                b1_0.bno,
//                b1_0.content,
//                b1_0.moddate,
//                b1_0.regdate,
//                b1_0.title,
//                w1_0.email,
//                w1_0.moddate,
//                w1_0.name,
//                w1_0.password,
//                w1_0.regdate,
//                r1_0.replyer,
//                r1_0.text
//        from
//        reply r1_0
//        left join
//        board b1_0
//        on b1_0.bno=r1_0.board_bno
//        left join
//        member w1_0
//        on w1_0.email=b1_0.writer_email
//        where
//        r1_0.rno=?
//        Reply(rno=1, text=Reply.......1, replyer=guest, board=Board(bno=46, title=Title.......46, content=Content.......46, writer=Member(email=user46@aaa.com, password=1111, name=USER46)))
//        Board(bno=46, title=Title.......46, content=Content.......46, writer=Member(email=user46@aaa.com, password=1111, name=USER46))

        //reply 테이블 ,board 테이블 , member 테이블까지 모두 조인
    }

    @Test
    public void testListByBoard(){
        List<Reply>replyList=replyRepository.getRepliesByBoardOrderByRno(
                Board.builder().bno(97L).build());
        replyList.forEach(reply -> System.out.println(reply));

    }

    }


