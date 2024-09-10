import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000, // 포트 번호 설정
  },
  build: {
    chunkSizeWarningLimit: 1000, // 경고 한도를 1000kB로 증가
  },
});
