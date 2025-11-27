// src/pages/public/SignupPage.tsx
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

interface Business {
    businessName: string
    email: string
    password: string
}

export default function Signup() {
    const [formData, setFormData] = useState<Business>({
        businessName: '',
        email: '',
        password: ''
    })
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const navigate = useNavigate()

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target
        setFormData(prev => ({
            ...prev,
            [id]: value
        }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        try {
            const response = await fetch('http://localhost:8080/business/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            })

            if (response.ok) {
                navigate('/login')
            } else {
                const errorData = await response.json()
                setError(errorData.message || 'Registration failed')
            }
        } catch (err) {
            setError('Network error. Please try again.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div>
            <h1>Create Your SailSnap Account</h1>

            {error && <div>{error}</div>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="businessName">Business Name</label>
                    <input
                        type="text"
                        id="businessName"
                        value={formData.businessName}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Creating Account...' : 'Sign Up'}
                </button>
            </form>

            <div>
                <p>Already have an account? <a href="/login">Login here</a></p>
            </div>
        </div>
    )
}