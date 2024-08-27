import "./InterestedPlace.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar3 from "../../assets/progressbar3.png";
import TravelCard from "../../components/TravelCard/TravelCard";
import Button from "../../components/Button/Button";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import TravelData from '../../data/TravelData';

const InterestedPlace = () => {
    const navigate = useNavigate();
    const [locations, setLocations] = useState([]);
    const [selectedCards, setSelectedCards] = useState([]); // 선택된 카드를 저장하는 상태

    useEffect(() => {
        const fetchData = async () => {
            setLocations(TravelData);
        };

        fetchData();
    }, []);

    const handleCardClick = (id) => {
        setSelectedCards(prevSelected => {
            if (prevSelected.includes(id)) {
                return prevSelected.filter(cardId => cardId !== id); // 이미 선택된 카드 클릭 시 선택 해제
            } else {
                return [...prevSelected, id]; // 선택되지 않은 카드 클릭 시 선택 추가
            }
        });
    };

    const handleSubmit = () => {
        const selectedCount = selectedCards.length;
        if (selectedCount < 5) {
            alert(`관심여행지를 5개 이상 선택해주세요! (${selectedCount}/5)`);
        } else {
            navigate(`/`); // 선택된 카드가 5개 이상일 때 지정된 URL로 이동
        }
    };

    return (
        <section className="interested-page">
            <ProfileInfo text={"AI 맞춤 추천을 위해 관심 여행지를 5개 이상 선택해주세요."} src={progressbar3} />

            <section className="interested-body">
                <section className="travel-grid">
                    {locations.map(location => (
                        <TravelCard
                            key={location.id}
                            image={location.image}
                            tags={location.tags}
                            title={location.title}
                            location={location.location}
                            isSelected={selectedCards.includes(location.id)} // 선택 상태 전달
                            onClick={() => handleCardClick(location.id)} // 클릭 핸들러 전달
                        />
                    ))}
                </section>
                <section className="subbtn">
                    <Button text={"완료"} onClick={handleSubmit} />
                </section>
            </section>
        </section>
    );
};

export default InterestedPlace;
