# Secure 1-to-1 Chat Backend

Production-ready, secure chat backend built with Spring Boot 3, Spring Security, WebSocket (STOMP), and PostgreSQL.

## Features
- **End-to-End Security**: Backend handles only encrypted message payloads.
- **JWT Authentication**: Stateless authentication for REST and WebSocket connections.
- **Real-time Messaging**: High-performance STOMP over WebSocket.
- **Media Support**: Encrypted file uploads.
- **Zero Cost Deployment**: Compatible with Railway, Render, and Neon (PostgreSQL).

## Tech Stack
- Java 21
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- WebSocket (STOMP)

## Prerequisites
- Java 21
- Maven
- PostgreSQL (Local or Cloud)

## Configuration
Configure the following environment variables (or set in `application.yml`):

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Server Port | `8080` |
| `DB_URL` | JDBC URL | `jdbc:postgresql://localhost:5432/secure_chat` |
| `DB_USER` | Database User | `postgres` |
| `DB_PASS` | Database Password | `postgres` |
| `JWT_SECRET` | 256-bit+ Secret Key | (Dev Default Provided) |
| `UPLOAD_DIR` | File Storage Path | `uploads` |

## Running Locally
1. **Database**: Ensure PostgreSQL is running and create a database named `secure_chat`.
2. **Build**:
   ```bash
   mvn clean package
   ```
3. **Run**:
   ```bash
   java -jar target/secure-chat-backend-0.0.1-SNAPSHOT.jar
   ```

## API Endpoints
### Authentication
- `POST /api/auth/register`: `{ "username": "user", "password": "users" }`
- `POST /api/auth/login`: `{ "username": "user", "password": "users" }` -> Returns JWT

### Chat
- `POST /api/chats?tempPartnerId={uuid}`: Start chat (Use `tempPartnerId` for now, or implement user search)
- `POST /api/chats/verify-user?username={username}`: Start chat by username
- `GET /api/chats`: List my chats

### Messages
- `GET /api/messages/{chatId}?page=0&size=20`: Get message history
- `POST /api/files/upload`: Upload file (Multipart) -> Returns URL

## WebSocket Integration
1. **Connect**: `ws://localhost:8080/ws`
   - Headers: `Authorization: Bearer <token>`
   - Note: Standard JS WebSocket API doesn't support headers. Use STOMP client or SockJS with query param (requires code update) or pass token in CONNECT frame headers (Preferred).
2. **Subscribe**: `/topic/chat/{chatId}`
3. **Send**: `/app/chat.sendMessage/{chatId}`
   - Payload:
     ```json
     {
       "content": "EncryptedString",
       "senderId": "MyUUID", 
       "messageType": "TEXT" 
     }
     ```

## Deployment (Railway / Render)
1. Fork/Push this repo to GitHub.
2. Link repo to Railway/Render.
3. Add PostgreSQL plugin (or provided URI).
4. Set Environment Variables (`DB_URL`, `JWT_SECRET`, etc.).
5. Deploy!
