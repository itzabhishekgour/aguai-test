package com.aguai.services;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LlmCommunicationService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String getAiResponse(String prompt) {
        try {
            // Naya stable model name format configuration
            GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                    .apiKey(this.apiKey)
                    .modelName("gemini-3.1-flash-lite") // Versioning error se bachne ke liye
                    .temperature(0.7)
                    .build();

            // 1. String prompt ko UserMessage mein badlo
            UserMessage userMessage = UserMessage.from(prompt);

            // 2. ChatRequest ka object taiyar karo jisme yeh message jayega
            ChatRequest chatRequest = ChatRequest.builder()
                    .messages(userMessage)
                    .build();

            // 3. Ab model.chat() ko chatRequest pass karo, IDE ekdum khush ho jayega!
            ChatResponse chatResponse = model.chat(chatRequest);

            // 4. Response se text nikal kar return karo
            return chatResponse.aiMessage().text();

        } catch (Exception e) {
            return "Error while talking to Gemini: " + e.getMessage();
        }
    }
}