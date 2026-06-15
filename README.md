# The AguAI — Multi-Agent AI Development Workforce

> **"Give it a requirement, watch an AI army build the entire project for you."**

The AguAI is a self-orchestrating multi-agent AI system built on **Spring Boot 3.x** and **Java 21**. Instead of writing code yourself, you describe what you want to build — and a team of specialized AI agents takes over the entire planning and coding pipeline automatically, generating a complete project directory structure from scratch.

---

## How It Works — The Full Pipeline

The system is designed around three specialized AI agent roles, each with a defined job and responsibility, all orchestrated by the Spring application context:

```
User Brief (Text Input)
        │
        ▼
┌─────────────────────┐
│   Architect Agent   │  → Sends the brief to Gemini API
│  (Master Planner)   │  → Returns a JSON file structure map
└─────────────────────┘
        │
        ▼
┌─────────────────────┐
│   Tracker Agent     │  → Logs every file-to-agent assignment
│   (Supervisor DB)   │  → Saves task status in PostgreSQL
└─────────────────────┘
        │
        ▼  (For each file in the plan)
┌──────────────────────────┐
│  CoderAgent × N          │  → Each gets a dedicated Gemini prompt
│  (Prototype AI Workers)  │  → Writes full production-ready code
└──────────────────────────┘
        │
        ▼
┌─────────────────────┐
│  FileSystemHandler  │  → Strips markdown wrappers from AI output
│  (Physical Writer)  │  → Writes clean code files to disk
└─────────────────────┘
        │
        ▼
  generate_project/   ← Your entire project is ready here!
```

---

## The Agents — Roles & Responsibilities

### 1. `ArchitectAgent` — The Master Planner
The Architect is the first AI in the chain. It takes your raw idea (like *"build a React documentation page"*) and sends it to Gemini with a precise **system architect prompt**. It instructs the LLM to return a strict JSON map — file paths mapped to their responsibilities:

```json
{
  "src/App.jsx": "Main React component with documentation content",
  "src/index.css": "Tailwind-powered global styles",
  "index.html": "Root HTML entry point for the SPA"
}
```

No casual explanations — just machine-readable structure.

---

### 2. `TrackerAgent` — The Supervisor
Before any coder agent begins work, the Tracker logs the assignment into the central **PostgreSQL database**. Every file, its assigned AI worker ID, and its live status (`ASSIGNED` → `DONE`) are tracked persistently. If something fails, the database tells you exactly where it broke.

---

### 3. `CoderAgent` — The AI Coding Workers (×N)
The real workforce. For every file in the architect's plan, a new **prototype-scoped** Spring Bean `CoderAgent` is instantiated. Each gets:
- A unique worker ID (`AI-Coder-1`, `AI-Coder-2`, ...)
- A targeted prompt for their specific file
- Access to Gemini API via `LlmCommunicationService`

Each agent writes **only their assigned file** and returns production-ready code. No fluff, no explanations — pure output.

---

### 4. `FileSystemHandler` — The Builder
Once the AI's code comes back, it isn't blindly dumped to disk. The handler first **strips all markdown code block wrappers** (` ```javascript ` etc.) that Gemini might add, then uses Java's native `FileWriter` to write clean, executable code to the output directory — with automatic subdirectory creation via `mkdirs()`.

---

## Key Technical Design Choices

| Feature | Implementation |
|---|---|
| **Multi-Agent Concurrency** | Spring `@Scope("prototype")` ensures each coder gets its own independent bean instance |
| **Background Execution** | REST endpoint triggers generation in a new `Thread` — API responds instantly without blocking |
| **Dynamic Prompt Engineering** | `PromptBuilderService` generates specialized prompts per agent role |
| **AI Cooldown** | 6-7 second `Thread.sleep()` between API calls to respect Gemini free-tier rate limits |
| **Code Cleaning** | Regex strips markdown wrappers from AI output before writing to disk |
| **Task Persistence** | All project and task data tracked in PostgreSQL with Spring Data JPA |
| **REST API Ready** | Full `POST /api/automation/build` endpoint for integration with any UI (including Electron desktop apps) |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 3.x |
| AI SDK | LangChain4j 0.36.0 |
| LLM Backend | Google Gemini (`gemini-3.1-flash-lite`) |
| Persistence | Spring Data JPA + PostgreSQL |
| Utilities | Lombok, Jackson ObjectMapper |
| Build Tool | Gradle |

---

## Local Setup

### Prerequisites
- Java 21+
- PostgreSQL (running locally or cloud-hosted)
- Google Gemini API key

### Configuration
Create `src/main/resources/application.properties` and fill in:

```properties
spring.datasource.url=jdbc:postgresql://your-host/your-db
spring.datasource.username=your_username
spring.datasource.password=your_password

gemini.api.key=YOUR_GEMINI_API_KEY
```

### Run

```bash
./gradlew bootRun
```

On startup, the automation pool fires automatically via `CommandLineRunner`. The generated project will appear in the `generate_project/` directory.

To trigger it via REST API instead:

```bash
curl -X POST http://localhost:8080/api/automation/build \
  -H "Content-Type: application/json" \
  -d '{"projectName": "My App", "brief": "Build a responsive React landing page with Tailwind CSS"}'
```

---

## What Gets Generated?

After the pipeline completes, the `generate_project/` directory on disk contains a fully structured, multi-file project — complete with all source files written end-to-end by AI agents, with no human coding required beyond the initial requirement description.

---

## Roadmap

- [ ] Electron-based desktop UI for submitting briefs and monitoring agent progress in real-time
- [ ] Parallel agent execution using Java Virtual Threads (Project Loom)
- [ ] Streaming progress via WebSocket — see each file being built live
- [ ] Agent retry logic for failed API calls

---

## Creator

Developed by **Abhishek Gour**

> *"Why write code when you can build an AI workforce that writes it for you?"*

---

## License

**Proprietary — All Rights Reserved.**

This software is the exclusive intellectual property of **Abhishek Gour**.
No part of this codebase may be copied, modified, distributed, or used without explicit written permission from the Author.

For licensing inquiries: [mrasgour1004@gmail.com](mailto:mrasgour1004@gmail.com)

Refer to [LICENSE](./LICENSE) for full terms and conditions.

