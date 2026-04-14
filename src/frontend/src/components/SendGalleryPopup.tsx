// src/components/SendGalleryPopup.tsx
import { useState } from 'react'

interface Gallery {
    id: number
    name: string
    createdAt: string
}

interface SendGalleryPopupProps {
    isOpen: boolean
    onClose: () => void
    gallery: Gallery | null
}

export default function SendGalleryPopup({
    isOpen,
    onClose,
    gallery
}: SendGalleryPopupProps) {

    const [emails, setEmails] = useState("")
    const [message, setMessage] = useState("")
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    if (!isOpen || !gallery) return null

    const handleSend = async () => {
        if (!emails.trim()) {
            setError("Please enter at least one email.")
            return
        }

        const emailList = emails
            .split(",")
            .map(e => e.trim())
            .filter(Boolean)

        setLoading(true)
        setError(null)

        try {
            const response = await fetch(
                "http://localhost:8080/galleries/send",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        galleryId: gallery.id,
                        emails: emailList,
                        message: message
                    })
                }
            )

            if (!response.ok) {
                throw new Error("Failed to send gallery")
            }

            onClose()
            setEmails("")
            setMessage("")
        } catch (err) {
            setError("Failed to send emails. Please try again.")
        } finally {
            setLoading(false)
        }
    }

    return (
        <div style={{
            position: 'fixed',
            top: '20%',
            left: '30%',
            width: '40%',
            backgroundColor: 'white',
            border: '1px solid #ccc',
            zIndex: 1000,
            padding: '20px'
        }}>
            <h2>Send Gallery: {gallery.name}</h2>

            <div style={{ marginBottom: '10px' }}>
                <label>Customer Emails (comma separated)</label>
                <input
                    type="text"
                    value={emails}
                    onChange={(e) => setEmails(e.target.value)}
                    style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    placeholder="example@email.com, another@email.com"
                />
            </div>

            <div style={{ marginBottom: '10px' }}>
                <label>Optional Message</label>
                <textarea
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    rows={4}
                    placeholder="Add a personal message..."
                />
            </div>

            {error && (
                <div style={{ color: 'red', marginBottom: '10px' }}>
                    {error}
                </div>
            )}

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
                <button onClick={onClose}>Cancel</button>
                <button onClick={handleSend} disabled={loading}>
                    {loading ? "Sending..." : "Send"}
                </button>
            </div>
        </div>
    )
}