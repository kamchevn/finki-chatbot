import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

const Home: React.FC = () => {
  const navigate = useNavigate()

  useEffect(() => {
    const timerId = window.setTimeout(() => {
      navigate('/chat', { replace: true })
    }, 10000)

    return () => {
      clearTimeout(timerId)
    }
  }, [navigate])

  return (
    <div className="min-h-screen bg-gray-900 flex items-center justify-center px-4">
      <div className="text-center">
        <div className="flex items-center justify-center mb-6">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
        </div>
        <h1 className="text-2xl font-semibold text-white mb-2">Loading...</h1>
        <p className="text-gray-400">Preparing the chatbot. You will be redirected shortly.</p>
      </div>
    </div>
  )
}

export default Home

