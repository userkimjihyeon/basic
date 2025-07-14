package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//Component어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체가 생성.
//Controller어노테이션을 통해 쉽게 사용자의 http req를 분석하고, http res를 생성.
@Controller
//클래스차원의 url매핑시에는 RequestMapping을 사용 -> 모든 메서드 URL 앞에 /hello가 자동으로 붙음.
@RequestMapping("/hello")
public class HelloController {

//    get요청의 case들
//    case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody있을때
    @GetMapping("") //아래 메서드에 대한 서버의 엔드포인트를 설정
//    @ResponseBody가 없고 return타입이 String인 경우, 서버는 templates폴더 밑에 helloworld.html을 찾아서 리턴
    @ResponseBody
    public String helloWorld() {
        return "helloworld";
    }

//    case2. 서버가 사용자에게 String(json형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
//    리턴타입 -> 객체 Hello
//    public String helloJson() throws JsonProcessingException {
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
//        직접 json으로 직렬화할 필요없이, return타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
        return h1;
    }
//    case3. parameter방식을 통해 사용자로부터 값을 수신
//    parameter의 형식 : /member?name=hongildong
    @GetMapping("/param")
    @ResponseBody
//        url입력값=파라미터값이들어오면  "키"의대응하는값을    이 변수에 담겠다.
    public Hello param(@RequestParam(value = "name")String inputName) {
        System.out.println(inputName);
        Hello h1 = new Hello(inputName, "abc@naver.com");
//        {name:사용자가넣은이름, email:아무거나}
        return h1;
    }
//    case4. pathvariable방식을 통해 사용자로부터 값을 수신
//    pathvariable의 형식 : /member/1
//    pathvariable방식은 url을 통해 자원의 구조를 명확하게 표현할때 사용(좀더 restful함) (->뭔소리?)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable Long inputId) {
//        별도의 형변환없이도, 매개변수에 타입지정시 자동형변환 됨.
//        long id = Long.parseLong(inputId);
//        System.out.println(id);
        System.out.println(inputId);
        return "OK";
    }
//    case5. parameter 2개 이상 형식
//    /hello/param2?name=hong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value="name")String inputName,
                         @RequestParam(value="email")String inputEmail) {
        System.out.println(inputName);  //hong
        System.out.println(inputEmail); //hong@naver.com
        return "OK";
    }
//    case6. parameter가 많아질경우, 데이터바인딩을 통해 input값 처리
//    데이터바인딩 : parameter를 사용하여 객체로 생성해줌.
//    ?name=hongildong&email=hong@naver.com
    @GetMapping("/param3")
    @ResponseBody
//    public String param3(Hello hello) {
//    @ModelAttribute : 명시적으로 param형식의 데이터를 받겠다는 표현(생략가능)
    public String param3(@ModelAttribute Hello hello) {
        System.out.println(hello);  //Hello(name=hong, email=hong@naver.com)
        return "OK";
    }
//    case7. 서버에서 화면을 return, 사용자로부터 넘어오는 input값을 활용하여 동적인 화면생성.
//    ssr방식 : 서버에서 화면(+데이터)을 렌더링해줌 (csr방식 : 서버는 데이터만 전송)
//    mvc(model, view, controller)패턴이라고도 함.
    @GetMapping("/model-param")
    public String modelParam(@RequestParam(value="id") Long inputId,
                             Model model) {
//        model객체는 데이터를 화면에 전달하는 역할
//        name이라는 키에 hongildong이라는 value를 key:value형태로 화면에 전달
        if(inputId == 1) {
//            addAttribute() : 속성을 더하겠다.
            model.addAttribute("name", "hongildong");
            model.addAttribute("email","hong@naver.com");
        } else if (inputId == 2) {
            model.addAttribute("name", "hongildong2");
            model.addAttribute("email","hong2@naver.com");
        }
        return "helloworld2";
//        @Controller라면 → hello.html 찾아감    (화면 이름을 리턴)
//        @RestController라면 → 그냥 "hello" 문자열 그대로 클라이언트에게 보냄
//                             따라서, return new Hello("지현", "jihyeon@example.com"); (데이터(JSON)를 리턴)
    }

//    post요청의 case 2가지 : url인코딩방식 또는 multipart-formdata, json
//    case1. text만 있는 form-data형식
//    형식 : body부에 name=xxx&email=xxx
    @GetMapping("/form-view")
    public String formView() {
        return  "form-view";
    }
    @PostMapping("/form-view")
    @ResponseBody
//    get요청에 url에 parameter방식과 동일한 데이터형식이므로, RequestParam 또는 데이터바인딩 방식 가능
    public String formViewPost(@ModelAttribute Hello hello) {   //폼의 name값들이 Hello 클래스의 필드로 자동 바인딩
        System.out.println(hello);
        return "OK";
    }

//    case2-1(html). text와 file이 있는 form-data형식(순수html로 제출)
    @GetMapping("/form-file-view")
    public String formFileView() {
        return  "form-file-view";
    }
    @PostMapping("/form-file-view")
    @ResponseBody
    public String formFileViewPost(@ModelAttribute Hello hello, //= @RequestParam 2개와 같음.
                                   @RequestParam(value="photo")MultipartFile photo) {
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "OK";
    }

//    case2-2(js). text와 file이 있는 form-data형식(js로 제출)
    @GetMapping("/axios-file-view")
    public String axiosFileView() {
        return  "axios-file-view";
    }
//    PostMapping은 위와 공유

//    case3. text와 멀티file이 있는 form-data형식(js로 제출)
    @GetMapping("/axios-multi-file-view")
    public String axiosMultiFileView() {
        return  "axios-multi-file-view";
    }
    @PostMapping("/axios-multi-file-view")
    @ResponseBody
    public String axiosMultiFileViewPost(@ModelAttribute Hello hello, //= @RequestParam 2개와 같음.
                                         @RequestParam(value="photos") List<MultipartFile> photos) {
        System.out.println(hello);
        for(int i=0; i<photos.size(); i++) {
            System.out.println(photos.get(i).getOriginalFilename());
        }
        return "OK";
    }
//    case4. json데이터 처리⭐중요⭐
    @GetMapping("/axios-json-view")
    public String axiosJsonView() {
        return "axios-json-view";
    }
    @PostMapping("/axios-json-view")
    @ResponseBody
//    @RequestBody : json형식으로 데이터가 들어올때 객체로 자동pasing⭐암기⭐
    public String axiosJsonViewPost(@RequestBody Hello hello) {
        System.out.println(hello);
        return "OK";
    }
//    case5. 중첩된 json데이터 처리
    @GetMapping("/axios-nested-json-view")
    public String axiosNestedJsonView() {
        return "axios-nested-json-view";
    }
    @PostMapping("/axios-nested-json-view")
    @ResponseBody
    public String axiosNestedJsonViewPost(@RequestBody Student student) {
        System.out.println(student); //Student(name=김지현, email=kjh921127@icloud.com, scores=[Student.Score(subject=수학, point=100), Student.Score(subject=사회, point=100)])
        return "OK";
    }

//    case6. json(text)+file 같이 처리할때 : text구조(2-1.formdata)가 복잡하여 피치못하게 json을 사용해야하는 경우.
//    데이터형식 : hello={name:"xx", email:"xxx"}&photo=이미지.jpg
//    결론은 단순json구조가 아닌, multipart-formdata구조안에 json을 넣는 구조.
    @GetMapping("/axios-json-file-view")
    public String axiosJsonFileView() {
        return "axios-json-file-view";
    }
    @PostMapping("/axios-json-file-view")
    @ResponseBody
    public String axiosJsonFileViewPost(
//            json과 file을 함께 처리해야할때 @RequestPart를 일반적으로 활용 (2-1과 비교하기)
            @RequestPart("hello") Hello hello,
            @RequestPart("photo") MultipartFile photo) {    //@RequestParam써도되는데 일관성있게 RequestPart사용
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "OK";
    }

}
