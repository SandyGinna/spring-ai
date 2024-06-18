package com.sandy.ai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PromptController {

    @Autowired
    ChatModel client;
    @GetMapping("/prompt")
    public String prompt(@RequestParam String message){
        return client.call(message);
    }

    @GetMapping("/")
    public String findPopularSportsPerson(@RequestParam String sports) {
        String message = """
                List of 5 most popular personalities in {sports} along with 
                their career achievements.
                 Show the details in proper readable format""";

        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt = template.create(Map.of("sports",sports));
        return client.call(prompt).getResult().getOutput().getContent();
    }

    @GetMapping("/persons")
    public String findPopularSportsPersonOnly(@RequestParam String sports) {

        SystemMessage systemMessage = new SystemMessage("""
your primary function is to share the information about sports personalities.
If someone ask anything else, you can say you only share about sports category.
""");

        UserMessage userMessage = new UserMessage(String.format("List of 5 most popular personalities in %s along with \n" +
                "                their career achievements.\n" +
                "                 Show the details in proper readable format",sports));
//        PromptTemplate template = new PromptTemplate(message);

        Prompt prompt =new Prompt(List.of(userMessage,systemMessage));
        return client.call(prompt).getResult().getOutput().getContent();
    }
}
