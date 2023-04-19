package com.example.api.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.repository.ChatGptRepository;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

@Service
public class ChatGptService {

    @Autowired
    private ChatGptRepository repository;

    public String checkMethodForrequestMessage(String requestMessage) throws Exception {
        List<ChatMessage> messageList = new ArrayList<>();
        this.generateBaseCheckMethodMessage(messageList);
        messageList.add(new ChatMessage(ChatMessageRole.USER.value(), requestMessage));

        String response = repository.requestChatGpt(messageList);
        if(response.contains("[json]{\"method\":")) {
            response = this.ignoreNotJson(response);
        } else {
            response = this.ignoreJson(response);
        }
        
        return response;
    }

    private void generateBaseCheckMethodMessage(List<ChatMessage> messageList) {
        messageList.add(new ChatMessage(ChatMessageRole.USER.value(), new StringBuilder()
        .append("「先々月」「先月」「先々週」「先週」「一昨日」「機能」「明日」「明後日」「来週」「再来週」「来月」「再来月」は、")
        .append(this.formatDate(new Date()))
        .append("を基準日として、判定してください。")
        .toString()));
        messageList.add(new ChatMessage(ChatMessageRole.USER.value(), new StringBuilder()
        .append("質問の内容に合わせて、以下で示す形式で回答してください。\n")
        .append("予定の追加もしくは登録の場合:")
        .append("[json]{\"method\": 1, \"title\": \"タイトル\", \"startDate\": \"yyyy/mm/dd\", \"endDate\": \"yyyy/mm/dd\"}[json]\n")
        .append("予定を教えてほしい場合:")
        .append("[json]{\"method\": 2 \"startDate\": \"yyyy/mm/dd\", \"endDate\": \"yyyy/mm/dd\"}[json]\n")
        .append("予定の削除の場合：")
        .append("[json]{\"method\": 3, \"title\": \"タイトル\", \"startDate\": \"yyyy/mm/dd\", \"endDate\": \"yyyy/mm/dd\"}[json]\n")
        .toString()));        
    }

    private String formatDate(Date date){
        SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日");
        return formater.format(date);
    }

    private String ignoreNotJson(String target) {
        List<String> strList = Arrays.asList(target.split("\\[json\\]"));
        return strList.stream().filter(str -> str.contains("{\"method\":")).findFirst().orElse(target);
    }

    private String ignoreJson(String target) {
        return target.replaceAll("\n[json]\\{.*method.*\\}[json]\n", "")
            .replaceAll("\n[json]\\{.*method.*\\}[json]", "")
            .replaceAll("[json]\\{.*method.*\\}[json]\n", "")
            .replaceAll("\s+[json]\\{.*method.*\\}[json]\s+", "")
            .replaceAll("\s+[json]\\{.*method.*\\}[json]", "")
            .replaceAll("[json]\\{.*method.*\\}[json]\s+", "")
            .replaceAll("[json]\\{.*method.*\\}[json]", "");
    }
}