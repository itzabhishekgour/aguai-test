package com.aguai.services;

import org.springframework.stereotype.Service;

@Service
public class PromptBuilderService {

    /**
     * ArchitectAgent (First AI) ke liye prompt taiyar karega
     */
    public String buildArchitectPrompt(String userBrief) {
        return "You are the Lead System Architect AI of 'The AguAI' enterprise system. " +
                "Analyze the following user requirement brief and output a comprehensive project file structure. " +
                "Your output MUST be strictly in raw JSON format mapping file paths to their short descriptions. " +
                "Do not write conversational text.\n" +
                "User Brief: " + userBrief;
    }

    /**
     * CoderAgent (100 AI Fauj) ke liye custom instruction banayega
     */
    public String buildCoderPrompt(String fileName, String taskDescription) {
        return "You are a specialized Coder Agent in 'The AguAI' development pool. " +
                "Your single goal is to write full production-ready code for the file: " + fileName + ". " +
                "Task Context: " + taskDescription + ". " +
                "Output ONLY the clean code inside markdown code blocks. No explanations.";
    }
}