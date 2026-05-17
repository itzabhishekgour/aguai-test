package com.aguai.agents;

import com.aguai.services.LlmCommunicationService;
import com.aguai.services.PromptBuilderService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") // Isse hum loops mein iske multiple independent instances bana payenge
public class CoderAgent {

    @Autowired
    private LlmCommunicationService llmService;

    @Autowired
    private PromptBuilderService promptBuilderService;

    @Setter
    private String agentId;

    public CoderAgent() {
        // Default constructor for Spring
    }

    /**
     * File ka naam aur uski details lekar yeh dedicated coder AI code generate karega.
     */
    public String executeCodingTask(String fileName, String fileDescription) {
        System.out.println("[" + agentId + "]: Writing production code for file -> " + fileName + " (" + fileDescription + ")");

        // Instructor service se dynamic custom prompt uthayenge
        String coderPrompt = promptBuilderService.buildCoderPrompt(fileName, fileDescription);

        // Gemini API ko hit karenge wrap hoke
        return llmService.getAiResponse(coderPrompt);
    }
}