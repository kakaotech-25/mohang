import axios from "axios";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: "localhost", // 서버주소
  //   withCredentials: true, // 자격증명을 포함한 쿠키를 서버로 전달
  //   headers: {
  //     "Content-Type": "application/json",
  //   },
  //   timeout: 10000,
});

// Axios 요청 전에 공통로직을 추가하기 위한 인터셉터
axiosInstance.interceptors.request.use(
  (config) => {
    // 로컬스토리지에서 가져와서 헤더에 추가하는 예제
    // const accessToken = localStorage.getItem("accessToken");
    // if (accessToken) {
    //   config.headers["토큰 토큰"] = `Bearer ${accessToken}`;
    // }

    // 쿠키값에서 가져와서 헤더에 추가하는 예제
    // const csrfToken = getCookie("csrf_access_token");
    // if (csrfToken) {
    //   config.headers["X-CSRF-TOKEN"] = csrfToken;
    // }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Axios 응답 시 처리할 공통로직 추가
axiosInstance.interceptors.response.use(
  (response) => {
    // 2xx 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
    return response;
  },
  async (error) => {
    // 2xx 외의 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
    return Promise.reject(error);
  }
);

export default axiosInstance;
