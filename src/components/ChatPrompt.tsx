import React, { useState, useRef, useCallback } from 'react'

// Types for API responses
interface GlossaryItem {
  id: string | number
  content: string
  bm25Score?: number
}

// Component state types
type RequestStatus = 'idle' | 'loading' | 'success' | 'error' | 'forbidden'

const ChatPrompt: React.FC = () => {
  // State management
  const [promptText, setPromptText] = useState('')
  const [response, setResponse] = useState('')
  const [status, setStatus] = useState<RequestStatus>('idle')
  const [lastSubmittedPrompt, setLastSubmittedPrompt] = useState('')
  
  // Abort controller for request timeout
  const abortControllerRef = useRef<AbortController | null>(null)

  // API endpoint
  const API_URL = 'http://localhost:8080/glossary/search'
  const TIMEOUT_MS = 10000 // 10 seconds

  // Function to send prompt to API
  const sendPrompt = useCallback(async (prompt: string) => {
    // Abort any existing request
    if (abortControllerRef.current) {
      abortControllerRef.current.abort()
    }

    // Create new abort controller for this request
    const controller = new AbortController()
    abortControllerRef.current = controller
    const timeoutId = window.setTimeout(() => {
      controller.abort()
    }, TIMEOUT_MS)

    const startAt = Date.now()

    setStatus('loading')
    setResponse('')

    let plannedResponse = ''
    let plannedStatus: RequestStatus = 'success'

    try {
      // Build query params
      const query = new URLSearchParams()
      query.append('q', prompt)
      const url = `${API_URL}?${query.toString()}`

      // Make API request with timeout
      const res = await fetch(url, {
        method: 'GET',
        signal: controller.signal,
      })

      // Handle different response statuses
      if (res.status === 200) {
        const data: GlossaryItem[] = await res.json()

        if (!Array.isArray(data) || data.length === 0) {
          plannedResponse = `Извини, но не најдов никакви информации поврзани со ${prompt}`
          plannedStatus = 'success'
        } else if (data.length === 1) {
          plannedResponse = data[0]?.content ?? ''
          plannedStatus = 'success'
        } else {
          plannedResponse = data[1]?.content ?? data[0]?.content ?? ''
          plannedStatus = 'success'
        }
      } else if (res.status === 403) {
        plannedResponse = 'Server is offline'
        plannedStatus = 'forbidden'
      } else if (res.status === 500) {
        // Try to extract error message from response body
        try {
          const errorData = await res.json()
          const errorMessage = (errorData as any).message || 'Server is offline'
          
          // Check if the error message contains the specific upstream 403 error
          if (errorMessage.includes('403 Forbidden on POST request for "https://llama3.finki.ukim.mk/api/generate"')) {
            plannedResponse = 'Server is offline'
            plannedStatus = 'forbidden'
          } else if (errorMessage.includes('HttpException (0x80004005): Request timed out.')) {
            plannedResponse = 'There was an error while generating the response. Try again'
            plannedStatus = 'error'
          } else {
            plannedResponse = errorMessage
            plannedStatus = 'forbidden'
          }
        } catch {
          plannedResponse = 'Server is offline'
          plannedStatus = 'forbidden'
        }
      } else {
        throw new Error(`HTTP ${res.status}`)
      }
    } catch (error) {
      // Handle timeout, network errors, and other exceptions
      if (error instanceof Error && error.name === 'AbortError') {
        // Request was aborted (timeout)
        plannedResponse = 'There was an error while generating the response. Try again'
        plannedStatus = 'error'
      } else {
        // Other network errors
        plannedResponse = 'There was an error while generating the response. Try again'
        plannedStatus = 'error'
      }
    } finally {
      clearTimeout(timeoutId)
      abortControllerRef.current = null
    }

    // Enforce minimum 10s delay before showing the result, unless message is 'Server is offline'
    const elapsedMs = Date.now() - startAt
    const shouldDelay = !(plannedStatus === 'forbidden' && plannedResponse === 'Server is offline')
    if (shouldDelay && elapsedMs < TIMEOUT_MS) {
      await new Promise<void>((resolve) => setTimeout(resolve, TIMEOUT_MS - elapsedMs))
    }

    setResponse(plannedResponse)
    setStatus(plannedStatus)
  }, [])

  // Handle form submission
  const handleSubmit = useCallback((e: React.FormEvent) => {
    e.preventDefault()
    const trimmedPrompt = promptText.trim()
    
    if (trimmedPrompt) {
      setLastSubmittedPrompt(trimmedPrompt)
      sendPrompt(trimmedPrompt)
    }
  }, [promptText, sendPrompt])

  // Handle retry button click
  const handleRetry = useCallback(() => {
    if (lastSubmittedPrompt) {
      sendPrompt(lastSubmittedPrompt)
    }
  }, [lastSubmittedPrompt, sendPrompt])

  // Handle key press in textarea
  const handleKeyPress = useCallback((e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      const trimmedPrompt = promptText.trim()
      
      if (trimmedPrompt) {
        setLastSubmittedPrompt(trimmedPrompt)
        sendPrompt(trimmedPrompt)
      }
    }
  }, [promptText, sendPrompt])

  // Check if submit button should be disabled
  const isSubmitDisabled = status === 'loading' || !promptText.trim()

  return (
    <div className="bg-gray-800 rounded-lg shadow-lg border border-gray-700 p-6">
      {/* Input Form */}
      <form onSubmit={handleSubmit} className="mb-6">
        <div className="mb-4">
          <label htmlFor="prompt" className="block text-sm font-medium text-gray-300 mb-2">
            Enter your prompt:
          </label>
          <textarea
            id="prompt"
            value={promptText}
            onChange={(e) => setPromptText(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Type your message here..."
            className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 resize-none text-white placeholder-gray-400"
            rows={4}
            disabled={status === 'loading'}
          />
        </div>
        
        <button
          type="submit"
          disabled={isSubmitDisabled}
          className={`w-full px-4 py-2 rounded-md font-medium transition-colors ${
            isSubmitDisabled
              ? 'bg-gray-600 text-gray-400 cursor-not-allowed'
              : 'bg-blue-600 text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-gray-800'
          }`}
        >
          {status === 'loading' ? 'Generating...' : 'Submit'}
        </button>
      </form>

      {/* Response Panel */}
      {status !== 'idle' && (
        <div className="border-t border-gray-700 pt-6">
          <h3 className="text-lg font-medium text-white mb-3">Response:</h3>
          
          <div className="bg-gray-700 rounded-md p-4 min-h-[100px] border border-gray-600">
            {status === 'loading' && (
              <div className="flex items-center text-gray-300">
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-500 mr-2"></div>
                Generating...
              </div>
            )}
            
            {status === 'success' && (
              <p className="text-gray-200 whitespace-pre-wrap">{response}</p>
            )}
            
            {status === 'forbidden' && (
              <p className="text-red-400 font-medium">{response}</p>
            )}
            
            {status === 'error' && (
              <div>
                <p className="text-red-400 font-medium mb-3">{response}</p>
                <button
                  onClick={handleRetry}
                  className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-gray-700 transition-colors"
                >
                  Try again
                </button>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default ChatPrompt
