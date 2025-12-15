import axios from "axios";

const api = axios.create({
  baseURL: "/api", // ← Giống axios.js
  withCredentials: false,
});

export default api;
