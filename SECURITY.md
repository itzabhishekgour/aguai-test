# Security Policy — The AguAI

## Intellectual Property Notice

> **This is proprietary software.** All source code, architectural design, and implementation details
> are the exclusive intellectual property of **Abhishek Gour**.
> Unauthorized access, copying, or use is strictly prohibited. Refer to [LICENSE](./LICENSE) for full terms.

---

## Supported Versions

Only the currently active version of The AguAI receives security attention.

| Version | Status              |
| ------- | ------------------- |
| Latest  | ✅ Active           |
| Older   | ❌ Not Supported    |

---

## What Constitutes a Security Issue?

Report the following types of vulnerabilities:

- **API Key Exposure**: Any code path that may accidentally expose Gemini API keys or database credentials.
- **Prompt Injection**: Malicious inputs via the `/api/automation/build` endpoint that could manipulate the AI agents into performing unintended actions.
- **SQL Injection / JPA Issues**: Any data persistence layer vulnerability that could leak or corrupt project or task data.
- **Unauthorized Access**: Any mechanism that could allow unauthorized parties to trigger the automation pool or access generated project files.
- **Dependency Vulnerabilities**: Critical CVEs in LangChain4j, Spring Boot, or any direct dependency that could compromise runtime security.

---

## Reporting a Vulnerability

**Do NOT open a public GitHub issue for security vulnerabilities.** Public disclosure of security issues before a fix is available puts this system and its data at risk.

### Private Reporting Channel

Contact the Author directly and privately:

| Field   | Details                            |
| ------- | ---------------------------------- |
| Email   | mrasgour1004@gmail.com             |
| Subject | `[AguAI SECURITY] <brief title>`   |

### What to Include in Your Report

Please provide the following in your email:

1. **Vulnerability type** (e.g., Prompt Injection, API key leakage)
2. **Affected component** (e.g., `LlmCommunicationService`, `ProjectController`)
3. **Steps to reproduce** the issue clearly and precisely
4. **Potential impact** — what could an attacker accomplish?
5. **Suggested fix** (optional, but appreciated)

---

## Response Timeline

| Step                   | Target Time |
| ---------------------- | ----------- |
| Acknowledgement        | Within 48 hours |
| Assessment & Triage    | Within 5 business days |
| Resolution / Patch     | Depends on severity |
| Private Disclosure     | After patch is confirmed |

---

## Security Best Practices for Running This Locally

If you have obtained permission from the Author to run this software, follow these mandatory practices:

- **Never commit your `.env` or `application.properties`** — they must stay in `.gitignore` at all times.
- **Rotate your Gemini API key** if it is accidentally exposed in any commit or log.
- **Use environment variables** for all secrets — never hardcode API keys or DB passwords.
- **Restrict database access** — use a dedicated PostgreSQL user with minimal permissions.
- **Do not expose port 8080 publicly** without authentication middleware in front of the REST endpoint.

---

## Legal Notice

Unauthorized access to, or exploitation of, this software and its systems may constitute a violation of applicable cybersecurity and intellectual property laws. All security incidents will be taken seriously and may be escalated to appropriate legal authorities if necessary.

**Author: Abhishek Gour**
