// src/components/DashboardLayout.tsx
import { Outlet } from 'react-router-dom'
import Navbar from './Navbar'

export default function DashboardLayout() {
  return (
    <div>
      <Navbar />
      <main>
        <Outlet /> {/* This renders dashboard pages */}
      </main>
    </div>
  )
}