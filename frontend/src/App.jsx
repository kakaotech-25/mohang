import './App.css'
import {Routes, Route} from "react-router-dom"
import Login from './pages/Login.jsx'

function App() {

  return (
    <>
    <h1>test</h1>
    <Routes>
      <Route path="/login" element={<Login />} />
    </Routes>
    </>
  )
}

export default App
