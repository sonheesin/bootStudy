package org.zerock.board.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //컨트롤러 역할 지정
@RequestMapping("/guestbook") //http://localhost/guestbook/???
@Log4j2
public class GuestbookController {
    @GetMapping({"/","/list"}) //2개의 경로가 생김
    public String list(){
        log.info("................");
        return "/guestbook/list"; //list.html로 감

    }
}
