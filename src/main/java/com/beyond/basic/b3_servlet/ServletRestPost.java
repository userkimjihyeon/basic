package com.beyond.basic.b3_servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

//서블릿 : 사용자의 req를 쉽게 처리하고, 사용자에게 res를 쉽게 조립해주는 기술 (Controller 이전의 기술)
//서블릿에서는 url매핑을 메서드단위가 아닌, 클래스단위로 지정
@WebServlet("/servlet/post")
public class ServletRestPost extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        url인코딩방식으로 데이터 전송      //?name=foo&email=bar 또는 postman에서 키밸류입력
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        System.out.println(name);
        System.out.println(email);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print("ok");
        printWriter.flush();
    }
}
