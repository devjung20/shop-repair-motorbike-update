import { API } from "../constants/constants";
import axios from "axios";
import { getJwtToken } from "../utils/token";
import AntdSwal from "../custom/AntdSwal";

// Create axios client with base settings
export const axiosClient = axios.create({
  baseURL: API.BASE_URL,
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
    "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept, Authorization",
  },
});

// Setting response interceptors
axiosClient.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    const res = error.response;

    if (res.status === 401) {
      AntdSwal.fire(
        "Lỗi xác thực",
        "Có vẻ như phiên truy cập của bạn đã hết hoặc không hợp lệ. Vui lòng đăng nhập lại",
        "error"
      );
      // removeJwtToken();
      // window.location.pathname = "/login";
    }
    return Promise.reject(error);
  }
);

// Setting request interceptors
axiosClient.interceptors.request.use(
  (config) => {
    const token = getJwtToken();
    if (token) {
      config.headers.Authorization = "Bearer " + token;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
