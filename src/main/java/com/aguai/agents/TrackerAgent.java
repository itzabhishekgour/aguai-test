package com.aguai.agents;

import com.aguai.models.Task;
import com.aguai.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackerAgent {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Jaise hi koi task assign hoga, Supervisor database table mein entry map kar dega.
     */
    public Task logNewTask(Long projectId, String fileName, String coderId) {
        System.out.println("[TrackerAgent 📋]: Mapping file '" + fileName + "' to workstation " + coderId);
        Task task = new Task(projectId, fileName, coderId, "ASSIGNED");
        return taskRepository.save(task);
    }

    /**
     * Jaise hi Coder AI ka kaam khatam hoga, supervisor status DONE karega aur backup code save karega.
     */
    public void updateTaskToDone(Task task, String generatedCode) {
        task.setFileStatus("DONE");
        task.setGeneratedCode(generatedCode);
        task.setFileStatus("DONE");
        taskRepository.save(task);
        System.out.println("[TrackerAgent 📋]: Task saved successfully. Coder " + task.getAssignedCoderId() + " marked as DONE.");
    }
}