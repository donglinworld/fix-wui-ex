# FIX Web UI Project Summary

## Project Overview
A web-based FIX (Financial Information eXchange) application with Java backend and Vue.js frontend.

## Architecture

### Backend (Java)
1. **Core Services**
   - `FIXService`: Interface for FIX operations
   - `FIXServiceImpl`: Implementation handling FIX sessions and messages

2. **Models**
   - `SessionStatus`: FIX session information (sessionId, status)
   - `JSONDataWrapper<T>`: Generic wrapper for JSON responses

3. **REST API**
   - `FIXController`: REST endpoints for FIX operations
   - Endpoints:
     - GET `/sessions`: List all FIX sessions
     - POST `/messages/send`: Send FIX message to specific session
     - GET `/messages/sendrecvlist`: Get sent/received FIX messages list on specific session

4. **Configuration**
   - `log4j2.xml`: Logging configuration
   - Jetty and QuickFIX logging set to INFO level
   - Root logging at DEBUG level

### Frontend (Vue.js/Vuetify)
1. **Components**
   - `Sessions.vue`: Displays list of FIX sessions
   - `Messages.vue`: Interface for sending FIX messages
     - Session selection dropdown
     - Message input text area
     - Send message functionality
     - Table of sent/received FIX messages list on specific session

2. **Features**
   - Session list display with status
   - FIX message composition and sending
   - Real-time session status updates
   - Show table of sent/received FIX messages list on specific session

## Build System
- Gradle-based build configuration
- Separate builds for backend (app/) and frontend (wui/)
- Local JAR dependencies supported

## Current Functionality

### FIX Session Management
1. **Session Listing**
   - View all active FIX sessions
   - Display session status
   - Session ID format: "SENDER<-->TARGET"

2. **Message Operations**
   - Select target session
   - Compose FIX messages
   - Send messages to selected session
   - Example message format: `35=D,22=2,48=Sedol,55=Sony,38=100,40=1`
   - Show table of sent/received FIX messages list on specific session, component vuetable-2/Vuetable.vue is used.

## Dependencies
1. **Backend**
   - QuickFIX/J for FIX protocol
   - JAX-RS for REST API
   - Log4j2 for logging

2. **Frontend**
   - Vue.js framework
   - Vuetify UI components
   - Axios for HTTP requests

## Development Environment
- VS Code as IDE
- Java backend with Gradle
- Vue.js frontend with npm
- Git version control