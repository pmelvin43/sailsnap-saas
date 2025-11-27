// src/components/ProtectedRoute.tsx
import { Navigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import type { JSX } from 'react'

export default function ProtectedRoute({ children }: { children: JSX.Element }) {
    const { isAuthenticated, loading } = useAuth()

    if (loading) {
        return <div>Loading...</div> // Simple loading state
    }

    return isAuthenticated ? children : <Navigate to="/login" />
}