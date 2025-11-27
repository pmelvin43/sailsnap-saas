// src/pages/public/SignupPage.tsx
export default function Signup() {
    // We'll add state and functionality here next
    return (
        <div style={{
            maxWidth: '400px',
            margin: '2rem auto',
            padding: '2rem',
            border: '1px solid #ddd',
            borderRadius: '8px'
        }}>
            <h1>Create Your SailSnap Account</h1>

            <form>
                <div style={{ marginBottom: '1rem' }}>
                    <label htmlFor="businessName" style={{ display: 'block', marginBottom: '0.5rem' }}>
                        Business Name
                    </label>
                    <input
                        type="text"
                        id="businessName"
                        style={{
                            width: '100%',
                            padding: '0.5rem',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    />
                </div>

                <div style={{ marginBottom: '1rem' }}>
                    <label htmlFor="email" style={{ display: 'block', marginBottom: '0.5rem' }}>
                        Email
                    </label>
                    <input
                        type="email"
                        id="email"
                        style={{
                            width: '100%',
                            padding: '0.5rem',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    />
                </div>

                <div style={{ marginBottom: '1rem' }}>
                    <label htmlFor="password" style={{ display: 'block', marginBottom: '0.5rem' }}>
                        Password
                    </label>
                    <input
                        type="password"
                        id="password"
                        style={{
                            width: '100%',
                            padding: '0.5rem',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    />
                </div>

                <button
                    type="submit"
                    style={{
                        width: '100%',
                        padding: '0.75rem',
                        backgroundColor: '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                >
                    Sign Up
                </button>
            </form>
        </div>
    )
}