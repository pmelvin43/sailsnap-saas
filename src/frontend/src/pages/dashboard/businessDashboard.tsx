// src/pages/dashboard/BusinessDashboard.tsx
import { useState } from 'react'

export default function BusinessDashboard() {
    const [showGalleryPopup, setShowGalleryPopup] = useState(false)

    return (
        <div>
            <h1>Business Dashboard</h1>
            <p>Welcome to your SailSnap dashboard!</p>

            <button onClick={() => setShowGalleryPopup(true)}>
                Create New Gallery
            </button>

            {showGalleryPopup && (
                <div style={{
                    position: 'fixed',
                    top: '15%',
                    left: '15%',
                    width: '70%',
                    height: '70%',
                    backgroundColor: 'white',
                    border: '1px solid #ccc',
                    zIndex: 1000,
                    padding: '20px'
                }}>
                    <h2>Gallery Creation</h2>
                    <p>This is where you'll create new galleries.</p>
                    <button onClick={() => setShowGalleryPopup(false)}>
                        Close
                    </button>
                </div>
            )}
        </div>
    )
}