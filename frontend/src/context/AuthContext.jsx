import _ from "lodash";
import authAPI from "../api/requests/auth";
import { apiCaller } from "../utils/api";
import { getJwtData, removeJwtToken, setJwtToken } from "../utils/token";
import { createContext, useEffect, useState } from "react";
import AntdSwal from "../custom/AntdSwal";

const AuthContext = createContext({
  isLoggedIn: false,
  user: {},
  login: () => {},
  logout: () => {},
  sendCodeToSever: () => {},
});

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState({});

  useEffect(() => {
    const userData = getJwtData();

    if (!_.isEmpty(userData)) {
      setIsLoggedIn(true);
      setUser(userData);
    }
  }, []);

  const login = async (data) => {
    await apiCaller(authAPI.login(data), (resData) => {
      const accessToken = resData?.data?.token;
      setJwtToken(accessToken);
      const userData = getJwtData();

      if (!_.isEmpty(userData)) {
        setIsLoggedIn(true);
        setUser(userData);
        AntdSwal.fire("Thành công", "Bạn đã đăng nhập thành công", "success");
      }
    });
  };

  const sendCodeToSever = async (data) => {
    await apiCaller(authAPI.sendCodeToSever(data), (resData) => {
      const accessToken = resData?.data?.token;
      setJwtToken(accessToken);
      const userData = getJwtData();
      if (!_.isEmpty(userData)) {
        setIsLoggedIn(true);
        setUser(userData);
        AntdSwal.fire("Thành công", "Mật khẩu đã được đặt lại thành mã code gửi đến email. ", "success");
      }
    });
  };

  const logout = async () => {
    removeJwtToken();
    setIsLoggedIn(false);
    setUser({});
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, user, login, logout, sendCodeToSever }}>{children}</AuthContext.Provider>
  );
};

export default AuthContext;
