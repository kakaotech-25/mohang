import './App.css'
import {Routes, Route} from "react-router-dom"
import Header from "./components/Header.jsx"
import Footer from "./components/Footer.jsx"
import Home from "./pages/Home/Home.jsx"
import Login from './pages/Login/Login.jsx'
import Profile from './pages/Profile/Profile.jsx'
import LivingInfo from './pages/LivingInfo/LivingInfo.jsx'
import InterestedPlace from './pages/InterestedPlace/InterestedPlace.jsx'
import Mypage from './pages/Mypage/Mypage.jsx'
import Place from './pages/Place/Place.jsx'
import Planner from './pages/Planner/Planner.jsx'
import PlannerList from './pages/PlannerList/PlannerList.jsx'

function App() {

  return (
    <>
      
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup/profile" element={<Profile />} />
        <Route path="/signup/livinginfo" element={<LivingInfo />} />
        <Route path="/signup/interestedplace" element={<InterestedPlace />} />
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
