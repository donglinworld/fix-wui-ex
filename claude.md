# FIX Web UI Project Summary

## Project Overview
A web-based FIX (Financial Information eXchange) application with Java backend and Vue.js frontend, providing FIX session management and message handling capabilities.

## Architecture

### Backend (Java)
1. **Core Services**
   - `FIXService`: Interface for FIX operations
   - `FIXServiceImpl`: Implementation handling FIX sessions and messages

2. **Models**
   - `SessionStatus`: FIX session information (sessionId, status)
   - `FixMessage`: Message information (sessionId, msgDirection, messageContent, timestamp)
   - `JSONDataWrapper<T>`: Generic wrapper for JSON responses

3. **REST API** (`FIXController`)
   - Endpoints:
     - GET `/sessions`: List all FIX sessions
     - POST `/messages/send`: Send FIX message to specific session
     - GET `/messages/sendrecvlist`: Get sent/received FIX messages list for specific session

4. **Configuration**
   - `log4j2.xml`: Logging configuration
     - Jetty and QuickFIX logging set to INFO level
     - Root logging at DEBUG level

### Frontend (Vue.js/Vuetify)
1. **Components**
   - `Sessions.vue`: Displays list of FIX sessions
   - `Messages.vue`: Interface for sending FIX messages and viewing message history
     - Session selection dropdown
     - Message input text area (monospace font, larger size)
     - Send message functionality
     - Message history table using vuetable-2

2. **Features**
   - Session list display with status
   - FIX message composition and sending
   - Real-time session status updates
   - Message history display with:
     - Message direction (SENT/RECEIVED)
     - Message content
     - Timestamp
     - Automatic refresh on session change or new message

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
   - Compose FIX messages in large text area
   - Send messages to selected session
   - Example message format: `8=FIX.4.2|9=57|35=D|34=6|49=BANZAI42|52=20250910-23:54:51.405|56=EXEC42|22=2|48=Sedol|55=Sony|38=100|40=1|54=1|60=20250910-23:54:51.405|21=1|11=abc-123|44=10.5|10=129|`
   - View message history per session
     - Table display using vuetable-2
     - Sort by direction, content, or timestamp
     - Auto-refresh on new messages

## Dependencies
1. **Backend**
   - QuickFIX/J for FIX protocol
   - JAX-RS for REST API
   - Log4j2 for logging
   - Jakarta XML Binding for data serialization

2. **Frontend**
   - Vue.js framework
   - Vuetify UI components
   - Axios for HTTP requests
   - Vuetable-2 for data tables

## Build and Development
1. **Build System**
   - Gradle-based build configuration
   - Separate builds for backend (app/) and frontend (wui/)
   - Local JAR dependencies supported

2. **Development Environment**
   - VS Code as IDE
   - Java backend with Gradle
   - Vue.js frontend with npm
   - Git version control

## Future Enhancements
1. Message filtering and search in history table
2. More detailed session statistics
3. Real-time message updates
4. Advanced message validation
5. Session configuration management