import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { fetchPosts } from '../services/api'
import PostCard from '../components/PostCard'
import './Feed.css'

const Feed = () => {
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  
  const { isAuthenticated } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login')
      return
    }

    loadPosts()
  }, [isAuthenticated, navigate])

  const loadPosts = async () => {
    try {
      setLoading(true)
      const fetchedPosts = await fetchPosts()
      setPosts(fetchedPosts)
      setError('')
    } catch (err) {
      setError('Failed to load posts')
      console.error('Error loading posts:', err)
    } finally {
      setLoading(false)
    }
  }

  const handlePostCreated = () => {
    loadPosts()
  }

  if (!isAuthenticated) {
    return null
  }

  return (
    <div className="page">
      <div className="container">
        <div className="feed-header">
          <h1>Instagram Clone Feed</h1>
          <button 
            onClick={() => navigate('/post')} 
            className="btn"
          >
            Create New Post
          </button>
        </div>

        {loading && (
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Loading posts...</p>
          </div>
        )}

        {error && (
          <div className="error-container">
            <p className="error">{error}</p>
            <button onClick={loadPosts} className="btn btn-secondary">
              Try Again
            </button>
          </div>
        )}

        {!loading && !error && posts.length === 0 && (
          <div className="empty-state">
            <h3>No posts yet</h3>
            <p>Be the first to create a post!</p>
            <button 
              onClick={() => navigate('/post')} 
              className="btn"
            >
              Create Your First Post
            </button>
          </div>
        )}

        {!loading && !error && posts.length > 0 && (
          <div className="posts-grid">
            {posts.map((post) => (
              <PostCard 
                key={post.id} 
                post={post} 
                onPostCreated={handlePostCreated}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default Feed
