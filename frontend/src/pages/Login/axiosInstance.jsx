import axios from "axios";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: "https://www.moheng.xyz/api",
});

// 요청 인터셉터: 엑세스 토큰을 요청 헤더에 추가
axiosInstance.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      config.headers["Authorization"] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터: 엑세스 토큰 만료 시 리프레시 토큰으로 갱신
axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    // 엑세스 토큰 만료 시 처리
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        try {
          const response = await axiosInstance.post("/auth/extend/login", {
            token: refreshToken,
          });

          const newAccessToken = response.data.accessToken;
          if (newAccessToken) {
            localStorage.setItem("accessToken", newAccessToken);
            axiosInstance.defaults.headers.common["Authorization"] = `Bearer ${newAccessToken}`;
            return axiosInstance(originalRequest); // 원래의 요청을 다시 시도
          }
        } catch (refreshError) {
          console.error("Failed to refresh access token:", refreshError);
        }
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
