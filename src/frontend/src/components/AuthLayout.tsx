// src/components/AuthLayout.tsx
import { Outlet } from 'react-router-dom'

export default function AuthLayout() {
  return (
    <div>
      <Outlet /> {/* This renders the auth pages */}
    </div>
  )
}