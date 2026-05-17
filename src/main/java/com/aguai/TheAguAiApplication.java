package com.aguai;

import com.aguai.agents.ArchitectAgent;
import com.aguai.agents.CoderAgent;
import com.aguai.agents.TrackerAgent;
import com.aguai.models.Project;
import com.aguai.models.Task;
import com.aguai.repositories.ProjectRepository;
import com.aguai.utils.FileSystemHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootApplication
public class TheAguAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheAguAiApplication.class, args);
    }

    @Bean
    public CommandLineRunner runTheAguaiAutomationPool(
            ArchitectAgent architectAgent,
            TrackerAgent trackerAgent,
            ProjectRepository projectRepository,
            FileSystemHandler fileHandler,
            ApplicationContext context) {
        return args -> {
            System.out.println("\n=== EXECUTION PHASE: THE AGUAI MULTI-AGENT WORKFORCE IS STANDING BY ===");

            String brief = "I want a single page of documention with tailwind inside dont make entire app only make single react file of documentation inside content is why we use AguAi";

            // 1. Project register karne ke liye direct setters use karenge (100% Safe from Null Error)
            Project myProject = new Project();
            myProject.setProjectName("The AguAI Docx");
            myProject.setBriefDescription(brief);
            myProject.setStatus("IN_PROGRESS");
            myProject.setCreatedAt(LocalDateTime.now());

            // Central DB mein save karo aur generate hui unique ID nikaalo
            myProject = projectRepository.save(myProject);
            Long pId = myProject.getProjectId();

            System.out.println("[Database 🗄️]: Project registered successfully with ID: " + pId);

            // 2. Architect Agent se complete JSON map layout uthao
            String planJson = architectAgent.generateProjectPlan(brief);

            // Jackson library se string ko map format mein convert karo
            ObjectMapper mapper = new ObjectMapper();
            try {
                @SuppressWarnings("unchecked")
                Map<String, String> fileMap = mapper.readValue(planJson, Map.class);

                int counter = 1;
                // 3. For-Loop lagakar 100 AI Workforce trigger karo
                for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                    String fileName = entry.getKey();
                    String fileDescription = entry.getValue();
                    String coderId = "AI-Coder-" + counter++;

                    // Supervisor pipeline update (assigned_tasks table entry)
                    Task currentTask = trackerAgent.logNewTask(pId, fileName, coderId);

                    // Prototype bean call naya dedicated instances lane ke liye
                    CoderAgent assignedAgent = context.getBean(CoderAgent.class);
                    assignedAgent.setAgentId(coderId);

                    // AI Coder writing production code
                    String codeOutput = assignedAgent.executeCodingTask(fileName, fileDescription);

                    // Task state database mein update aur code backup
                    trackerAgent.updateTaskToDone(currentTask, codeOutput);

                    // Utility handler writing physical file to disk
                    fileHandler.writeCodeToFile(fileName, codeOutput);

                    System.out.println("-------------------------------------------------------");

                    // Safe test limits for API quotas: Pehle 2 files test hone ke baad loop break hoga
//                    if (counter > 3) {
//                        System.out.println("\n[System Check]: Successfully tested first few files pipeline. API loop complete.");
//                        break;
//                    }
                    // 🔥 Google API free tier ko thanda rakhne ke liye har file ke baad 5-10 seconds ka delay
                    try {
                        System.out.println("[System Cooldown ☕]: Waiting for 6 seconds to respect API rate limits...");
                        Thread.sleep(6000); // 6000 milliseconds = 6 seconds
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                System.err.println("JSON Parsing block parsing error fallback: " + e.getMessage());
            }

            System.out.println("\n=======================================================");
            System.out.println("   THE AGUAI HAS BUILT THE PROJECT FILES ON DISK! 🎉    ");
            System.out.println("=======================================================");
        };
    }
}