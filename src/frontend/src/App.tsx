import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Signup from './pages/public/signupPage'
import Navbar from './components/navbar'

function App() {
  return (
    <BrowserRouter>
      {/* Navigation that appears on every page */}
      <Navbar />

      {/* Main content area - changes based on route */}
      <main>
        <Routes>
          <Route path="/signup" element={<Signup />} />
          {/* We'll add more routes as we build them */}
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App 