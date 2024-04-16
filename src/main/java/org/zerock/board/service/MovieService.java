package org.zerock.board.service;

import org.zerock.board.dto.MovieDTO;
import org.zerock.board.dto.MovieImageDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Movie;
import org.zerock.board.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {
    Long register(MovieDTO movieDTO);
    MovieDTO getMovie(Long mno);

    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);

    //목록 처리
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) { //map 타입으로 반환
        Map<String, Object> entityMap = new HashMap<>();
        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();
        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        if (imageDTOList != null && imageDTOList.size() > 0) {
            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO ->
            {
                MovieImage movieImage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie).build();
                return movieImage;
            }).collect(Collectors.toList());  //객체를 새로운 리스트로 만드는 방법
            entityMap.put("imgList", movieImageList);
        }

        return entityMap;
    }
    default MovieDTO entitiesToDTO(Movie moive,List<MovieImage> movieImages,Double avg,Long reviewCnt){
        MovieDTO movieDTO =MovieDTO.builder()
                .mno(moive.getMno())
                .title(moive.getTitle())
                .regDate(moive.getRegDate())
                .modDate(moive.getModDate())
                .build();

        List<MovieImageDTO>movieImageDTOList = movieImages.stream().map(movieImage -> {
            return MovieImageDTO.builder().imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .uuid(movieImage.getUuid())
                    .build();

        }).collect(Collectors.toList());
        movieDTO.setImageDTOList(movieImageDTOList);
        movieDTO.setAvg(avg);
        movieDTO.setReviewCnt(reviewCnt.intValue());
        return movieDTO;
    }






}
