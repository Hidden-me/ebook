package org.ebook.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/library")
public class LibraryController {
    @GetMapping
    public ModelAndView getLibraryView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public String getBookList(){
        String result = "{\"library\": [\n" +
                "                                        {\n" +
                "                                            \"category\": \"文学作品\",\n" +
                "                                            \"books\": [\n" +
                "                                                {\"title\": \"Macbeth\", \"author\": \"W. Shakespeare\", \"image\": \"@/assets/logo.png\", \"isbn\": \"1\", \"stock\": \"32\", \"price\": \"28\"},\n" +
                "                                                {\"title\": \"泰戈尔诗选\", \"author\": \"泰戈尔\", \"image\": \"@/assets/logo.png\", \"isbn\": \"2\", \"stock\": \"30\", \"price\": \"20\"},\n" +
                "                                                {\"title\": \"Hamlet\", \"author\": \"W. Shakespeare\", \"image\": \"@/assets/logo.png\", \"isbn\": \"3\", \"stock\": \"35\", \"price\": \"25\"},\n" +
                "                                                {\"title\": \"狂人日记\", \"author\": \"鲁迅\", \"image\": \"@/assets/logo.png\", \"isbn\": \"4\", \"stock\": \"0\", \"price\": \"15\"}\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"category\": \"科技创新\",\n" +
                "                                            \"books\": [\n" +
                "                                                {\"title\": \"Nature 2017\", \"author\": \"Nature\", \"image\": \"@/assets/logo.png\", \"isbn\": \"11\", \"stock\": \"30\", \"price\": \"5\"},\n" +
                "                                                {\"title\": \"JAMA 2017\", \"author\": \"JAMA\", \"image\": \"@/assets/logo.png\", \"isbn\": \"12\", \"stock\": \"30\", \"price\": \"5\"}\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"category\": \"教科教辅\",\n" +
                "                                            \"books\": [\n" +
                "                                                {\"title\": \"理论力学\", \"author\": \"\", \"image\": \"@/assets/logo.png\", \"isbn\": \"21\", \"stock\": \"0\", \"price\": \"35\"},\n" +
                "                                                {\"title\": \"深入了解计算机系统\", \"author\": \"Randal E. Bryant, David R. O'Hallaron\", \"image\": \"@/assets/logo.png\", \"isbn\": \"22\", \"stock\": \"10\", \"price\": \"100\"},\n" +
                "                                                {\"title\": \"C Primer Plus\", \"author\": \"Stephen Prata\", \"image\": \"@/assets/logo.png\", \"isbn\": \"23\", \"stock\": \"5\", \"price\": \"90\"},\n" +
                "                                                {\"title\": \"电路基础\", \"author\": \"\", \"image\": \"@/assets/logo.png\", \"isbn\": \"24\", \"stock\": \"30\", \"price\": \"65\"}\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"category\": \"艺术欣赏\",\n" +
                "                                            \"books\": [\n" +
                "                                                {\"title\": \"世界名画家全集\", \"author\": \"何政广\", \"image\": \"@/assets/logo.png\", \"isbn\": \"31\", \"stock\": \"10\", \"price\": \"50\"},\n" +
                "                                                {\"title\": \"交响音乐通俗讲座\", \"author\": \"薛金炎 李近朱\", \"image\": \"@/assets/logo.png\", \"isbn\": \"32\", \"stock\": \"15\", \"price\": \"20\"}\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"category\": \"时尚期刊\",\n" +
                "                                            \"books\": []\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"category\": \"其他读物\",\n" +
                "                                            \"books\": [\n" +
                "                                                {\"title\": \"The Times\", \"author\": \"\", \"image\": \"@/assets/logo.png\", \"isbn\": \"101\", \"stock\": \"0\", \"price\": \"15\"},\n" +
                "                                                {\"title\": \"The Economist\", \"author\": \"\", \"image\": \"@/assets/logo.png\", \"isbn\": \"102\", \"stock\": \"10\", \"price\": \"16\"}\n" +
                "                                            ]\n" +
                "                                        }\n" +
                "                                    ]}";
        return result;
    }
}
