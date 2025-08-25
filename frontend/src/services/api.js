import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Add token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// API functions
export const loginUser = async (username, password) => {
  try {
    const response = await api.post('/api/auth/login', {
      username,
      password,
    })
    return response.data
  } catch (error) {
    if (error.response?.data) {
      throw new Error(error.response.data)
    }
    throw new Error('Login failed')
  }
}

export const fetchPosts = async () => {
  try {
    const response = await api.get('/api/posts')
    return response.data
  } catch (error) {
    console.error('Error fetching posts:', error)
    throw new Error('Failed to fetch posts')
  }
}

export const createPost = async (imageFile, caption, userId) => {
  try {
    const formData = new FormData()
    formData.append('image', imageFile)
    formData.append('caption', caption)
    formData.append('userId', userId)

    const response = await api.post('/api/posts', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  } catch (error) {
    if (error.response?.data) {
      throw new Error(error.response.data)
    }
    throw new Error('Failed to create post')
  }
}

export const getUserPosts = async (userId) => {
  try {
    const response = await api.get(`/api/posts/user/${userId}`)
    return response.data
  } catch (error) {
    console.error('Error fetching user posts:', error)
    throw new Error('Failed to fetch user posts')
  }
}
