import React, { useEffect, useState } from "react";
import axiosInstance from "./Axios";

const AxiosTestComponent = () => {
  const [data, setData] = useState(null);

  // 화면이 마운트 되면
  const fetchData = async () => {
    try {
      const response = await axiosInstance.get("keyword/random/trip"); // 실제 엔드포인트로 변경 필요
      setData(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return <button onClick={fetchData}>데이터 테스트</button>;
};

export default AxiosTestComponent;
