package org.zerock.board.repository;
//DAO(Repository) 실제로 DB에 접근하여 데이터를 CRUD 하는 객체이다
//Service와 DB를 연결해주는 역할을 하고 있으며,
// 인터페이스와 이에 대한 구현체를 만들어서 구현체에 CRUD관련 기능을 구현하고,
// 이를 DI(Dependency Injection) 해주는 방식으로 사용된다.
//DAO(Data Access Object) 패턴은 Data Persistence의 추상화이며, 영구 데이터 저장소에 가까운 것으로 추정
//Repository 패턴은 객체의 상태를 관리하는 저장소로 추정되며, 영구 저장소가 아닌 객체의 상태를 관리하는 저장소
//DAO는 Repository를 사용하여 구현할 수 없지만, Repository는 DAO를 사용해 구현할 수 있음


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
//    @Query("select m, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
//            "left outer join Review  r on r.movie = m group by m")
//
//    Page<Object[]> getListPage(Pageable pageable);


    @Query("select m, mi, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review  r on r.movie = m group by m ")
    Page<Object[]> getListPage(Pageable pageable); //페이징 처리


    @Query("select m, mi ,avg(coalesce(r.grade,0)),  count(r)" +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review  r on r.movie = m "+
            " where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(Long mno); //특정영화조회
}


