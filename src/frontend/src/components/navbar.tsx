// src/components/Navbar.tsx
import { Link } from 'react-router-dom'

export default function Navbar() {
  return (
    <nav style={{ 
      padding: '1rem', 
      borderBottom: '1px solid #ccc',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }}>
      <div className="logo">
        <Link to="/" style={{ textDecoration: 'none', fontWeight: 'bold' }}>
          SailSnap
        </Link>
      </div>
      
      <div className="nav-links">
        <Link to="/signup" style={{ marginLeft: '1rem' }}>
          Sign Up
        </Link>
        {/* We'll add Login link later */}
      </div>
    </nav>
  )
}