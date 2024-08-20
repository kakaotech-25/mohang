import { useState } from 'react';
import './Mypage.css';

function Mypage() {
  const [activeTab, setActiveTab] = useState('tab1');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  return (
    <div className="my-page">
      <section className="tabs">
        <button
          className={activeTab === 'tab1' ? 'tab-button active' : 'tab-button'}
          onClick={() => handleTabClick('tab1')}
        >
          프로필
        </button>
        <button
          className={activeTab === 'tab2' ? 'tab-button active' : 'tab-button'}
          onClick={() => handleTabClick('tab2')}
        >
          생활
        </button>
      </section>

      <section className="tab-content">
        {activeTab === 'tab1' && (
          <div id="tab1">
            <h1>프로필 공간</h1>
          </div>
        )}
        {activeTab === 'tab2' && (
          <div id="tab2">
            <h1>생활정보 공간</h1>
          </div>
        )}
      </section>
    </div>
  );
}

export default Mypage;
