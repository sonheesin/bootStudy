package org.zerock.board.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
//실제 DB의 테이블과 매칭될 핵심 클래스이다.
//최대한 외부에서 Entity 클래스의 Setter method를 사용하지 않도록 해당 클래스 안에서 필요한 logic을 구현해야한다.
// 이 때, Constructor(생성자) 또는 Builder를 사용한다.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) //명시적으로 LAZY 로딩 지정
    private Member writer; // 연관관계 지정

    public void changeTitle(String title){
        this.title=title;
    }
    public void changeContent(String content){
        this.content=content;
    }


}
