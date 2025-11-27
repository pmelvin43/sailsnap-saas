import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import AuthLayout from './components/authLayout'
import DashboardLayout from './components/dashboardLayout'
import Signup from './pages/public/signupPage'
import Login from './pages/public/loginPage'
import LandingPage from './pages/public/landingPage'
import ProtectedRoute from './components/ProtectedRoute'
import BusinessDashboard from './pages/dashboard/businessDashboard'
import AutoRedirect from './components/autoRedirect'

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