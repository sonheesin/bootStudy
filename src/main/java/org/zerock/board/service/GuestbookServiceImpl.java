package org.zerock.board.service;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Guestbook;
import org.zerock.board.entity.QGuestbook;
import org.zerock.board.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입


public class GuestbookServiceImpl implements GuestbookService {


    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto){

        log.info("dto");
        log.info(dto);
        Guestbook entity =doToEntity(dto);
        log.info(entity);

        repository.save(entity);


        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO,Guestbook> getList(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder =getSearch(requestDTO);// 검색조건처리


        Page<Guestbook>result = repository.findAll(booleanBuilder,pageable);
        //Querydsl  사용

        Function<Guestbook,GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result,fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        // Optional 복잡한 조건문 없이도 널(null) 값으로 인해 발생하는 예외를 처리할 수 있게 됩니다.
        return result.isPresent()? entityToDto(result.get()): null;

        //GuestbookServiceImpl 내부 정의된 read() 기능 구현
        //repository 에서  findById(gno)를 통해 엔티티 객체를 가져오면
        //entityToDo() 이용해서 엔티티 객체를 dto 변환해서 반환
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        //업데이트 하는 항목은 '제목','내용'
        Optional<Guestbook>result = repository.findById(dto.getGno());
        if(result.isPresent()){
            Guestbook entity =result.get();
            entity.changerTitle(dto.getTitle());
            entity.changerContent(dto.getContent());


            repository.save(entity);
            // repository 에 수정한 값을 저장
        }

    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        //Querydsl 처리
        //쿼리를 타입에 안전하게 생성 및 관리해주는 프레임워크

        //BooleanBuilder  동적으로 SQL 쿼리의 조건을 구성
        //주요 메서드로는 and(), or(), not() 등이 있습니다.
        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        //요청처리 dto type을 가져와서 type 변수에 넣음
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword= requestDTO.getKeyword();
        //요청처리 dto keyword를 가져와서 keyword 변수에 넣음
        BooleanExpression expression = qGuestbook.gno.gt(0L);
        //gno > 조건만 생성
        booleanBuilder.and(expression);
        if(type == null || type.trim().length() == 0){
    //검색조건이 없는 경우
            return booleanBuilder;
        }
        //검색조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));

        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));

        }
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }
}
