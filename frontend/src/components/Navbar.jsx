import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import './Navbar.css'

const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
  }

  if (!isAuthenticated) {
    return null
  }

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-brand">
          <Link to="/" className="navbar-logo">
            Instagram Clone
          </Link>
        </div>
        
        <div className="navbar-menu">
          <Link to="/" className="navbar-link">
            Feed
          </Link>
          <Link to="/post" className="navbar-link">
            Create Post
          </Link>
        </div>
        
        <div className="navbar-user">
          <span className="username">@{user?.username}</span>
          <button onClick={handleLogout} className="btn btn-secondary">
            Logout
          </button>
        </div>
      </div>
    </nav>
  )
}

export default Navbar
