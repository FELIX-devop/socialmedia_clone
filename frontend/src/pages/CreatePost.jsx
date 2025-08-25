import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { createPost } from '../services/api'
import './CreatePost.css'

const CreatePost = () => {
  const [imageFile, setImageFile] = useState(null)
  const [caption, setCaption] = useState('')
  const [preview, setPreview] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  
  const { user, isAuthenticated } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login')
      return
    }
  }, [isAuthenticated, navigate])

  const handleImageChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setImageFile(file)
      setError('')
      
      // Create preview
      const reader = new FileReader()
      reader.onload = () => {
        setPreview(reader.result)
      }
      reader.readAsDataURL(file)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!imageFile) {
      setError('Please select an image')
      return
    }

    if (!caption.trim()) {
      setError('Please enter a caption')
      return
    }

    try {
      setLoading(true)
      setError('')
      setSuccess('')
      
      await createPost(imageFile, caption, user.id)
      
      setSuccess('Post created successfully!')
      setImageFile(null)
      setCaption('')
      setPreview('')
      
      // Redirect to feed after a short delay
      setTimeout(() => {
        navigate('/')
      }, 1500)
      
    } catch (err) {
      setError(err.message || 'Failed to create post')
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    navigate('/')
  }

  if (!isAuthenticated) {
    return null
  }

  return (
    <div className="page">
      <div className="container">
        <div className="create-post-header">
          <h1>Create New Post</h1>
          <button onClick={handleCancel} className="btn btn-secondary">
            Cancel
          </button>
        </div>

        <div className="create-post-container">
          <form onSubmit={handleSubmit} className="create-post-form">
            <div className="form-group">
              <label htmlFor="image" className="form-label">
                Choose Image
              </label>
              <input
                type="file"
                id="image"
                accept="image/*"
                onChange={handleImageChange}
                className="form-control file-input"
                disabled={loading}
              />
            </div>

            {preview && (
              <div className="image-preview">
                <img src={preview} alt="Preview" />
              </div>
            )}

            <div className="form-group">
              <label htmlFor="caption" className="form-label">
                Caption
              </label>
              <textarea
                id="caption"
                className="form-control"
                placeholder="Write a caption..."
                value={caption}
                onChange={(e) => setCaption(e.target.value)}
                rows="4"
                disabled={loading}
              />
            </div>

            {error && <div className="error">{error}</div>}
            {success && <div className="success">{success}</div>}

            <button
              type="submit"
              className="btn"
              disabled={loading || !imageFile || !caption.trim()}
            >
              {loading ? 'Creating Post...' : 'Create Post'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

export default CreatePost
