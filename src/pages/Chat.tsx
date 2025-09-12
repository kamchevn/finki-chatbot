import React from 'react'
import ChatPrompt from '../components/ChatPrompt'

const Chat: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-900 py-8">
      <div className="max-w-4xl mx-auto px-4">
        <header className="text-center mb-8">
          {/* Logo Space */}
          <div className="mb-6">
            <div className="w-24 h-24 mx-auto bg-gray-800 rounded-lg border-2 border-gray-700 flex items-center justify-center">
              <img 
                src="src\images\moodle-io.png" 
                alt="Logo" 
                className="w-24 h-24 mx-auto"
                style={{paddingTop: '10px'}}
              />
            </div>
          </div>
          
          <h1 className="text-3xl font-bold text-white mb-2">
            Finki Chatbot
          </h1>
          <p className="text-gray-400">
            Enter your prompt below to get a response from the AI
          </p>
        </header>
        
        <ChatPrompt />
      </div>
    </div>
  )
}

export default Chat

