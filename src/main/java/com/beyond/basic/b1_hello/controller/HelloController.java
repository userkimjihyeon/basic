package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// RestController는 Requestbody+controller 임
// Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http request를 분석하고, http response를 생성
@Controller
// 클래스 차원에 url 매핑시에는 RequestMapping을 사용
@RequestMapping("/hello")
public class HelloController {

    // get요청의 case들

    // case 1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody 있을 때
    @GetMapping("") // 아래 메서드에 대한 서버의 앤드포인트를 설정
    // ResponseBody가 없고, return 타입이 String인 경우 서버는 templates폴더 밑에 helloworld.html을 찾아서 리턴 (화면을 찾음)
    @ResponseBody
    public String helloWorld(){
        return "helloworld"; // 자동으로 http문서로 반환. body로 감
        // 대신 thymeleaf 이거 gradle에 추가.
    }

    // case2. 서버가 사용자에게 String(json형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody // 데이터를 주겠다는 뜻. JSON!!
    //public String helloJson() throws JsonProcessingException {

    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
        // 직접 json으로 직렬화할 필요 없이, return타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
//        return result;
        return h1;
    } // 객체를 return해주면 json이 나간다. 앞으로 ObjectMapper 쓸 일 없다.


    // case3. parameter 방식을 통해 사용자로부터 값을 수신
    // parameter의 형식 : /member?id=1 또는 /member?name=hongildong 등
    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name")String inputName){
        Hello h1 = new Hello(inputName, "abc@naver.com");
        return h1;
//        System.out.println(inputName);
//
//        // {name:사용자가 넣어온이름, email:아무거나}
//        return null;
    }

    // case4. pathVariable 방식을 통해 사용자로부터 값을 수신
    // paravariable의 형식 : /member/1
    // pathVariable방식은 url을 통해 자원의 구조를 명확하게 표현할때 사용(좀더 restful함)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable String inputId){
        // 별도의 형변환 없이도, 매개변수에 타입지정시 자동형변환 시켜줌
        long id = Long.parseLong(inputId);
        System.out.println(id);
        return "OK";

    }
    // case5. parameter 2개 이상 형식
    // /hello/param2?name=hong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value = "name")String inputName,
                         @RequestParam(value = "email")String inputEmail){
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "ok";
    }

    // case6. parameter가 많아질 경우, 데이터바인딩을 통해 input값 처리
    // 데이터바인딩 : param을 사용하여 객체로 생성
    // ?name=hong&email=hong@naver.com
    // http://localhost:8080/hello/param3?name=hong&email=hong@naver.com
    @GetMapping("/param3")
    @ResponseBody
    //public String param3(Hello hello){ // 객체
    // @ModelAttribute를 써도되고 안써도 되는데, 이 키워드를 써서 명시적으로 param형식의 데이터를 받겠다라는 것을 표현
    public String param3(@ModelAttribute Hello hello){
        System.out.println(hello);
        return "ok";
    }

    // case7. 서버에서 화면을 return, 사용자로부터 넘어오는 input을 활용하여 동적인 화면 생성.
    // 서버에서 화면(+데이터)을 렌더링해주는 ssr방식(csr은 서버는 데이터만)
    @GetMapping("/model-param")
    public String modelParam(@RequestParam(value = "id")Long inputId, Model model){
        // model 객체는 데이터를 화면에 전달해주는 역할
        // name이라는 키에 hongildong이라는 value를 key:value 형태로 화면에 전달
        // MVC(model, view, controller)패턴이라고도 함.
        if(inputId==1){
            model.addAttribute("name", "hongildong");
            model.addAttribute("email", "hong@naver.com");
        }else if(inputId==2){
            model.addAttribute("name", "hongildong2");
            model.addAttribute("email", "hong2@naver.com");
        }
        return "helloworld2";
    }



    // post 요청의 case 2가지 : url인코딩방식 또는 multipart-formdata, json
    // case1. text만 있는 form-data 형식
    // 형식 : body부에 name=xxx&email=xxx  (파라미터방식)
    @GetMapping("/form-view")
    public String fromView(){
        return "form-view";
    }

    @PostMapping("/form-view") // GET과는 별개여서 url 같아도 됨
    @ResponseBody
    // get요청에 url에 파라미터방식과 동일한 데이터 형식이므로(body에 들어오긴 하지만), RequestParam 또는 데이터바인딩(객체) 방식 가능
    public String formViewPost(@ModelAttribute Hello hello){ // 파라미터 형식.  지금은 객체
        System.out.println(hello);
        return "ok";
    }

    // case2-1. text와 file이 있는 form-data 형식(순수html로 제출)
    @GetMapping("/form-file-view")
    public String fromFileView(){
        return "form-file-view";
    }

    @PostMapping("/form-file-view") // GET과는 별개여서 url 같아도 됨
    @ResponseBody
    public String formFileViewPost(@ModelAttribute Hello hello,
                                   @RequestParam(value = "photo")MultipartFile photo){ // photo를 Hello안에 선언해도 됨
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename()); // 이건 intellij 콘솔창에 보이는것
        return "ok";
    }

    // case2-2. text와 file이 있는 form-data 형식(js로 제출)
    @GetMapping("/axios-file-view")
    public String axiosFileView(){
        return "form-file-view";
    }


    // case3. text와 멀티file이 있는 form-data 형식(js로 제출)
    @GetMapping("/axios-multi-file-view")
    public String axiosMultiFileView(){
        return "axios-multi-file-view";
    }

    @PostMapping("/axios-multi-file-view")
    @ResponseBody
    public String axiosMultiFileViewPost(@ModelAttribute Hello hello,
                                   @RequestParam(value = "photos") List<MultipartFile> photos){ // photo를 Hello안에 선언해도 됨
        System.out.println(hello);
        for(int i=0; i<photos.size(); i++){
            System.out.println(photos.get(i).getOriginalFilename());
        }
        return "ok";
    }

    // case4. json데이터 전송
    @GetMapping("/axios-json-view")
    public String axiosJsonView(){
        return "axios-json-view";
    }

    @PostMapping("/axios-json-view")
    @ResponseBody
     // RequestBody : json형식으로 데이터가 들어올 때 객체로 자동파싱
    public String axiosJsonViewPost(@RequestBody Hello hello){
        System.out.println(hello);
        return "ok";
    }


    // case5. 중첩된 json 데이터 처리
    @GetMapping("/axios-nested-json-view")
    public String axiosNestedJsonView(){
        return "axios-nested-json-view";
    }

    @PostMapping("/axios-nested-json-view")
    @ResponseBody
    // RequestBody : json형식으로 데이터가 들어올 때 객체로 자동파싱
    public String axiosNestedJsonViewPost(@RequestBody Student student){
        System.out.println(student);
        return "ok";
    }

    // case6. json(text)+file 같이 처리할 때 : text 구조가 복잡하여 피치못하게 json을 써야하는 경우.
    // file은 json에 넣을 수 없음. multipart form data를 써야 함
    // 데이터형식 : hello={name:"xx", email:"xxx"}&photo=이미지.jpg
    // 결론은 단순json구조가 아닌, multipart-formdata구조안에 json을 넣는 구조.!!

    @GetMapping("/axios-json-file-view")
    public String axiosJsonFileView(){
        return "axios-json-file-view";
    }

    @PostMapping("/axios-json-file-view")
    @ResponseBody
    public String axiosJsonFileViewPost(
            // json과 file을 함께 처리해야 할 때 RequestPart 일반적으로 활용
            @RequestPart("hello") Hello hello,
            @RequestPart("photo") MultipartFile photo
    ){
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "ok";
    }
}
// json만은 Requestbody
// json+file은 case 6
// 데이터형식 : hello={name="xx"&email="xxx"} 이러면 ModelAttribute
