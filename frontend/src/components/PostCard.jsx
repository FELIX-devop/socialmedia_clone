import React from 'react'
import './PostCard.css'

const PostCard = ({ post }) => {
  const formatDate = (dateString) => {
    const date = new Date(dateString)
    const now = new Date()
    const diffInHours = Math.floor((now - date) / (1000 * 60 * 60))
    
    if (diffInHours < 1) {
      return 'Just now'
    } else if (diffInHours < 24) {
      return `${diffInHours}h ago`
    } else {
      const diffInDays = Math.floor(diffInHours / 24)
      return `${diffInDays}d ago`
    }
  }

  const getImageUrl = (imageUrl) => {
    if (imageUrl.startsWith('http')) {
      return imageUrl
    }
    // If it's a relative URL, prepend the backend URL
    return `http://localhost:8080${imageUrl}`
  }

  return (
    <div className="post-card">
      <div className="post-header">
        <div className="post-user">
          <div className="user-avatar">
            {post.username.charAt(0).toUpperCase()}
          </div>
          <div className="user-info">
            <span className="username">{post.username}</span>
            <span className="timestamp">{formatDate(post.createdAt)}</span>
          </div>
        </div>
      </div>
      
      <div className="post-image">
        <img 
          src={getImageUrl(post.imageUrl)} 
          alt={post.caption || 'Post image'}
          onError={(e) => {
            e.target.style.display = 'none'
            e.target.nextSibling.style.display = 'flex'
          }}
        />
        <div className="image-error" style={{ display: 'none' }}>
          <span>Image not available</span>
        </div>
      </div>
      
      <div className="post-content">
        <div className="post-caption">
          <span className="username">{post.username}</span>
          <span className="caption-text">{post.caption}</span>
        </div>
      </div>
    </div>
  )
}

export default PostCard
