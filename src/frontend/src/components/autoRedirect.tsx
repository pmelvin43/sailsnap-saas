// src/components/AutoRedirect.tsx
import { Navigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import type { JSX } from 'react'

export default function AutoRedirect({ children }: { children: JSX.Element }) {
  const { isAuthenticated, loading } = useAuth()
  
  if (loading) {
    return <div>Loading...</div>
  }
  
  return isAuthenticated ? <Navigate to="/dashboard" /> : children
}