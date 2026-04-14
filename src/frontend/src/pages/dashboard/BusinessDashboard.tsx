// src/pages/dashboard/BusinessDashboard.tsx
import { useState, useEffect } from 'react'
import { useAuth } from '../../contexts/AuthContext'
import GalleryCreationPopup from '../../components/GalleryCreationPopup'
import GalleryViewPopup from '../../components/GalleryViewPopup'
import SendGalleryPopup from '../../components/SendGalleryPopup'

interface Gallery {
    id: number
    name: string
    createdAt: string
}

export default function BusinessDashboard() {
    const { business } = useAuth()
    const [showGalleryPopup, setShowGalleryPopup] = useState(false)
    const [showViewPopup, setShowViewPopup] = useState(false)
    const [showSendPopup, setShowSendPopup] = useState(false)
    const [galleries, setGalleries] = useState<Gallery[]>([])
    const [loading, setLoading] = useState(true)
    const [selectedGallery, setSelectedGallery] = useState<Gallery | null>(null)

    useEffect(() => {
        const fetchGalleries = async () => {
            if (!business?.id) return

            try {
                const response = await fetch(
                    `http://localhost:8080/galleries/list-galleries?businessId=${business.id}`,
                    {
                        method: 'POST',
                    }
                )

                if (response.ok) {
                    const galleriesData = await response.json()
                    setGalleries(galleriesData)
                }
            } catch (error) {
                console.error('Failed to fetch galleries:', error)
            } finally {
                setLoading(false)
            }
        }

        fetchGalleries()
    }, [business?.id])

    const handleViewGallery = (gallery: Gallery) => {
        setSelectedGallery(gallery)
        setShowViewPopup(true)
    }

    const handleGalleryCreated = (gallery: any) => {
        setGalleries(prev => [...prev, gallery])
    }

    if (loading) {
        return <div>Loading galleries...</div>
    }

    return (
        <div>
            <h1>Business Dashboard</h1>
            <p>Welcome to your SailSnap dashboard!</p>

            <button onClick={() => setShowGalleryPopup(true)}>
                Create New Gallery
            </button>

            <div style={{ marginTop: '20px' }}>
                <h3>Your Galleries ({galleries.length})</h3>
                {galleries.length === 0 ? (
                    <p>No galleries yet. Create your first one!</p>
                ) : (
                    <div>
                        {galleries.map(gallery => (
                            <div key={gallery.id} style={{ border: '1px solid #ccc', padding: '10px', margin: '10px 0' }}>
                                <h4>{gallery.name}</h4>
                                <p>ID: {gallery.id}</p>
                                <p>Created: {new Date(gallery.createdAt).toLocaleDateString()}</p>
                                <button onClick={() => handleViewGallery(gallery)}>
                                    View Gallery
                                </button>
                                <button onClick={() => {
                                    setSelectedGallery(gallery)
                                    setShowSendPopup(true)
                                }}>
                                    Send Gallery
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            <GalleryCreationPopup
                isOpen={showGalleryPopup}
                onClose={() => setShowGalleryPopup(false)}
                onGalleryCreated={handleGalleryCreated}
            />

            <GalleryViewPopup
                isOpen={showViewPopup}
                onClose={() => setShowViewPopup(false)}
                gallery={selectedGallery}
            />

            <SendGalleryPopup
                isOpen={showSendPopup}
                onClose={() => setShowSendPopup(false)}
                gallery={selectedGallery}
            />
        </div>
    )
}