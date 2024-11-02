import "./App.css";
import { Routes, Route } from "react-router-dom";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Home from "./pages/Home/Home.jsx";
import Login from "./pages/Login/Login.jsx";
import Profile from "./pages/Profile/Profile.jsx";
import LivingInfo from "./pages/LivingInfo/LivingInfo.jsx";
import InterestedPlace from "./pages/InterestedPlace/InterestedPlace.jsx";
import Mypage from "./pages/Mypage/Mypage.jsx";
import Planner from "./pages/Planner/Planner.jsx";
import PlannerDetails from "./pages/PlannerDetails/PlannerDetails.jsx";
import TravelDetails from "./pages/TravelDetails/TravelDetails.jsx";
import Landing from "./pages/Landing/Landing.jsx";
import Callback from "./pages/Login/Callback.jsx";

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/callback/kakao" element={<Callback />} />
        <Route path="/landing" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup/profile" element={<Profile />} />
        <Route path="/signup/livinginfo" element={<LivingInfo />} />
        <Route path="/signup/interestedplace" element={<InterestedPlace />} />
        <Route path="/mypage" element={<Mypage />} />
        <Route path="/traveldetails/:id" element={<TravelDetails />} />
        <Route path="/planner" element={<Planner />} />
        <Route path="/plannerdetails/:id" element={<PlannerDetails />} />
      </Routes>
      <Footer />
    </>
  );
}

export default App;
