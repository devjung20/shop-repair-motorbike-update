import { generateApi } from "../../utils/api";

const authAPI = {
  login: (data, params) => {
    return generateApi("post", "/auth/login", params, data);
  },

  register: (data, params) => {
    return generateApi("post", "/auth/register", params, data);
  },
  sendEmail:(data,params) => {
    return generateApi("post","/auth/forgot-password",params,data)
  },
  sendCodeToSever:(data,params) => {
    return generateApi("post","/auth/forgot-password-confirm",params,data)
  }
};

export default authAPI;
