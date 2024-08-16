import "./ProfileInfo.css";

const ProfileInfo = ({text, src}) => {
    return(
        <div>
            <div className="profile-info">
                {text}
            </div>
            <div className="profile-progressbar">
                <img src={src} />
            </div>
        </div>
    );
};

export default ProfileInfo;