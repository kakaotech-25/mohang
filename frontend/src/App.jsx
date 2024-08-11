import './App.css'
import {Routes, Route} from "react-router-dom"
import Header from "./components/Header.jsx"
import Footer from "./components/Footer.jsx"
import Home from "./pages/Home.jsx"
import Login from './pages/Login.jsx'
import Profile from './pages/Profile.jsx'
import Mypage from './pages/Mypage.jsx'
import Place from './pages/Place.jsx'
import Planner from './pages/Planner.jsx'
import PlannerList from './pages/PlannerList.jsx'

function App() {

  return (
    <>
      
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/mypage" element={<Mypage />} />
        <Route path="/place" element={<Place />} />
        <Route path="/planner" element={<Planner />} />
        <Route path="/plannerlist" element={<PlannerList />} />
      </Routes>
      <Footer />
    </>
  )
}

export default App
