package org.zerock.board.dto;
//DTO는 계층간 데이터 교환을 도와주는 객체이다.
//세부적인 로직을 갖고 있지 않은 순수한 데이터 객체라고 생각하면 된다.
// 오직 Getter/Setter method만 가지고 있으며, 계층간 데이터 교환시 사용한다.


import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor //@Builder 생성자에 필요함
@NoArgsConstructor //@Builder 생성자에 필요함

public class BoardDTO {
    private Long bno;
    private String title;
    private String content;
    private String writerEmail; //작성자의 이메일 (id)
    
    private String writerName; //작성자의 이름
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount;//해당게시글의 댓글 수
    
    
    
}
