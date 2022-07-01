package com.school.kiqa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WelcomeController {

    @GetMapping("/")
    public ResponseEntity<String> welcomeToKiqaApi() {
        return ResponseEntity.ok("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>FE 4 BE</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            font-family: Courier new, monospace;\n" +
                "            flex-direction: column;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            text-align: center\n" +
                "        }\n" +
                "\n" +
                "        .photos {\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "        }\n" +
                "\n" +
                "        .identify {\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            flex-direction: column;\n" +
                "            margin: 5px;\n" +
                "        }\n" +
                "\n" +
                "        .names {\n" +
                "            color: darkblue;\n" +
                "        }\n" +
                "\n" +
                "        img {\n" +
                "            max-height: 300px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>\n" +
                "    <h1>Welcome to Kiqa</h1>\n" +
                "    <p>Welcome to kiqa api. This is a makeup e-commerce application.</p>\n" +
                "    <p>As such, users are able to create an account, get their information and change it.</p>\n" +
                "    <p>You can create your account through <b>POST /users</b></p>\n" +
                "    <p>Besides that users can also check out our products.</p>\n" +
                "    <p>You can check the products at <b>GET /products</b></p>\n" +
                "    <p>Check everything else you can do at <b>GET /swagger-ui/index.html#/</b></p>\n" +
                "</div>\n" +
                "<h3>Our beloved backenders</h3>\n" +
                "<div class=\"photos\">\n" +
                "    <div class=\"identify\">\n" +
                "        <img src=\"https://media.discordapp.net/attachments/890702053234589696/992221763557666836/working.jpeg?width=612&height=967\"\n" +
                "             alt=\"Flepa\">\n" +
                "        <p class=\"names\">Flepa</p>\n" +
                "    </div>\n" +
                "    <div class=\"identify\">\n" +
                "        <img src=\"https://media.discordapp.net/attachments/890702053234589696/992217730457546862/PHOTO-2022-07-01-00-58-18.jpg\"\n" +
                "             alt=\"Neto\">\n" +
                "        <p class=\"names\">Neto</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
