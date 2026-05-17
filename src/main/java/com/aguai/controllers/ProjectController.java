package com.aguai.controllers;

import com.aguai.agents.ArchitectAgent;
import com.aguai.agents.CoderAgent;
import com.aguai.agents.TrackerAgent;
import com.aguai.models.Project;
import com.aguai.models.Task;
import com.aguai.repositories.ProjectRepository;
import com.aguai.utils.FileSystemHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/automation")
@CrossOrigin(origins = "*") // Taaki Electron UI bina kisi CORS issue ke baat kar sake
public class ProjectController {

    @Autowired
    private ArchitectAgent architectAgent;

    @Autowired
    private TrackerAgent trackerAgent;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FileSystemHandler fileHandler;

    @Autowired
    private ApplicationContext context;

    @PostMapping("/build")
    public ResponseEntity<String> startGenerationPool(@RequestBody Map<String, String> requestBody) {
        String projectName = requestBody.getOrDefault("projectName", "The AguAI Generated Project");
        String brief = requestBody.get("brief");

        if (brief == null || brief.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Project brief cannot be empty!");
        }

        // Background thread mein automation chalu karenge taaki UI freeze na ho
        new Thread(() -> {
            try {
                // 1. Project register karo
                Project myProject = new Project();
                myProject.setProjectName(projectName);
                myProject.setBriefDescription(brief);
                myProject.setStatus("IN_PROGRESS");
                myProject.setCreatedAt(LocalDateTime.now());
                myProject = projectRepository.save(myProject);
                Long pId = myProject.getProjectId();

                // 2. Plan layout uthao
                String planJson = architectAgent.generateProjectPlan(brief);

                ObjectMapper mapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, String> fileMap = mapper.readValue(planJson, Map.class);

                int counter = 1;
                for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                    String fileName = entry.getKey();
                    String fileDescription = entry.getValue();
                    String coderId = "AI-Coder-" + counter++;

                    // Log task
                    Task currentTask = trackerAgent.logNewTask(pId, fileName, coderId);

                    // Call Coder Agent instance
                    CoderAgent assignedAgent = context.getBean(CoderAgent.class);
                    assignedAgent.setAgentId(coderId);

                    // Execute task
                    String codeOutput = assignedAgent.executeCodingTask(fileName, fileDescription);

                    // Update DB and write to disk
                    trackerAgent.updateTaskToDone(currentTask, codeOutput);
                    fileHandler.writeCodeToFile(fileName, codeOutput);

                    // API rate limit sleep cooling delay
                    Thread.sleep(7000);
                }

                myProject.setStatus("COMPLETED");
                projectRepository.save(myProject);
                System.out.println("[System]: Project generation fully completed successfully!");

            } catch (Exception e) {
                System.err.println("Error during background automation pool: " + e.getMessage());
            }
        }).start();

        return ResponseEntity.ok("Automation workforce triggered successfully in background!");
    }
}