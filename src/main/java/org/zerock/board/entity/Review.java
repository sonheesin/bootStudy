package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie","mmember"})
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ManyToOne(fetch = FetchType.LAZY)  // fk
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)  // fk
    private Mmember mmember;

    private int grade;

    private String text;
    //Hibernate:
    //    create table review (
    //        reviewnum bigint not null auto_increment,
    //        moddate datetime(6),
    //        regdate datetime(6),
    //        grade integer not null,
    //        text varchar(255),
    //        mmember_email varchar(255),
    //        movie_mno bigint,
    //        primary key (reviewnum)
    //    ) engine=InnoDB
    //Hibernate:
    //    alter table if exists review
    //       add constraint FKdtn5itub8h5kf026dao5gs5ih
    //       foreign key (mmember_mid)
    //       references tbl_mmember (mid)
    //Hibernate:
    //    alter table if exists review
    //       add constraint FKdg4bkv5wfpxx015elj4h915gw
    //       foreign key (movie_mno)
    //       references movie (mno)

    // 450 추가
    public void changeGrade(int grade){
        this.grade = grade;
    }

    public void changeText(String text){
        this.text = text;
    }
}