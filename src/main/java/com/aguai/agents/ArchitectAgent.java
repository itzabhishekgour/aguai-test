package com.aguai.agents;

import com.aguai.services.LlmCommunicationService;
import com.aguai.services.PromptBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArchitectAgent {

    @Autowired
    private LlmCommunicationService llmService;

    @Autowired
    private PromptBuilderService promptBuilderService;

    /**
     * User ka idea lekar yeh Gemini API ko bhejega
     * aur return mein ek complete JSON project roadmap lekar aayega.
     */
    public String generateProjectPlan(String userBrief) {
        System.out.println("[ArchitectAgent]: Analyzing user requirements and drafting the master plan...");

        // Dynamic prompt taiyar karenge service ka use karke
        String dynamicPrompt = promptBuilderService.buildArchitectPrompt(userBrief);

        // LlmCommunicationService ke through Gemini ko call karenge
        return llmService.getAiResponse(dynamicPrompt);
    }
}