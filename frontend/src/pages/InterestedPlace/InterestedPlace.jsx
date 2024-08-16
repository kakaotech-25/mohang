import "./InterestedPlace.css"
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar3 from "../../assets/progressbar3.png"

const InterestedPlace = () => {
    return (
        <section className="interested-page">
            <ProfileInfo text={"AI 맞춤 추천을 위해 관심 여행지를 5개 이상 선택해주세요."} src={progressbar3} />
        </section>
    );
};

export default InterestedPlace;