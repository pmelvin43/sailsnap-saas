// src/contexts/AuthContext.tsx
import { createContext, useContext, useState, useEffect } from 'react'
import type { ReactNode } from 'react'

interface Business {
    id: number
    businessName: string
    email: string
}

interface AuthContextType {
    business: Business | null
    login: (business: Business) => void
    logout: () => void
    isAuthenticated: boolean
    loading: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
    const [business, setBusiness] = useState<Business | null>(null)
    const [loading, setLoading] = useState(true)

    // Check localStorage on app start
    useEffect(() => {
        const savedBusiness = localStorage.getItem('sailsnap_business')
        if (savedBusiness) {
            setBusiness(JSON.parse(savedBusiness))
        }
        setLoading(false)
    }, [])

    const login = (businessData: Business) => {
        setBusiness(businessData)
        localStorage.setItem('sailsnap_business', JSON.stringify(businessData))
    }

    const logout = () => {
        setBusiness(null)
        localStorage.removeItem('sailsnap_business')
    }

    const isAuthenticated = !!business

    return (
        <AuthContext.Provider value={{ business, login, logout, isAuthenticated, loading }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const context = useContext(AuthContext)
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider')
    }
    return context
}