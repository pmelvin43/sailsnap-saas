// src/components/GalleryCreationPopup.tsx
import { useState } from 'react'
import { useAuth } from '../contexts/AuthContext'

interface GalleryCreationPopupProps {
  isOpen: boolean
  onClose: () => void
  onGalleryCreated: (gallery: any) => void
}

export default function GalleryCreationPopup({ isOpen, onClose, onGalleryCreated }: GalleryCreationPopupProps) {
    const { business } = useAuth()
    const [galleryName, setGalleryName] = useState('')
    const [uploadedFiles, setUploadedFiles] = useState<File[]>([])

    const handleCreateGallery = async (e: React.FormEvent) => {
        e.preventDefault()

        try {
            const galleryResponse = await fetch(
                `http://localhost:8080/galleries?businessId=${business?.id}&name=${encodeURIComponent(galleryName)}`, 
                {
                    method: 'POST',
                }
            )

            if (galleryResponse.ok) {
                const gallery = await galleryResponse.json()
                
                if (uploadedFiles.length > 0) {
                    await uploadFilesToGallery(gallery.id, uploadedFiles)
                }
                
                onGalleryCreated(gallery)
                setGalleryName('')
                setUploadedFiles([])
                onClose()
            }
        } catch (error) {
            console.error('Failed to create gallery:', error)
        }
    }

    const uploadFilesToGallery = async (galleryId: number, files: File[]) => {
        for (const file of files) {
            const formData = new FormData()
            formData.append('file', file)
            formData.append('businessId', business!.id.toString())
            formData.append('galleryId', galleryId.toString())
            
            try {
                await fetch('http://localhost:8080/media/upload', {
                    method: 'POST',
                    body: formData,
                })
            } catch (error) {
                console.error(`Error uploading ${file.name}:`, error)
            }
        }
    }

    const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            setUploadedFiles(prev => [...prev, ...Array.from(e.target.files!)])
        }
    }

    const handleDragOver = (e: React.DragEvent) => {
        e.preventDefault()
    }

    const handleDrop = (e: React.DragEvent) => {
        e.preventDefault()
        if (e.dataTransfer.files) {
            setUploadedFiles(prev => [...prev, ...Array.from(e.dataTransfer.files)])
        }
    }

    if (!isOpen) return null

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
            <h2>Create New Gallery</h2>

            <form onSubmit={handleCreateGallery}>
                <div style={{ marginBottom: '20px' }}>
                    <label>Gallery Name:</label>
                    <input
                        type="text"
                        value={galleryName}
                        onChange={(e) => setGalleryName(e.target.value)}
                        required
                        style={{ marginLeft: '10px' }}
                    />
                </div>

                <div
                    style={{
                        border: '2px dashed #ccc',
                        padding: '40px',
                        textAlign: 'center',
                        marginBottom: '20px'
                    }}
                    onDragOver={handleDragOver}
                    onDrop={handleDrop}
                >
                    <p>Drag & drop photos/videos here</p>
                    <p>or</p>
                    <input
                        type="file"
                        multiple
                        accept="image/*,video/*"
                        onChange={handleFileUpload}
                    />
                </div>

                {uploadedFiles.length > 0 && (
                    <div style={{ marginBottom: '20px' }}>
                        <h3>Files to Upload ({uploadedFiles.length}):</h3>
                        <ul>
                            {uploadedFiles.map((file, index) => (
                                <li key={index}>{file.name}</li>
                            ))}
                        </ul>
                    </div>
                )}

                <div>
                    <button type="submit">Create Gallery</button>
                    <button
                        type="button"
                        onClick={onClose}
                        style={{ marginLeft: '10px' }}
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    )
}