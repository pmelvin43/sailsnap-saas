// src/components/GalleryViewPopup.tsx
import { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'

interface MediaItem {
    id: number
    fileKey: string
    fileType: string
    mediaUrl: string  // This is the correct field name!
    uploadedAt: string
    fileSize: number
    isWatermarked: boolean
}

interface Gallery {
    id: number
    name: string
    createdAt: string
}

interface GalleryViewPopupProps {
    isOpen: boolean
    onClose: () => void
    gallery: Gallery | null
}

export default function GalleryViewPopup({ isOpen, onClose, gallery }: GalleryViewPopupProps) {
    const { business } = useAuth()
    const [galleryMedia, setGalleryMedia] = useState<MediaItem[]>([])
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        if (isOpen && gallery) {
            fetchGalleryMedia()
        }
    }, [isOpen, gallery])

    const fetchGalleryMedia = async () => {
        if (!gallery || !business?.id) return

        setLoading(true)
        try {
            const response = await fetch(
                `http://localhost:8080/media/gallery/${gallery.id}?businessId=${business.id}`
            )

            if (response.ok) {
                const media = await response.json()
                console.log('Media response:', media) // Should show mediaUrl field
                setGalleryMedia(media)
            } else {
                console.error('Failed to fetch media, status:', response.status)
            }
        } catch (error) {
            console.error('Failed to fetch gallery media:', error)
        } finally {
            setLoading(false)
        }
    }

    if (!isOpen || !gallery) return null

    return (
        <div style={{
            position: 'fixed',
            top: '10%',
            left: '10%',
            width: '80%',
            height: '80%',
            backgroundColor: 'white',
            border: '1px solid #ccc',
            zIndex: 1000,
            padding: '20px',
            overflow: 'auto'
        }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h2>Gallery: {gallery.name}</h2>
                <button onClick={onClose}>Close</button>
            </div>

            {loading ? (
                <div>Loading media...</div>
            ) : galleryMedia.length === 0 ? (
                <div>
                    <p>No media in this gallery yet.</p>
                </div>
            ) : (
                <div>
                    <p>Found {galleryMedia.length} media items</p>
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '10px' }}>
                        {galleryMedia.map(media => (
                            <div key={media.id} style={{ border: '1px solid #ddd', padding: '10px' }}>
                                {media.fileType === 'PHOTO' ? (
                                    <img
                                        src={media.mediaUrl}  // Use mediaUrl here!
                                        alt={media.fileKey}
                                        style={{ width: '100%', height: '150px', objectFit: 'cover' }}
                                        onError={(e) => {
                                            console.error('Failed to load image:', media.mediaUrl)
                                            e.currentTarget.style.backgroundColor = '#f0f0f0'
                                        }}
                                        onLoad={() => console.log('Image loaded successfully!')}
                                    />
                                ) : (
                                    <video
                                        src={media.mediaUrl}  // Use mediaUrl here!
                                        style={{ width: '100%', height: '150px', objectFit: 'cover' }}
                                        controls
                                        onError={() => console.error('Failed to load video:', media.mediaUrl)}
                                    />
                                )}
                                <p style={{ fontSize: '12px', marginTop: '5px' }}>
                                    {media.fileType.toLowerCase()}
                                </p>
                                <p style={{ fontSize: '10px', color: '#666' }}>
                                    Size: {(media.fileSize / 1024 / 1024).toFixed(1)} MB
                                </p>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    )
}