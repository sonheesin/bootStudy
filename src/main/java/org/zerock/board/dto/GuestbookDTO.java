package org.zerock.board.dto;
//DTO는 계층간 데이터 교환을 도와주는 객체이다.
//세부적인 로직을 갖고 있지 않은 순수한 데이터 객체라고 생각하면 된다.
// 오직 Getter/Setter method만 가지고 있으며, 계층간 데이터 교환시 사용한다.

//Entity 클래스와 DTO 클래스를 분리하는 이유
//DB와 View 사이의 역할 분리를 위해서이며 DTO는 순수하게 데이터를 담고 있다는 점이 Entity와 유사하지만,
// 목적 자체가 전달이므로 읽고, 쓰는 것이 모두 가능함
//JPA 사용시 Entity 객체는 단순히 데이터를 담는 객체 이상으로 DB와 중요한 역할을 하며
//DTO가 일회성으로 데이터를 주고받는 용도로 사용되는 것과 다르게 Entity의 생명주기도 달라 분리하며 사용함

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestbookDTO {
    private Long gno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate, modDate;
}
