import React, { createContext, useContext, useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { loginUser } from '../services/api'

const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [token, setToken] = useState(null)
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    // Check if user is already logged in
    const savedUser = localStorage.getItem('user')
    const savedToken = localStorage.getItem('token')
    
    if (savedUser && savedToken) {
      setUser(JSON.parse(savedUser))
      setToken(savedToken)
    }
    
    setLoading(false)
  }, [])

  const login = async (username, password) => {
    try {
      const response = await loginUser(username, password)
      
      if (response.userId && response.username && response.token) {
        const userData = {
          id: response.userId,
          username: response.username
        }
        
        setUser(userData)
        setToken(response.token)
        
        // Save to localStorage
        localStorage.setItem('user', JSON.stringify(userData))
        localStorage.setItem('token', response.token)
        
        navigate('/')
        return { success: true }
      } else {
        return { success: false, error: 'Invalid response from server' }
      }
    } catch (error) {
      return { success: false, error: error.message || 'Login failed' }
    }
  }

  const logout = () => {
    setUser(null)
    setToken(null)
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    navigate('/login')
  }

  const value = {
    user,
    token,
    loading,
    login,
    logout,
    isAuthenticated: !!user && !!token
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}
