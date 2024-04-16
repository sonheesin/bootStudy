package org.zerock.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.GuestbookService;

@Controller //컨트롤러 역할 지정
//Client(View)에서 Request Body에 담긴 데이터(DTO)를 Service에 넘겨주고,
//Service에서 처리하고 Response Body에 담겨 반환된 데이터(DTO)를 돌려주는 역할을 한다.

@RequestMapping("/guestbook") //http://localhost/guestbook/???
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class GuestbookController {

    private final GuestbookService service; //final 로 선언


//    @GetMapping({"/","/list"}) //2개의 경로가 생김
//    public String list(){
//        log.info("................");
//        return "/guestbook/list"; //list.html로 감
//
//    }
//    @GetMapping({"/","/list"})

    @GetMapping("/")
    public String index() {
        log.info("................");
        return "redirect:/guestbook/list"; //list.html로 감
    }



    @GetMapping("/list")
    public  void list(PageRequestDTO pageRequestDTO , Model model){
        log.info("리스트---------------------------" + pageRequestDTO);
        model.addAttribute("result",service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){
        log.info("등록 작업---get---------");

    }
    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto  -----" + dto);

        //새로 추가된 엔티티의 번호
        Long gno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg" ,gno);
        //msg 변수처리 모달창 보여주는 용도 addFlashAttribute  한번만 데이터 전달
        return "redirect:/guestbook/list";

    }
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno" + gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);

        //get 방식으로 gno 값을 받아서 Model에 GuestbookDTO 객체를 담아서 전달함
        //페이지로 돌아가는 데이터를 저장하기 위해 PageRequestDTO 파라미터로 같이 사용
        //ModelAttribute없이도 처리가능하지만  requestDTO 로 처리
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto,@ModelAttribute("requestDTO")
                         PageRequestDTO requestDTO,RedirectAttributes redirectAttributes){
        log.info("받은 수정------------------------------------------------");
        log.info("dto " + dto);
        service.modify(dto);
        //
        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno",dto.getGno());

        return "redirect:/guestbook/read";
    }

    @PostMapping("/remove")
    public String remove(long gno,RedirectAttributes redirectAttributes){
        log.info("gno" + gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg",gno);
        //한번 사용할 변수에 찾은 값을 넣음
        return "redirect:/guestbook/list";
    }
}
