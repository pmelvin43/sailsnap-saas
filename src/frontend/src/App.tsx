import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import AuthLayout from './components/AuthLayout'
import DashboardLayout from './components/DashboardLayout'
import Signup from './pages/public/SignupPage'
import Login from './pages/public/LoginPage'
import LandingPage from './pages/public/LandingPage'
import ProtectedRoute from './components/ProtectedRoute'
import BusinessDashboard from './pages/dashboard/BusinessDashboard'
import AutoRedirect from './components/AutoRedirect'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public routes */}
          <Route element={<AuthLayout />}>
            <Route
              path="/"
              element={
                <AutoRedirect>
                  <LandingPage />
                </AutoRedirect>
              }
            />
            <Route path="/signup" element={<Signup />} />
            <Route path="/login" element={<Login />} />
          </Route>

          {/* Protected routes */}
          <Route element={<DashboardLayout />}>
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <BusinessDashboard />
                </ProtectedRoute>
              }
            />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App