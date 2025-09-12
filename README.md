# Chatbot UI

A React + TypeScript frontend for a simple chatbot interface built with Vite and Tailwind CSS.

## Features

- Clean, modern UI with responsive design
- Real-time prompt submission with loading states
- Robust error handling for different HTTP status codes
- Automatic retry functionality for failed requests
- Keyboard shortcuts (Enter to submit, Shift+Enter for new line)
- 10-second request timeout with proper abort handling

## Tech Stack

- **React 18** with TypeScript
- **Vite** for fast development and building
- **Tailwind CSS** for styling
- **Fetch API** with AbortController for request management

## Getting Started

### Prerequisites

- Node.js (version 16 or higher)
- npm or yarn

### Installation

1. Clone the repository or navigate to the project directory
2. Install dependencies:

```bash
npm install
```

3. Start the development server:

```bash
npm run dev
```

4. Open your browser and navigate to `http://localhost:5173`

### Building for Production

```bash
npm run build
```

The built files will be in the `dist` directory.

## API Integration

The app expects a backend API running at `http://localhost:8080` with the following endpoint:

- **POST** `/api/prompt/generate`
- **Content-Type**: `application/x-www-form-urlencoded`
- **Body**: `promptText=<user's prompt>`

### Response Format

**Success (200 OK):**
```json
{
  "responseDto": {
    "response": "The AI's response text"
  }
}
```

**Error Handling:**
- **403 Forbidden**: Shows "Server is offline"
- **Network errors/Timeout**: Shows "There was an error while generating the response. Try again" with a retry button

## Project Structure

```
src/
├── components/
│   └── ChatPrompt.tsx    # Main chatbot component
├── App.tsx               # Main app component
├── main.tsx             # App entry point
└── index.css            # Global styles with Tailwind
```

## Usage

1. Type your prompt in the textarea
2. Press Enter or click Submit to send the request
3. View the response in the response panel below
4. If there's an error, use the "Try again" button to retry with the same prompt

## Development

- **Linting**: `npm run lint`
- **Preview build**: `npm run preview`
