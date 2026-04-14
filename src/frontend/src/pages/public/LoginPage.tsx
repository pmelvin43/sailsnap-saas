// src/pages/public/LoginPage.tsx
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

export default function Login() {
    const { login } = useAuth()
    const navigate = useNavigate()
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        try {
            const response = await fetch('http://localhost:8080/business/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            })

            if (response.ok) {
                const business = await response.json() // Get the business data
                login(business) // Update auth context
                navigate('/dashboard') // Use React Router navigation
            } else {
                setError('Invalid email or password')
            }
        } catch (err) {
            setError('Network error. Please try again.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div>
            <h1>Login to SailSnap</h1>

            {error && <div>{error}</div>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Logging in...' : 'Login'}
                </button>
            </form>

            <div>
                <p>Don't have an account? <a href="/signup">Sign up here</a></p>
            </div>
        </div>
    )
}