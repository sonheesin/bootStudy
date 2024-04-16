package org.zerock.board.dto;



import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO <DTO,EN>{

    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    //현재 페이지 번호
    private int page;

    //목록 사이즈
    private int size;

    //시작페이지 번호 ,끝 페이지 번호
    private int start ,end;

    //이전 ,다음
    private boolean prev,next;

    //페이지 번호 목록
    private List<Integer> pageList;


    public PageResultDTO(Page<EN> result , Function<EN,DTO>fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }
    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() +1; // 0부터 시작하므로 1을 추가
        //페이지에 넘버를 가져옴
        this.size = pageable.getPageSize();

        //temp end page 페이징의 끝 번호 계산
        int tempEnd = (int)(Math.ceil(page/10.0))*10;


        start = tempEnd - 9;
        prev = start >1;
        end = totalPage >tempEnd ? tempEnd: totalPage;
        next = totalPage >tempEnd;

        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
        // 출력: 시작 과 끝
        //'range' 와 'rangeClosed' 메소드는 주어진 범위 내 순차적인 정수 스트림을 반환


    }
}
