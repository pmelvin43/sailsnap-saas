// src/components/Navbar.tsx
import { Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function Navbar() {
    const { logout, business } = useAuth()

    return (
        <nav style={{
            padding: '1rem',
            borderBottom: '1px solid #ccc',
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center'
        }}>
            <div className="logo">
                <Link to="/dashboard" style={{ textDecoration: 'none', fontWeight: 'bold' }}>
                    SailSnap
                </Link>
            </div>

            <div className="nav-links">
                <span>Welcome, {business?.businessName}!</span>
                <button
                    onClick={logout}
                    style={{ marginLeft: '1rem' }}
                >
                    Logout
                </button>
            </div>
        </nav>
    )
}