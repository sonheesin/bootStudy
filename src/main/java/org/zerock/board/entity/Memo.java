package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_memo") //db 테이블 명을 지정
@ToString //객체가 아닌 문자로
@Getter
@Builder //메서드.필드(값).필드(값).bulier; (빌더 패턴)
@AllArgsConstructor //@AllArgsConstructor new 클래스(모든필드값 파라미터로 만듬);
@NoArgsConstructor // @NoArgsConstructor (new 클래스();)


public class Memo {
    //엔티티는 데이터베이스에 테이블과 필드를 생성시켜 관리하는 객체
    //엔티티를 이용해서 jpa를 확설화하면서 application.properties dp
    @Id //기본키를 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200 , nullable = false)
    private String memoText;


}
