import "./InterestedPlace.css"
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar3 from "../../assets/progressbar3.png"
import TravelCard from "../../components/TravelCard/TravelCard";
import travelcardimg from "../../assets/travelcard.png"

const InterestedPlace = () => {
    return (
        <section className="interested-page">
            <ProfileInfo text={"AI 맞춤 추천을 위해 관심 여행지를 5개 이상 선택해주세요."} src={progressbar3} />

            <section className="interested-body">
                <TravelCard
                    image={travelcardimg}
                    tags={['20대', '연인']}
                    title="롯데월드"
                    location="서울특별시 송파구"
                />
            </section>
        </section>
    );
};

export default InterestedPlace;